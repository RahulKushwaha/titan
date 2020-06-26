package rk.projects.titan.commons.cache;

import io.lettuce.core.api.StatefulRedisConnection;
import reactor.core.publisher.Mono;

public class RedisLruSetImpl<K> implements LruSet<K> {

  private final StatefulRedisConnection<String, K> cache;
  private final String name;
  private final long maxSize;

  public RedisLruSetImpl(final StatefulRedisConnection<String, K> cache, final String name,
      final long maxSize) {
    this.cache = cache;
    this.name = name;
    this.maxSize = maxSize;
  }

  @Override
  public Mono<K> get(K k) {
    return this.cache.reactive()
        .zrank(this.name, k)
        .flatMap(rank -> {
          return this.cache.reactive()
              .zadd(this.name, System.currentTimeMillis(), k)
              .flatMap(numberOfElements -> {
                return Mono.just(k);
              });
        });
  }

  @Override
  public Mono<K> set(K k) {
    return this.get(k)
        .switchIfEmpty(this.cache.reactive()
            .zadd(this.name, System.currentTimeMillis(), k)
            .flatMap(score -> {
              return this.cache.reactive()
                  .zremrangebyrank(this.name, this.maxSize, Long.MAX_VALUE)
                  .flatMap(numberOfElementsRemove -> {
                    return Mono.just(k);
                  });
            }));
  }

  @Override
  public Mono<Long> rem(K k) {
    return this.cache.reactive().zrem(this.name, k);
  }
}
