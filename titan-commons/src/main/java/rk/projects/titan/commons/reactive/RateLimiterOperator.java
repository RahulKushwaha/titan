package rk.projects.titan.commons.reactive;

import com.google.common.util.concurrent.RateLimiter;
import java.util.function.UnaryOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RateLimiterOperator<T> implements UnaryOperator<Flux<T>> {

  private final RateLimiter rateLimiter;

  public RateLimiterOperator(final RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  public static <V> RateLimiterOperator<V> of(final RateLimiter rateLimiter) {
    return new RateLimiterOperator<>(rateLimiter);
  }

  /**
   * Applies this function to the given argument.
   *
   * @param tFlux the function argument
   * @return the function result
   */
  @Override
  public Flux<T> apply(Flux<T> tFlux) {
    if (rateLimiter.tryAcquire()) {
      return tFlux;
    }

    return Flux.defer(() -> Mono.just(this.rateLimiter.acquire()))
        .flatMap(ignoreResult -> {
          return tFlux;
        });
  }
}
