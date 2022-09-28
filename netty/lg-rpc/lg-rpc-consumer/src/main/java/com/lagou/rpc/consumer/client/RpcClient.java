package com.lagou.rpc.consumer.client;

import com.lagou.rpc.consumer.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * client .connect to netty server
 * <p>offer close method</p>
 * <p>message send</p>
 */
public class RpcClient {

  private final String ip;
  private final int port;

  private NioEventLoopGroup group;
  private Channel channel;

  private final RpcClientHandler rpcClientHandler = new RpcClientHandler();

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  public RpcClient(String ip, int port) {
    this.ip = ip;
    this.port = port;
    initClient();
  }

  public void initClient() {
    group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    try {
      bootstrap.group(group).channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                  .addLast(rpcClientHandler);
            }
          });
      //客户端连接服务端
      channel = bootstrap.connect(ip, port).sync().channel();
    } catch (InterruptedException e) {
      e.printStackTrace();
      close();
    }

  }

  public Object send(String message) throws ExecutionException, InterruptedException {
    rpcClientHandler.setRequestMessage(message);
    Future submit = executorService.submit(rpcClientHandler);
    return submit.get();

  }

  public void close() {
    if (group != null) {
      group.shutdownGracefully();
    }
    if (channel != null) {
      channel.close();
    }
  }
}
