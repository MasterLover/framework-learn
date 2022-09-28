package com.lagou.proxy;

import com.lagou.Hello;

public class HelloProxy implements Hello {


  private final Hello hello;

  public HelloProxy(Hello hello) {
    this.hello = hello;
  }

  @Override
  public void morning(String name) {
    System.out.println("proxy before hello");
    hello.morning(name);
    System.out.println("proxy after hello");
  }

}
