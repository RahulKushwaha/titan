package rk.projects.titan.commons.reactive;

import com.google.common.util.concurrent.RateLimiter;
import java.util.HashMap;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

public class RateLimiterTest {

  public static void main(String[] args) throws InterruptedException {
    HashMap<String, String> map = new HashMap<>();
    map.put("Hello", "World");

    Flux.just("Hello World")
        .transform(RateLimiterOperator.of(RateLimiter.create(1)))
        .repeat()
        .doOnEach(s -> {
          System.out.println(s.getContext());
        })
        .subscribe(r -> {
          System.out.println(r);
        });

    while (true) {
      Thread.sleep(1000);
    }
  }
}
