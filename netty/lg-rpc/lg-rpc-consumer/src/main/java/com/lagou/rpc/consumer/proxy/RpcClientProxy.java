package com.lagou.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.client.RpcClient;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 封装request请求. 创建rpcclient. 发送消息. 返回结果.
 */
public class RpcClientProxy {


  public static Object createProxy(Class serviceClass) {
    Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class[]{serviceClass}, (proxy, method, args) -> {
          RpcRequest rpcRequest = new RpcRequest();
          rpcRequest.setRequestId(UUID.randomUUID().toString());
          rpcRequest.setClassName(method.getDeclaringClass().getName());
          rpcRequest.setParameterTypes(method.getParameterTypes());
          rpcRequest.setParameters(args);
          rpcRequest.setMethodName(method.getName());
          RpcClient rpcClient = new RpcClient("127.0.0.1", 19898);
          try {
            Object message = rpcClient.send(JSON.toJSONString(rpcRequest));
            RpcResponse response = JSON.parseObject(message.toString(), RpcResponse.class);
            if (response.getError() != null) {
              throw new RuntimeException("error: " + response.getError());
            }
            return JSON.parseObject(response.getResult().toString(), method.getReturnType());
          } catch (Exception e) {
            throw new RuntimeException(e);
          } finally {
            rpcClient.close();
          }
        });
    return proxyInstance;
  }
}
