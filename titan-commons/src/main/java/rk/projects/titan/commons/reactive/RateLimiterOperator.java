package rk.projects.titan.commons.reactive;

import com.google.common.util.concurrent.RateLimiter;
import java.util.function.UnaryOperator;
import reactor.core.publisher.Flux;

public class RateLimiterOperator<T> implements UnaryOperator<Flux<T>> {

  private final RateLimiter rateLimiter;

  public RateLimiterOperator(final RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  public static <V> RateLimiterOperator<V> of(final RateLimiter rateLimiter) {
    System.out.println("New Rate Limiter created.");
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
    return new FluxRateLimiter<>(tFlux, this.rateLimiter);
  }
}
