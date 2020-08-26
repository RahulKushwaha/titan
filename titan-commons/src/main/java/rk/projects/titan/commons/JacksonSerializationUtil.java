package rk.projects.titan.commons;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Function;
import javax.inject.Inject;

public class JacksonSerializationUtil implements SerializationUtil {

  private final ObjectMapper objectMapper;

  @Inject
  public JacksonSerializationUtil(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private <T> Function<Class<T>, Function<String, T>> deserializer() {
    return x -> y -> {
      try {
        return this.objectMapper.readValue(y, x);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private <T> Function<TypeReference<T>, Function<String, T>> typeRefDeserializer() {
    return x -> y -> {
      try {
        return this.objectMapper.readValue(y, x);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  public <T> Function<String, T> deserialize(final Class<T> tClass) {
    return this.<T>deserializer().apply(tClass);
  }

  @Override
  public <T> Function<String, T> deserialize(final TypeReference<T> tTypeReference) {
    return this.<T>typeRefDeserializer().apply(tTypeReference);
  }

  @Override
  public <T> Function<T, String> serialize() {
    return x -> {
      try {
        return objectMapper.writeValueAsString(x);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
