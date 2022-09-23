package com.lagou.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * http服务器处理类
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


  /**
   * 读取就绪事件.
   * <p>读取浏览器消息.</p>
   *
   * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
   *            belongs to
   * @param msg the message to handle
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    // 1. 判断http请求
    if (!(msg instanceof HttpRequest)) {
      return;
    }
    //不响应/favicon.ico
    DefaultHttpRequest request = (DefaultHttpRequest) msg;
    if ("/favicon.ico".equals(request.uri())) {
      return;
    }
    System.out.println("浏览器请求URI: " + request.uri());
    // 2.响应浏览器
    ByteBuf byteBuf = Unpooled.copiedBuffer("hello,netty", CharsetUtil.UTF_8);
    DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
        HttpResponseStatus.OK, byteBuf);
    // 2.1设置响应头
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
    // 出栈
    ctx.channel().writeAndFlush(response);
  }
}
