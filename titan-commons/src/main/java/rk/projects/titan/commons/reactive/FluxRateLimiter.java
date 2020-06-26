package rk.projects.titan.commons.reactive;

import com.google.common.util.concurrent.RateLimiter;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxOperator;
import reactor.core.publisher.Mono;

public class FluxRateLimiter<T> extends FluxOperator<T, T> {

  private final RateLimiter rateLimiter;

  public FluxRateLimiter(Flux<? extends T> source, final RateLimiter rateLimiter) {
    super(source);
    this.rateLimiter = rateLimiter;
  }

  @Override
  public void subscribe(CoreSubscriber<? super T> actual) {
    boolean hasPermit = this.rateLimiter.tryAcquire();
    if (hasPermit) {
      source.subscribe(actual);
      return;
    }

    Mono.defer(() -> Mono.just(this.rateLimiter.acquire()))
        .subscribe(r -> {
          source.subscribe(actual);
        });
  }
}
