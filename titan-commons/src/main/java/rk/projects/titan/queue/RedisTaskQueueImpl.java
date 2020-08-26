package rk.projects.titan.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.Value;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class RedisTaskQueueImpl<T> implements TaskQueue<Message<T>> {

  private final RedisAdvancedClusterReactiveCommands<String, Message<T>> db;
  private final RedisAdvancedClusterReactiveCommands<String, String> visibleMessages;
  private final QueueConfig queueConfig;
  private final String[] listNames;
  private final Duration blpopTimeout;
  private final ObjectMapper objectMapper;
  private final Clock clock;

  public RedisTaskQueueImpl(final RedisAdvancedClusterReactiveCommands<String, Message<T>> db,
      final RedisAdvancedClusterReactiveCommands<String, String> visibleMessage,
      final QueueConfig queueConfig, final Clock clock, final ObjectMapper objectMapper) {
    this.db = db;
    this.visibleMessages = visibleMessage;
    this.queueConfig = queueConfig;
    this.listNames = queueConfig.getAllListNames()
        .toArray(new String[this.queueConfig.getAllListNames().size()]);
    this.blpopTimeout = Duration.ofMillis(100);
    this.objectMapper = objectMapper;
    this.clock = clock;
  }

  @Override
  public CompletableFuture<Message<T>> poll() {
    return this.db.blpop(this.blpopTimeout.toMillis(), this.listNames)
        .map(Value::getValue)
        .flatMap(message -> {
          return this.visibleMessages.zadd(message.getId(), this.clock.millis())
              .
        })
        .toFuture();
  }

  @Override
  public CompletableFuture<Void> push(Message<T> message) {
    return this.db.lpush(this.queueConfig.getQ(message.getPriority()), message)
        .then()
        .toFuture();
  }

  @Override
  public CompletableFuture<Void> markAsComplete() {
    return null;
  }

  private String serialize(Message<T> message) {
    try {
      return this.objectMapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JsonProcessing Failed", e);
    }
  }
}
