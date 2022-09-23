package com.lagou.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import java.util.List;

/**
 * 消息编解码器
 */
public class MessageCodec extends MessageToMessageCodec {

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
    System.out.println("消息正在进行编码......");
    ByteBuf byteBuf = Unpooled.copiedBuffer((String) msg, CharsetUtil.UTF_8);
    out.add(byteBuf);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
    System.out.println("正在进行消息解码......");
    ByteBuf byteBuf = (ByteBuf) msg;
    out.add(byteBuf.toString(CharsetUtil.UTF_8));//传递到下一个handler
  }
}
