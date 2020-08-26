package rk.projects.titan.commons.rps;

import java.time.Clock;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.openjdk.jmh.annotations.Setup;
import reactor.core.publisher.Flux;

public class TokenBucketTest {

  private TokenBucket tokenBucket;
  private Clock clock;

  @Setup
  public void setup() {
    this.clock = Mockito.mock(Clock.class);
    this.tokenBucket = new TokenBucketImpl(1, clock);
  }

  @Test
  public void testAcquire() {
    Mockito.when(this.clock.millis()).thenReturn(1L);
    int numberOfTokens = Math.toIntExact(Flux.range(1, 200)
        .map(i -> this.tokenBucket.acquire())
        .count().block());

    Assertions.assertEquals(numberOfTokens, 1);
  }

  @Test
  public void testAcquireDuring5Seconds() {
    Mockito.when(this.clock.millis()).thenReturn(1L);
    int numberOfTokens = Math.toIntExact(Flux.range(1, 200)
        .map(i -> this.tokenBucket.acquire())
        .count().block());

    Mockito.when(this.clock.millis()).thenReturn(1000L);
    numberOfTokens += Math.toIntExact(Flux.range(1, 200)
        .map(i -> this.tokenBucket.acquire())
        .count().block());

    Assertions.assertEquals(numberOfTokens, 2);
  }
}
