package com.lagou.rpc.provider.server;

import com.lagou.rpc.provider.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * server启动类
 */
@Service
public class RpcServer implements DisposableBean {

  @Autowired
   RpcServerHandler handler;
   NioEventLoopGroup bossGroup;
   NioEventLoopGroup workerGroup;

  public void startServer(String ip, int port) {
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new StringDecoder())
                  .addLast(new StringEncoder())
                  .addLast(handler);
            }
          });

      ChannelFuture sync = serverBootstrap.bind(ip, port).sync();
      System.out.println("==========服务端启动成功==========");
      sync.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      if (bossGroup != null) {
        bossGroup.shutdownGracefully();
      }
      if (workerGroup != null) {
        workerGroup.shutdownGracefully();
      }
    }
  }

  @Override
  public void destroy() {
    if (bossGroup != null) {
      bossGroup.shutdownGracefully();
    }
    if (workerGroup != null) {
      workerGroup.shutdownGracefully();
    }
  }
}
