package com.lagou.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import java.util.Objects;

/**
 * 聊天室服务端
 */
public class NettyHttpServer {


  private final int port;

  public NettyHttpServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws InterruptedException {
    new NettyHttpServer(8080).run();
  }

  public void run() throws InterruptedException {

    // 1. 创建bossGroup线程组: 处理网络事件--连接事件
    EventLoopGroup bossGroup = null;
    // 2. 创建workerGroup线程组: 处理网络事件--读写事件2*处理器线程数
    EventLoopGroup workerGroup = null;
    try {

      bossGroup = new NioEventLoopGroup(1);

      workerGroup = new NioEventLoopGroup();
      // 3. 创建服务端启动助手
      ServerBootstrap serverBootstrap = new ServerBootstrap();

      // 4. 设置bossGroup线程组和workerGroup线程组
      serverBootstrap.group(bossGroup, workerGroup)
          // 5. 设置服务端通道实现为NIO
          .channel(NioServerSocketChannel.class)
          // 6. 参数设置
          .option(ChannelOption.SO_BACKLOG, 128)
          // 活跃状态
          .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
          // 7. 创建一个通道初始化对象
          .childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
              // 添加编码器
              ch.pipeline().addLast(new HttpServerCodec());
              // 8. 向pipeline中添加自定义业务处理handler
              ch.pipeline().addLast(new NettyHttpServerHandler());

            }
          });

      // 9. 启动服务端并绑定端口,同时将异步改为同步
      ChannelFuture bind = serverBootstrap.bind(port);//异步
      bind.addListener(future -> {
        if (future.isSuccess()) {
          System.out.println("端口绑定成功");
        } else {
          System.out.println("端口绑定失败");
        }
      });

      System.out.println("HttpServer启动成功....");
      // 10. 关闭通道(并不是真正意义上的关闭,而是监听通道关闭状态)
      //     关闭连接池
      bind.channel().closeFuture().sync();
    } finally {
      Objects.requireNonNull(bossGroup).shutdownGracefully();
      Objects.requireNonNull(workerGroup).shutdownGracefully();
    }
  }
}
