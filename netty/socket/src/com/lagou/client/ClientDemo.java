package com.lagou.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientDemo {

  public static void main(String[] args) throws IOException {
    while (true) {
      Socket socket = new Socket("127.0.0.1", 9999);
      OutputStream outputStream = socket.getOutputStream();
      System.out.println("请输入:");
      Scanner scanner = new Scanner(System.in);
      String next = scanner.next();
      outputStream.write(next.getBytes());

      InputStream inputStream = socket.getInputStream();
      byte[] b = new byte[1024];
      int read = inputStream.read(b);
      System.out.println("老板说:" + new String(b, 0, read).trim());
      socket.close();

    }
  }

}
