package com.lagou.unpacking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端处理类
 */
public class NettyClientHandler implements ChannelInboundHandler {

  public int count = 0;

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

  }

  /**
   * 通道就绪事件.
   *
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    for (int i = 0; i < 10; i++) {
      ChannelFuture channelFuture = ctx.writeAndFlush(
          Unpooled.copiedBuffer("你好.我是Netty客户端" + i, CharsetUtil.UTF_8));
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {

  }

  /**
   * 通道读取就绪事件
   *
   * @param ctx
   * @param msg
   * @throws Exception
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    System.out.println("server response: " + byteBuf.toString(CharsetUtil.UTF_8));
    System.out.println("读取次数" + (++count));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

  }
}
