package com.lagou.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务端
 */
public class NettyServer {

  public static void main(String[] args) throws InterruptedException {
    // 1. 创建bossGroup线程组: 处理网络事件--连接事件
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    // 2. 创建workerGroup线程组: 处理网络事件--读写事件2*处理器线程数
    EventLoopGroup workerGroup = new NioEventLoopGroup();

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

            //添加解码器
            // ch.pipeline().addLast("messageDecoder", new MessageDecoder());
            // ch.pipeline().addLast("messageEncode", new MessageEncoder());
            //添加编解码器
            ch.pipeline().addLast("messageCodec", new MessageCodec());

            // 8. 向pipeline中添加自定义业务处理handler
            ch.pipeline().addLast(new NettyServerHandler());
          }
        });

    // 9. 启动服务端并绑定端口,同时将异步改为同步
    // ChannelFuture future = serverBootstrap.bind(9999).sync();//同步
    ChannelFuture bind = serverBootstrap.bind(9999);//异步
    bind.addListener(future -> {
      if (future.isSuccess()) {
        System.out.println("端口绑定成功");
      } else {
        System.out.println("端口绑定失败");
      }
    });

    System.out.println("服务器启动成功....");
    // 10. 关闭通道(并不是真正意义上的关闭,而是监听通道关闭状态)
    //     关闭连接池
    bind.channel().closeFuture().sync();
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }


}
