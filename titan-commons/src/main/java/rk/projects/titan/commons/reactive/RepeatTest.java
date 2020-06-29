package rk.projects.titan.commons.reactive;

import java.time.Duration;
import reactor.core.publisher.Flux;

public class RepeatTest {

  public static void main(String[] args) throws InterruptedException {
    Flux.just("Hello World")
        .doOnSubscribe(s -> {
          System.out.println("Subscribed AGAIN");
        })
        .cache(Duration.ofMillis(100))
        .delayElements(Duration.ofMillis(1000))
        .repeat()
        .subscribe(r -> {
          System.out.println(r);
        });

    while (true) {
      Thread.sleep(1000);
    }
  }
}
