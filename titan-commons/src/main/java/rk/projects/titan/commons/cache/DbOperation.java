package rk.projects.titan.commons.cache;

import java.util.concurrent.CompletableFuture;

public interface DbOperation<T> {

  CompletableFuture<T> get(T t);

  CompletableFuture<T> set(T t);
}
