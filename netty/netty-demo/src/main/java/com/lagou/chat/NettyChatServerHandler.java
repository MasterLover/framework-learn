package com.lagou.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天室业务处理类
 */
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

  public static final String CONNECTED = "上线了.";
  public static final String DISCONNECTED = "下线了.";
  public static final String ERROR = "异常";
  private static final List<Channel> channelList = new ArrayList<>();


  /**
   * 异常处理事件.
   *
   * @param ctx
   * @param cause
   * @throws Exception
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // cause.printStackTrace();
    systemPrint(ctx.channel(), ERROR);
  }

  /**
   * 通道就绪事件.
   *
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //当有新客户端连接的时候放入集合中
    Channel channel = ctx.channel();
    channelList.add(channel);
    systemPrint(channel, CONNECTED);
  }


  /**
   * 通道为就绪--channel下线. 客户端断开连接.
   *
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channelList.remove(channel);
    systemPrint(channel, DISCONNECTED);
  }

  private static void systemPrint(Channel channel, String down) {
    System.out.println("[Server]:+" + getChannelIP(channel) + down);
  }

  /**
   * 通道读取事件.
   *
   * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
   *            belongs to
   * @param msg the message to handle
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    //当前发送消息的通道.当前发送的客户端连接.
    Channel src = ctx.channel();
    channelList.forEach(des -> send(msg, src, des));
  }

  /**
   * 发消息.
   *
   * @param msg
   * @param src
   * @param des
   */
  private static void send(String msg, Channel src, Channel des) {
    if (src != des) {
      des.writeAndFlush(
          "[%s]说:%s".formatted(getChannelIP(src), msg));
    }
  }

  /**
   * 获取channel ip.
   *
   * @param channel
   * @return
   */
  private static String getChannelIP(Channel channel) {
    return channel.remoteAddress().toString().substring(1);
  }
}
