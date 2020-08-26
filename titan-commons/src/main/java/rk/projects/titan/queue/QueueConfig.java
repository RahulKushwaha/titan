package rk.projects.titan.queue;

import com.google.common.collect.ImmutableList;
import java.util.concurrent.CompletionStage;
import org.apache.commons.math3.util.Pair;

public class QueueConfig {

  private final String p1Q;
  private final String p2Q;
  private final String p3Q;
  private final String visibilityTimeoutQ;
  private final ImmutableList<String> listNames;

  public QueueConfig(final String p1Q, final String p2Q, final String p3Q,
      final String visibilityTimeoutQ) {
    this.p1Q = p1Q;
    this.p2Q = p2Q;
    this.p3Q = p3Q;
    this.visibilityTimeoutQ = visibilityTimeoutQ;
    this.listNames = ImmutableList.of(this.p1Q, this.p2Q, this.p3Q, this.visibilityTimeoutQ);
  }

  public String getP1Q() {
    return p1Q;
  }

  public String getP2Q() {
    return p2Q;
  }

  public String getP3Q() {
    return p3Q;
  }

  public String getVisibilityTimeoutQ() {
    return visibilityTimeoutQ;
  }

  public ImmutableList<String> getAllListNames() {
    return this.listNames;
  }

  public String getQ(Priority priority) {
    switch (priority) {
      case P0:
        return this.p1Q;
      case P1:
        return this.p1Q;

      case P2:
      default:
        return this.p2Q;
    }
  }
}
