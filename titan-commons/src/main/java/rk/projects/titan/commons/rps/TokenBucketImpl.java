package rk.projects.titan.commons.rps;

import java.time.Clock;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketImpl implements TokenBucket {

  private final AtomicInteger tokens;
  private final Clock clock;
  private boolean forceRefill;
  private long lastRefillTimestamp;
  private Config config;

  public TokenBucketImpl(final double refillRatePerSecond, final Clock clock) {
    this.clock = clock;
    this.lastRefillTimestamp = this.clock.millis();
    this.config = new Config(refillRatePerSecond);
    this.tokens = new AtomicInteger((int) refillRatePerSecond);
    this.forceRefill = false;
  }

  @Override
  public boolean acquire() {
    this.refill();
    int tokenCount;
    if ((tokenCount = this.tokens.get()) > 0
        && this.tokens.compareAndSet(tokenCount, tokenCount - 1)) {
      return true;
    }

    return false;
  }

  @Override
  public void setRefillRate(double refillRatePerSecond) {
    this.config = new Config(refillRatePerSecond);
    this.forceRefill = true;
  }

  private void refill() {
    Config currentConfig = this.config;
    long currentTimestamp = this.clock.millis();
    long timeDifference = currentTimestamp - this.lastRefillTimestamp;

    if (this.forceRefill || timeDifference >= currentConfig.refillAfter) {
      this.tokens.set(Math.min(currentConfig.maxTokens,
          this.tokens.get() + (int) (timeDifference * currentConfig.refillRatePerSecond)));
      this.lastRefillTimestamp = currentTimestamp;
    }
  }

  private static final class Config {

    private final double refillRatePerSecond;
    private final int maxTokens;
    private final long refillAfter;

    public Config(double refillRatePerSecond) {
      this.refillRatePerSecond = refillRatePerSecond;
      this.maxTokens = (int) refillRatePerSecond;
      this.refillAfter = (long) (1000D / refillRatePerSecond);
    }
  }
}
