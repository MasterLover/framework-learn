package com.lagou.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {

  public static void main(String[] args) throws IOException {
    ExecutorService executorService = Executors.newCachedThreadPool();
    ServerSocket serverSocket = new ServerSocket(9999);
    System.out.println("server starting");
    while (true) {
      final Socket accept = serverSocket.accept();
      System.out.println("有客户端连接");

      executorService.execute(() -> {
        handle(accept);
      });
    }
  }

  private static void handle(Socket socket) {
    try (socket) {
      System.out.println("线程ID:" + Thread.currentThread().getId());
      InputStream inputStream = socket.getInputStream();
      byte[] bytes = new byte[1024];
      int read = inputStream.read(bytes);
      System.out.println("client:" + new String(bytes, 0, read));
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write("hello".getBytes());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
