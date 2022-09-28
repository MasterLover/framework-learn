package com.lagou.rpc.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.Callable;

/**
 * client handler.
 *
 * @author Master
 * @since 2022/09/27 21:14
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

  private ChannelHandlerContext handlerContext;
  private String requestMessage;
  private String responseMessage;

  public void setRequestMessage(String requestMessage) {
    this.requestMessage = requestMessage;
  }

  /**
   * 连接就绪事件。
   *
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    this.handlerContext = ctx;
  }

  /**
   * read ready.
   *
   * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
   *            belongs to
   * @param msg the message to handle
   * @throws Exception
   */
  @Override
  protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    this.responseMessage = msg;
    notify();
  }

  /**
   * send message
   *
   * @return
   * @throws Exception
   */
  @Override
  public synchronized Object call() throws Exception {
    handlerContext.writeAndFlush(requestMessage);
    wait();
    return responseMessage;
  }
}
