package rk.projects.titan.commons.rps;

public interface TokenBucket {

  boolean acquire();

  void setRefillRate(double refillRatePerSecond);
}
