package com.lagou;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {

  private final BlockingQueue<KouZhao> queue;

  private Integer index = 0;

  public Producer(BlockingQueue<KouZhao> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.MILLISECONDS.sleep(200);

        if (queue.remainingCapacity() <= 0) {
          System.out.println("口罩生产太多了");
        } else {

          KouZhao kouZhao = new KouZhao();
          kouZhao.setType("N95");
          kouZhao.setId(index++);
          queue.put(kouZhao);
          System.out.println("已经生产了" + queue.size() + "个口罩");
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }


    }
  }
}
