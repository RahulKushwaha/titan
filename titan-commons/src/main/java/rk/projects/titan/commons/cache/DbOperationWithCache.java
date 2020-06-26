package rk.projects.titan.commons.cache;

import io.lettuce.core.Range;
import io.lettuce.core.api.StatefulRedisConnection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import reactor.core.publisher.Mono;

public class DbOperationWithCache<K, T> implements DbOperation<T> {

  private final DbOperation<T> dbDelegate;
  private final StatefulRedisConnection<K, T> cache;
  private final StatefulRedisConnection<String, K> negativeCache;
  private final Function<T, K> keygen;

  public DbOperationWithCache(final DbOperation<T> dbDelegate,
      final StatefulRedisConnection<K, T> cache,
      final StatefulRedisConnection<String, K> negativeCache,
      final Function<T, K> keyGen) {
    this.dbDelegate = dbDelegate;
    this.cache = cache;
    this.negativeCache = negativeCache;
    this.keygen = keyGen;
  }

  @Override
  public CompletableFuture<T> get(T t) {
    K k = this.keygen.apply(t);
    return this.negativeCache.reactive()
        .zscore("negativeCache", k)
        .flatMap(score -> negativeCache.reactive()
            .zadd("negativeCache", k)
            .flatMap(setSize -> Mono.<T>error(new RuntimeException("Not Found in DB"))))
        .switchIfEmpty(cache.reactive().get(k))
        .switchIfEmpty(Mono.fromFuture(this.dbDelegate.get(t))
            .doOnEach(dbObject -> {
              this.negativeCache.reactive().zrem("negativeCache", k).subscribe();
              this.cache.reactive().set(k, dbObject.get()).subscribe();
            }))
        .switchIfEmpty(negativeCache.reactive()
            .zadd("negativeCache", System.currentTimeMillis(), k)
            .flatMap(addedElementsCount -> {
              return negativeCache.reactive()
                  .zremrangebyscore("negativeCache",
                      Range.<Long>create(0L, System.currentTimeMillis()));
            })
            .flatMap(setSize -> Mono.<T>error(new RuntimeException("Not Found in DB"))))
        .toFuture();
  }

  @Override
  public CompletableFuture<T> set(T t) {
    return null;
  }
}
