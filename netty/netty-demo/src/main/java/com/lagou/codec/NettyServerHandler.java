package com.lagou.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 自定义处理handler
 */
public class NettyServerHandler implements ChannelInboundHandler {

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {

  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {

  }

  /**
   * 通道读取事件.
   *
   * @param ctx
   * @param msg
   * @throws Exception
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // ByteBuf byteBuffer = (ByteBuf) msg;
    // System.out.println("client send:" + byteBuffer.toString(CharsetUtil.UTF_8));
    System.out.println("client send:" + msg);
  }

  /**
   * 通道读取完毕事件
   *
   * @param ctx
   * @throws Exception
   */

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    // 消息出栈
    // ctx.writeAndFlush(Unpooled.copiedBuffer("你好,我是Netty服务端", CharsetUtil.UTF_8));
    ctx.writeAndFlush("你好,我是Netty服务端");
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

  /**
   * 通道异常事件
   *
   * @param ctx
   * @param cause
   * @throws Exception
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

  }
}
