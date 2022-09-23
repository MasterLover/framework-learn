package com.lagou.netty;

import com.lagou.config.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Netty服务器
 */
@Component
public class NettyWebSocketServer implements Runnable {

  @Autowired
  NettyConfig nettyConfig;

  @Autowired
  WebSocketChannelInit webSocketChannelInit;


  private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

  private final EventLoopGroup workerGroup = new NioEventLoopGroup();

  /**
   * 资源关闭--在springIoC容器销毁时关闭
   */
  @PreDestroy
  public void close() {
    shutdown();
  }

  @Override
  public void run() {
    try {
      //1.创建服务端启动助手
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      //2.设置线程组
      serverBootstrap.group(bossGroup, workerGroup);
      //3.设置参数
      serverBootstrap.channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.DEBUG))
          .childHandler(webSocketChannelInit);
      //4.启动
      ChannelFuture channelFuture = serverBootstrap.bind(nettyConfig.getPort()).sync();
      System.out.println("--Netty服务端启动成功---");
      channelFuture.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
      shutdown();
    } finally {
      shutdown();
    }
  }

  /**
   * 关闭服务器.
   */
  private void shutdown() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
}
