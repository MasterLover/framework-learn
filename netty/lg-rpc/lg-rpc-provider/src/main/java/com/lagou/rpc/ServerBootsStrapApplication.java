package com.lagou.rpc;

import com.lagou.rpc.provider.server.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerBootsStrapApplication implements CommandLineRunner {

  @Autowired
  private RpcServer rpcServer;

  public static void main(String[] args) {
    SpringApplication.run(ServerBootsStrapApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    new Thread(new Runnable() {
      @Override
      public void run() {
        rpcServer.startServer("127.0.0.1", 19898);
      }
    }).start();
  }
}
