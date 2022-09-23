package com.lagou.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import java.util.List;

/**
 * 处理消息编码.
 */
public class MessageEncoder extends MessageToMessageEncoder {

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
    System.out.println("消息正在进行编码......");
    ByteBuf byteBuf = Unpooled.copiedBuffer((String) msg, CharsetUtil.UTF_8);
    out.add(byteBuf);
  }
}
