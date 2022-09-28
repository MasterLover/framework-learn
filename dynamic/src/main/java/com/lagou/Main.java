package com.lagou;

import com.lagou.impl.HelloImpl;
import com.lagou.proxy.HelloProxy;
import java.lang.reflect.Proxy;

public class Main {

  public static void main(String[] args) {
    dynamicProxyMethod();
    // staticProxyMethod();
  }

  private static void staticProxyMethod() {
    HelloImpl hello = new HelloImpl();
    HelloProxy proxy = new HelloProxy(hello);
    proxy.morning("kaka");
  }

  private static void dynamicProxyMethod() {
    Hello o = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class},
        (proxy, method, args1) -> {
          System.out.println(method);
          if (method.getName().equals("morning")) {
            System.out.println("GoodMorning" + args1[0]);
          }
          return null;
        });
    o.morning("ka13265ka");
  }
}
