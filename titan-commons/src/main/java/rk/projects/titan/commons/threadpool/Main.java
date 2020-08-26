package rk.projects.titan.commons.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.asynchttpclient.AsyncHttpClient;

public class Main {

  public static void main(String[] args) {
    ThreadPoolExecutor threadPoolExecutor = new ContextualThreadPoolExecutor(1,
        2, 3,
        TimeUnit.MILLISECONDS,
        new LinkedTransferQueue<>());

    ContextualExecutorService contextualExecutorService = new ContextualExecutorService(
        threadPoolExecutor);
    CompletableFuture.supplyAsync(() -> {
      System.out.println("Hello World");
      return 42;
    }, contextualExecutorService).join();

    CompletableFuture.runAsync(() -> {
      System.out.println("Hello World");
    }, contextualExecutorService).join();
  }
}
