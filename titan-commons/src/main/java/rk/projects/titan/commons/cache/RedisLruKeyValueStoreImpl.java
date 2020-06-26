package rk.projects.titan.commons.cache;

import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import reactor.core.publisher.Mono;

public class RedisLruKeyValueStoreImpl<K, V> implements KeyValueStore<K, V> {

  private final LruSet<K> lruSet;
  private final StatefulRedisConnection<K, V> cache;
  private final Duration ttl;

  public RedisLruKeyValueStoreImpl(final StatefulRedisConnection<K, V> cache, Duration ttl) {
    this.cache = cache;
    this.lruSet = null;
    this.ttl = ttl;
  }


  @Override
  public Mono<V> get(K k) {
    return this.lruSet.get(k)
        .flatMap(element -> {
          return this.cache.reactive().get(k)
              .switchIfEmpty(this.lruSet.rem(k).flatMap(l -> Mono.empty()));
        });
  }

  @Override
  public Mono<V> set(K k, V v) {
    return this.cache.reactive().set(k, v)
        .flatMap(s -> {
          return this.lruSet.set(k)
              .flatMap(x -> Mono.just(v));
        });
  }
}
