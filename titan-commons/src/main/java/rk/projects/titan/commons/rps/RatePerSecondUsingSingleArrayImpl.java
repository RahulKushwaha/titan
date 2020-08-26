package rk.projects.titan.commons.rps;

import java.time.Clock;

public class RatePerSecondUsingSingleArrayImpl implements RatePerSecond {

  private final Clock clock;
  private final int bucketSize;
  private final int[] buckets;
  private int totalCount;
  private long lastTimestamp;

  public RatePerSecondUsingSingleArrayImpl(final Clock clock) {
    this.bucketSize = 100;
    this.buckets = new int[this.bucketSize];
    this.clock = clock;
    this.lastTimestamp = 0L;
  }

  private void reset() {
    for (int i = 0; i < this.bucketSize; i++) {
      this.buckets[0] = 0;
    }
  }

  @Override
  public void increment() {
    this.sanitizeBuckets(true);
  }

  @Override
  public int get() {
    return this.sanitizeBuckets(false);
  }

  private int sanitizeBuckets(boolean increment) {
    long currentTimestamp = this.clock.millis();
    int currentBucket = (int) (this.clock.millis() % this.bucketSize);

    if (currentTimestamp - this.lastTimestamp >= 1000) {
      this.reset();
      this.totalCount = 0;
    } else {
      int lastBucket = (int) (this.lastTimestamp % this.bucketSize);
      if (currentBucket > lastBucket) {
        for (int i = lastBucket + 1; i <= currentBucket && i < this.bucketSize; i++) {
          this.totalCount -= this.buckets[i];
        }
      } else if (currentBucket < lastBucket) {
        for (int i = lastBucket; i < this.bucketSize; i++) {
          this.totalCount -= this.buckets[i];
          this.buckets[i] = 0;
        }

        for (int i = 0; i <= currentBucket; i++) {
          this.totalCount -= this.buckets[i];
          this.buckets[i] = 0;
        }
      }
    }

    if (increment) {
      this.totalCount++;
      this.buckets[currentBucket]++;
      this.lastTimestamp = currentTimestamp;
    }

    return this.totalCount;
  }
}
