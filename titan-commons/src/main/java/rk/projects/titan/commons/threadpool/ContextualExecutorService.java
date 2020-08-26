package rk.projects.titan.commons.threadpool;

import com.google.common.util.concurrent.ForwardingExecutorService;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ContextualExecutorService extends ForwardingExecutorService {

  private final ExecutorService executorService;

  public ContextualExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  @Override
  protected ExecutorService delegate() {
    return this.executorService;
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    System.out.println(
        "public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)");
    return super.invokeAll(tasks);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout,
      TimeUnit unit) throws InterruptedException {
    System.out.println("invokeAll(Collection<? extends Callable<T>> tasks, long timeout,\n"
        + "      TimeUnit unit)");
    return super.invokeAll(tasks, timeout, unit);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    System.out.println("public <T> T invokeAny(Collection<? extends Callable<T>> tasks)");
    return super.invokeAny(tasks);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    System.out.println(
        " public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)");
    return super.invokeAny(tasks, timeout, unit);
  }

  @Override
  public void execute(Runnable command) {
    System.out.println("public void execute(Runnable command)");
    System.out.println(command.getClass());
    super.execute(new ContextualRunnable(command));
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    System.out.println(" public <T> Future<T> submit(Callable<T> task)");
    return super.submit(task);
  }

  @Override
  public Future<?> submit(Runnable task) {
    System.out.println("public Future<?> submit(Runnable task) ");
    return super.submit(task);
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    System.out.println("public <T> Future<T> submit(Runnable task, T result)");
    return super.submit(task, result);
  }
}
