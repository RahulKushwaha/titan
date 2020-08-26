package rk.projects.titan.commons.reactive;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import reactor.core.publisher.Mono;

public class BufferdValues {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Started Bufferedh");
//    Flux.range(0, 30_000_000)
//        .map(i -> i % 50000)
//       // .buffer(Duration.ofMillis(1000))
//        //.window(Duration.ofMillis(10000))
//        .flatMap(i -> {
//          System.out.println("HERE: " + i.getClass());
//          return i
//              .groupBy(t -> t, Integer.MAX_VALUE)
//              .flatMap(h -> {
//                AtomicInteger f = new AtomicInteger(0);
//                return h.sample(Duration.ofMillis(1000))
//                    .window(Duration.ofMillis(10))
//                    .flatMap(v -> {
//                      return v.sample(Duration.ofMillis(10));
//                    })
//                    ;
//              });
//        })
//       //
//        .flatMap(i -> {
//          System.out.println(i);
//          return Mono.just(i);
//        })
//        .count()
//        .subscribeOn(Schedulers.elastic())
//        .subscribe(c -> {
//          System.out.println("FINISHED: " + c);
//        });
    AtomicLong generated = new AtomicLong();
    AtomicLong processed = new AtomicLong();
    final Random random = new Random();
    Mono.defer(() -> Mono.just(random.nextInt()))
        .repeat(Integer.MAX_VALUE)
        .doOnEach(r -> {
          generated.getAndIncrement();
        })
        .map(Math::abs)
        .map(i -> i )
        .groupBy(t -> t)
        .flatMap(i -> {
          System.out.println("Group Created: " + i.key());
          return i.sample(Duration.ofMillis(1000));
        }, Integer.MAX_VALUE)
        //.flatMap(i -> i)
        .doOnEach(i -> {
          processed.getAndIncrement();
//          System.out.println(
//              i.get() + " Generated: " + generated.get() + " Processed: " + processed.get());

        })
        .count()
        .doOnError(e -> {
          e.printStackTrace();
        })
        .subscribe(c -> {
          System.out.println("FINISHED: " + c);
        });

    while (true) {
      Thread.sleep(100);
    }
  }
}
