package rk.projects.titan.commons.reactive;

import java.util.function.UnaryOperator;
import reactor.core.publisher.Mono;

public class Hedging<T> implements UnaryOperator<Mono<T>> {

  public static void main(String[] args) {
    Mono.just("hello")
        .transform(new Hedging<>())
        .subscribe();
  }
  /**
   * Applies this function to the given argument.
   *
   * @param tMono the function argument
   * @return the function result
   */
  @Override
  public Mono<T> apply(Mono<T> tMono) {
    return new HedgingOperator<T>(tMono);
  }
}
