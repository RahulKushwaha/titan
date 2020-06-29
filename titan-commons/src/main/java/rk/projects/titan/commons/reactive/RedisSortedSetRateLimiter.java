package rk.projects.titan.commons.reactive;

import io.lettuce.core.api.StatefulRedisConnection;

public class RedisSortedSetRateLimiter {

  private final StatefulRedisConnection<String, String> cache;

  public RedisSortedSetRateLimiter(final StatefulRedisConnection<String, String> cache) {
    this.cache = cache;
  }

  public boolean acquire() {
    return true;
  }
}
