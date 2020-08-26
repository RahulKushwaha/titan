package rk.projects.titan.queue;

import java.util.concurrent.CompletableFuture;

public interface TaskQueue<M> {

  CompletableFuture<M> poll();

  CompletableFuture<Void> push(M message);

  CompletableFuture<Void> markAsComplete();
}
