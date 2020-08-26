package rk.projects.titan.commons.lease;

import java.util.Date;

public class Lease {

  private final String id;
  private final Date acquiredAt;

  public Lease(String id, Date acquiredAt) {
    this.id = id;
    this.acquiredAt = acquiredAt;
  }

  public String getId() {
    return id;
  }

  public Date getAcquiredAt() {
    return acquiredAt;
  }
}
