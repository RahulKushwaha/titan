package rk.projects.titan.commons.reactive;

import java.time.Duration;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

public class HedgingOperator<T> extends MonoOperator<T, T> {

  public HedgingOperator(Mono<? extends T> source) {
    super(source);
  }

  @Override
  public void subscribe(CoreSubscriber<? super T> actual) {
    Flux.merge(
        source,
        source.delaySubscription(Duration.ofMillis(10)),
        source.delaySubscription(Duration.ofMillis(20)))
        .take(1)
        .single()
        .subscribe(actual);
  }
}
