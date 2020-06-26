package rk.projects.titan.commons;

import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class LazyGroupMembershipImpl implements LazyGroupMembership {

  private final String groupId;
  private final String clientId;
  private final StatefulRedisConnection<String, String> cache;
  private final Flux<Long> size;

  public LazyGroupMembershipImpl(final StatefulRedisConnection<String, String> cache,
      final String groupId, final String clientId) {
    this.groupId = groupId;
    this.cache = cache;
    this.clientId = clientId;
    this.size = this.getSizePublisher();
  }

  private String getKey() {
    return String.format("%s::%d", this.groupId, System.currentTimeMillis() / 60_000);
  }

  private String getPrevKey() {
    return String.format("%s::%d", this.groupId,
        (System.currentTimeMillis() - Duration.ofMinutes(1).toMillis()) / 60_000);
  }

  @Override
  public void start() {
    this.cache.reactive()
        .hsetnx(this.getKey(), "HASH_CREATED_TS", Long.toString(System.currentTimeMillis()))
        .flatMap(result -> {
          if (!result) {
            return this.cache.reactive()
                .expire(this.getKey(), Duration.ofMinutes(1).toMillis())
                .flatMap(r -> Mono.just(false));
          }

          return Mono.just(true);
        })
        .flatMap(dummyResult -> {
          return this.cache.reactive()
              .hset(this.getKey(), this.clientId, Long.toString(System.currentTimeMillis()));
        })
        .delayElement(Duration.ofSeconds(2))
        .onErrorContinue((e, s) -> {
          // Log error.
        })
        .repeat()
        .subscribe();
  }

  @Override
  public int size() {
    return Math.toIntExact(this.size.blockFirst());
  }

  private Flux<Long> getSizePublisher() {
    return Flux.merge(this.cache.reactive().hgetall(this.getKey()),
        this.cache.reactive().hgetall(this.getPrevKey()))
        .flatMapIterable(Map::entrySet)
        .filter(entry -> System.currentTimeMillis() - Long.parseLong(entry.getValue())
            <= Duration.ofMinutes(1)
            .toMillis())
        .distinct()
        .count()
        .repeat()
        .cache(Duration.ofSeconds(30));
  }
}
