package rk.projects.titan.commons.rps;

import java.time.Clock;

public class RatePerSecondWithDualArrayImpl implements RatePerSecond {

  private final Clock clock;
  private final int bucketSize;
  private final int[] counters;
  private final long[] timestamps;

  public RatePerSecondWithDualArrayImpl(final Clock clock) {
    this.bucketSize = 100;
    this.clock = clock;
    this.counters = new int[this.bucketSize];
    this.timestamps = new long[this.bucketSize];
  }

  @Override
  public void increment() {
    long currentTs = this.clock.millis();
    int currentBucket = (int) (currentTs % this.bucketSize);

    long diff = currentTs - timestamps[currentBucket];
    if (diff < 1000) {
      counters[currentBucket]++;
    } else {
      timestamps[currentBucket] = currentTs;
      counters[currentBucket] = 1;
    }
  }

  @Override
  public int get() {
    long currentTs = this.clock.millis();
    int sum = 0;
    for (int i = 0; i < this.bucketSize; i++) {
      if (currentTs - timestamps[i] < 1000) {
        sum += counters[i];
      }
    }

    return sum;
  }
}
