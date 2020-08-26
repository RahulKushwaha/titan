package rk.projects.titan.commons.threadpool;

public class ContextualRunnable implements Runnable {

  private final Runnable runnable;

  public ContextualRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void run() {
    this.runnable.run();
  }
}
