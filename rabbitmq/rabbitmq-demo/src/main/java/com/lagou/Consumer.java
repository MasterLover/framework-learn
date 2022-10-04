package com.lagou;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {


  private final BlockingQueue<KouZhao> queue;

  public Consumer(BlockingQueue<KouZhao> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    while (true) {
      try {

        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("准备买口罩");
        KouZhao take = queue.take();
        System.out.println("买到第" + take.getId() + "个口罩");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
