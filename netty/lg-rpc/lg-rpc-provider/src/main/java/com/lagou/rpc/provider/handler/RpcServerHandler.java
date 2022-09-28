package com.lagou.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.provider.anno.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * server business.
 * <p>1. get bean with @RpcService annotation from cache</p>
 * <P>2. receive client request</P>
 * <p>3. get bean from beanCache</p>
 * <p>4. parse method name,param type,param info from request</p>
 * <p>5. reflect invoke bean method</p>
 * <p>6. response to client</p>
 */
@Component
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements
    ApplicationContextAware {

  private static final Map<String, Object> SERVICE_INSTANCE_MAP = new HashMap<>();

  /**
   * read ready event
   *
   * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
   *            belongs to
   * @param msg the message to handle
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    //format

    RpcRequest request = JSON.parseObject(msg, RpcRequest.class);
    RpcResponse response = new RpcResponse();
    response.setRequestId(request.getRequestId());
    try {
      response.setResult(handler(request));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    ctx.writeAndFlush(JSON.toJSONString(response));
  }

  public Object handler(RpcRequest rpcRequest) throws InvocationTargetException {
    // 3.根据传递过来的beanName从缓存中查找到对应的bean
    Object serviceBean = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
    if (serviceBean == null) {
      throw new RuntimeException("根据beanName找不到服务,beanName:" + rpcRequest.getClassName());
    }
    //4.解析请求中的方法名称. 参数类型 参数信息
    Class<?> serviceBeanClass = serviceBean.getClass();
    String methodName = rpcRequest.getMethodName();
    Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
    Object[] parameters = rpcRequest.getParameters();
    //5.反射调用bean的方法- CGLIB反射调用
    FastClass fastClass = FastClass.create(serviceBeanClass);
    FastMethod method = fastClass.getMethod(methodName, parameterTypes);
    return method.invoke(serviceBean, parameters);

  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Map<String, Object> beans = applicationContext.getBeansWithAnnotation(
        RpcService.class);
    if (!beans.isEmpty()) {
      for (Entry<String, Object> entry : beans.entrySet()) {
        Object serviceBean = entry.getValue();
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length == 0) {
          throw new RuntimeException(" no implement");
        }
        String beanName = interfaces[0].getName();
        SERVICE_INSTANCE_MAP.put(beanName, serviceBean);
      }
    }
  }
}
