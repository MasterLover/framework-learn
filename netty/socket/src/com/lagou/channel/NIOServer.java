package com.lagou.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NIOServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    // 创建
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

    // 端口
    serverSocketChannel.bind(new InetSocketAddress(9999));

    // 非阻塞
    serverSocketChannel.configureBlocking(false);

    System.out.println("server  started....");

    while (true) {
      // check  client
      SocketChannel socketChannel = serverSocketChannel.accept();
      if (socketChannel == null) {
        System.out.println("no client .....");
        TimeUnit.SECONDS.sleep(1);
        continue;
      }
      ByteBuffer allocate = ByteBuffer.allocate(1024);

      // 返回值: 正数 有效字节数   0 没有读到数据 -1 读到末尾
      int read = socketChannel.read(allocate);

      System.out.println("msg:" + new String(allocate.array(), 0, read, StandardCharsets.UTF_8));

      // 会写数据
      ByteBuffer wrap = ByteBuffer.wrap("没钱".getBytes(StandardCharsets.UTF_8));
      socketChannel.write(wrap);

      // close
      socketChannel.close();
    }


  }

}
