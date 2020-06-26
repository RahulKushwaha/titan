package rk.projects.titan.commons.reactive;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorAnyOf {

  public static void main(String[] args) {
    Flux<String> firstFlux = Flux.just("Hello World")
        .delaySequence(Duration.ofDays(1))
        .flatMap(r -> {
          return Mono.<String>error(new RuntimeException("T"));
        })
        .doOnCancel(() -> {
          System.out.println("Flux Cancelled");
        });

    Flux<String> secondFlux = Flux.just("Not a hello world");

    Flux.merge(firstFlux, secondFlux)
        .take(1)
        .subscribe(r -> {
          System.out.println(r);
        });
  }
}
