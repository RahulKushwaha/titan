package rk.projects.titan.commons.reactive;

import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;

public class SwitchIfEmptyTest {

  public static void main(String[] args) throws InterruptedException {
    Mono.just(4)
        .switchIfEmpty(Mono.defer(() -> Mono.fromFuture(fun1())))
        .subscribe(r -> {
          System.out.println(r);
        });

    while (true) {
      Thread.sleep(1000);
    }
  }

  public static CompletableFuture<Integer> fun1() {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("RUnning not required");
      return 42;
    });
  }
}
