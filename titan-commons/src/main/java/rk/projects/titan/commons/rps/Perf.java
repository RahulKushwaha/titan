package rk.projects.titan.commons.rps;

import java.time.Clock;

public class Perf {

  private final static RatePerSecondWithDualArrayImpl RATE_PER_SECOND_WITH_DUAL_ARRAY_IMPL = new RatePerSecondWithDualArrayImpl(
      Clock.systemUTC());

  private final static RatePerSecondUsingSingleArrayImpl RATE_PER_SECOND_USING_SINGLE_ARRAY_IMPL = new RatePerSecondUsingSingleArrayImpl(
      Clock.systemUTC());
//
//  @org.openjdk.jmh.annotations.Benchmark
//  public void measureRpsIncrement() {
//    ratePerSecond.increment();
//  }
//
//  @org.openjdk.jmh.annotations.Benchmark
//  public void measureRpsGet() {
//    ratePerSecond.get();
//  }
//
//  @org.openjdk.jmh.annotations.Benchmark
//  public void measureRpsIncrementAndGet() {
//    ratePerSecond.increment();
//    ratePerSecond.get();
//  }

  @org.openjdk.jmh.annotations.Benchmark
  public void measureRpsGetAndIncrement() {
    RATE_PER_SECOND_WITH_DUAL_ARRAY_IMPL.get();
    RATE_PER_SECOND_WITH_DUAL_ARRAY_IMPL.increment();
  }
}
