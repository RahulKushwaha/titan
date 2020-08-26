package rk.projects.titan.queue;

import com.fasterxml.jackson.core.type.TypeReference;

public class Message<T> {

  private final String id;
  private final T payload;
  private final TypeReference<T> typeReference;
  private final Priority priority;

  private Message(String id, T payload, Priority priority) {
    this.id = id;
    this.payload = payload;
    this.typeReference = new TypeReference<T>() {
    };
    this.priority = priority;
  }

  public String getId() {
    return id;
  }

  public T getPayload() {
    return payload;
  }

  public TypeReference<T> getTypeReference() {
    return typeReference;
  }

  public Priority getPriority() {
    return priority;
  }

  public static final class MessageBuilder<T> {

    private String id;
    private T payload;
    private Priority priority = Priority.P2;

    private MessageBuilder() {
    }

    public static <U> MessageBuilder<U> getInstance() {
      return new MessageBuilder<U>();
    }

    public MessageBuilder<T> withId(String id) {
      this.id = id;
      return this;
    }

    public MessageBuilder<T> withPayload(T payload) {
      this.payload = payload;
      return this;
    }

    public MessageBuilder<T> withPriority(Priority priority) {
      this.priority = priority;
      return this;
    }

    public Message<T> build() {
      return new Message<T>(id, payload, priority);
    }
  }
}
