package com.lagou.impl;

import com.lagou.Hello;

public class HelloImpl implements Hello {

  @Override
  public void morning(String name) {
    System.out.println("hello:" + name);
  }
}
