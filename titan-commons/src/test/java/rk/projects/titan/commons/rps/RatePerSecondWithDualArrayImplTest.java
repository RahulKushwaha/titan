package rk.projects.titan.commons.rps;


import java.time.Clock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class RatePerSecondWithDualArrayImplTest {

  private RatePerSecondWithDualArrayImpl ratePerSecondWithDualArrayImpl;
  private Clock clock;

  @Before
  public void setUp() throws Exception {
    this.clock = Mockito.mock(Clock.class);
    this.ratePerSecondWithDualArrayImpl = new RatePerSecondWithDualArrayImpl(this.clock);
  }

  @After
  public void tearDown() throws Exception {
  }

  private void createRequest(int counter) {
    for (int i = 0; i < counter; i++) {
      this.ratePerSecondWithDualArrayImpl.increment();
    }
  }

  @Test
  public void test1RPS() {
    this.createRequest(1);
    Assertions.assertEquals(1, this.ratePerSecondWithDualArrayImpl.get());
  }

  @Test
  public void test100RPS() {
    this.createRequest(100);
    Assertions.assertEquals(100, this.ratePerSecondWithDualArrayImpl.get());
  }

  @Test
  public void test1000RPS() {
    this.createRequest(1000);
    Assertions.assertEquals(1000, this.ratePerSecondWithDualArrayImpl.get());
  }

  @Test
  public void testRPSWithinSameSecondWindow() {
    long currentTimestamp = System.currentTimeMillis();
    Mockito.when(this.clock.millis()).thenReturn(currentTimestamp);
    this.createRequest(1000);
    Assertions.assertEquals(1000, this.ratePerSecondWithDualArrayImpl.get());
  }

  @Test
  public void testRPSWithinSameSecondWindowFullScan() {
    long currentTimestamp = System.currentTimeMillis();
    Mockito.when(this.clock.millis()).thenReturn(currentTimestamp);
    this.createRequest(1000);

    // Make sure all the millis till next second return same RPS value.
    for (int i = 0; i < 1000; i++) {
      Mockito.when(this.clock.millis()).thenReturn(currentTimestamp + i);
      Assertions.assertEquals(1000, this.ratePerSecondWithDualArrayImpl.get());
    }
  }

  @Test
  public void testRPSWithin2SecondWindow() {
    long currentTimestamp = System.currentTimeMillis();
    Mockito.when(this.clock.millis()).thenReturn(currentTimestamp);
    this.createRequest(1000);
    Mockito.when(this.clock.millis()).thenReturn(currentTimestamp + 1000);
    Assertions.assertEquals(0, this.ratePerSecondWithDualArrayImpl.get());
  }
}