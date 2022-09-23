package com.lagou.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NIOSelectorServer {

  public static void main(String[] args) throws IOException {
    // 打开一个服务通道
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

    // 绑定端口
    serverSocketChannel.bind(new InetSocketAddress(9999));

    // 非阻塞
    serverSocketChannel.configureBlocking(false);

    // 获取selector
    Selector selector = Selector.open();

    // 注册连接事件
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    // 检查是否有相关事件
    System.out.println("server started");

    while (true) {
      // 判断事件个数
      int select = selector.select(2000);
      if (select == 0) {
        System.out.println("no  message");
        continue;
      }

      Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        // 是不是连接事件
        if (key.isAcceptable()) {
          // 获取到通道
          SocketChannel socketChannel = serverSocketChannel.accept();
          System.out.println("client connected");
          //必须设置成非阻塞
          socketChannel.configureBlocking(false);
          // 注册读事件
          socketChannel.register(selector, SelectionKey.OP_READ);
        }
        if (key.isReadable()) {
          // 得到客户端通道, 读取数据到缓冲区
          SocketChannel channel = (SocketChannel) key.channel();

          ByteBuffer allocate = ByteBuffer.allocate(1024);
          int read = channel.read(allocate);
          if (read > 0) {
            System.out.println(
                "client:" + new String(allocate.array(), 0, read, StandardCharsets.UTF_8));

            // 回写数据
            channel.write(ByteBuffer.wrap("没钱".getBytes(StandardCharsets.UTF_8)));
            channel.close();
          }
        }

        // 删除事件
        iterator.remove();
      }
    }
  }

}
