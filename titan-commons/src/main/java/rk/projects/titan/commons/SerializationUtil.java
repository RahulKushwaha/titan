package rk.projects.titan.commons;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.function.Function;

public interface SerializationUtil {

  <T> Function<String, T> deserialize(final TypeReference<T> tTypeReference);

  <T> Function<String, T> deserialize(final Class<T> tClass);

  <T> Function<T, String> serialize();
}
