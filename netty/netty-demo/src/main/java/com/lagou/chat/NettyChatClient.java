package com.lagou.chat;

import com.lagou.codec.MessageCodec;
import com.lagou.codec.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.util.Scanner;

/**
 * 聊天室客户端
 */
public class NettyChatClient {

  public int port;
  public String host;

  public NettyChatClient(String host, int port) {
    this.port = port;
    this.host = host;
  }

  public void run() {
    EventLoopGroup group = null;
    try {
      // 1. 创建线程组
      group = new NioEventLoopGroup();
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

              //添加解码器
              // ch.pipeline().addLast("messageDecoder", new MessageDecoder());
              // ch.pipeline().addLast("messageEncode", new MessageEncoder());
              // ch.pipeline().addLast("messageCodec", new MessageCodec());
              // 6. 向pipeline中添加自定义业务处理handler
              // 添加编码器
              ch.pipeline().addLast(new StringEncoder());
              ch.pipeline().addLast(new StringDecoder());

              // 添加客户端处理
              ch.pipeline().addLast(new NettyChatClientHandler());
            }
          });

      // 7. 启动客户端,等待连接服务端,同时将异步改为同步
      ChannelFuture future = bootstrap.connect(host, port).sync();
      Channel channel = future.channel();
      System.out.println("------" + channel.localAddress().toString().substring(1) + "------");
      Scanner scanner = new Scanner(System.in);
      while (scanner.hasNextLine()) {
        String message = scanner.nextLine();
        channel.writeAndFlush(message);
      }
      // 8. 关闭通道和关闭连接池
      channel.closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      group.shutdownGracefully();

    }

  }

  public static void main(String[] args) {
    new NettyChatClient("127.0.0.1", 9998).run();
  }

}
