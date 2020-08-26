package rk.projects.titan.commons.reactive;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RepeatTest {

  public static void main(String[] args) throws InterruptedException {
//    Flux.just("Hello World")
//        .doOnSubscribe(s -> {
//          System.out.println("Subscribed AGAIN");
//        })
//        .cache(Duration.ofMillis(100))
//        .delayElements(Duration.ofMillis(1000))
//        .repeat()
//        .subscribe(r -> {
//          System.out.println(r);
//        });

    Flux.zip(Mono.empty(), Mono.empty())
        .doOnComplete(() -> {
          System.out.println("Completed");
        })
        .subscribe();

    while (true) {
      Thread.sleep(1000);
    }
  }
}
