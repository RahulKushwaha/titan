package rk.projects.titan.commons.groupmembership;

import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RedisGroupMembershipManager implements GroupMembershipManager {

  private final String groupId;
  private final String memberId;
  private final StatefulRedisConnection<String, String> cache;

  public RedisGroupMembershipManager(final StatefulRedisConnection<String, String> cache,
      final String groupId, final String memberId) {
    this.groupId = groupId;
    this.cache = cache;
    this.memberId = memberId;
  }

  private String getKey() {
    return String.format("%s::%d", this.groupId, System.currentTimeMillis() / 60_000);
  }

  private String getPrevKey() {
    return String.format("%s::%d", this.groupId,
        (System.currentTimeMillis() - Duration.ofMinutes(1).toMillis()) / 60_000);
  }

  /**
   * @param groupId Unique Id of the group.
   * @return Number of members in the group.
   */
  @Override
  public CompletableFuture<Integer> getCount(String groupId) {
    return Flux.merge(this.cache.reactive().hgetall(this.getKey()),
        this.cache.reactive().hgetall(this.getPrevKey()))
        .flatMapIterable(Map::entrySet)
        .filter(entry -> System.currentTimeMillis() - Long.parseLong(entry.getValue())
            <= Duration.ofMinutes(1)
            .toMillis())
        .distinct()
        .count()
        .map(Math::toIntExact)
        .toFuture();
  }

  /**
   * @param groupId  Unique Id of the group.
   * @param memberId Unique Id of the member.
   * @return CompletionStage representing successful update.
   */
  @Override
  public CompletableFuture<Void> update(String groupId, String memberId) {
    return this.cache.reactive()
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
              .hset(this.getKey(), this.memberId, Long.toString(System.currentTimeMillis()));
        })
        .toFuture()
        .thenAccept(r -> {
        });
  }
}
