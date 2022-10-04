package com.lagou.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOClient {

  public static void main(String[] args) throws IOException {
    // 设置连接端口号
    SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));

    socketChannel.write(ByteBuffer.wrap("老板还钱".getBytes(StandardCharsets.UTF_8)));

    ByteBuffer allocate = ByteBuffer.allocate(1024);
    int read = socketChannel.read(allocate);
    System.out.println(new String(allocate.array(), 0, read, StandardCharsets.UTF_8));

    socketChannel.close();
  }

}
