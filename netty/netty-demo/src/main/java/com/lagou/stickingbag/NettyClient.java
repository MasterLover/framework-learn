package com.lagou.stickingbag;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty客户端
 */
public class NettyClient {

  public static void main(String[] args) throws InterruptedException {
    // 1. 创建线程组
    EventLoopGroup group = new NioEventLoopGroup();
    // 2. 创建客户端启动助手
    Bootstrap bootstrap = new Bootstrap();
    // 3. 设置线程组
    bootstrap.group(group)
        // 4. 设置客户端通道实现为NIO
        .channel(NioSocketChannel.class)
        // 5. 创建一个通道初始化对象
        .handler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel ch) throws Exception {
            // 6. 向pipeline中添加自定义业务处理handler
            // 添加到末尾或者前面,链式
            ch.pipeline().addLast(new NettyClientHandler());
          }
        });

    // 7. 启动客户端,等待连接服务端,同时将异步改为同步
    ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
    // 8. 关闭通道和关闭连接池
    future.channel().closeFuture().sync();

    group.shutdownGracefully();

  }
}
