package rk.projects.titan.commons.reactive;

import java.time.Duration;
import reactor.core.publisher.Flux;

public class Cached {

  public static void main(String[] args) throws InterruptedException {
    Flux.just("Started")
        .map(r -> {
          System.out.println("Time Requested");
          return System.currentTimeMillis();
        })
        .cache(Duration.ofSeconds(10))
        .delaySequence(Duration.ofSeconds(1))
        .repeat()
        .subscribe(r -> {
          System.out.println(r);
        });

    while (true) {
      Thread.sleep(100);
    }
  }
}
