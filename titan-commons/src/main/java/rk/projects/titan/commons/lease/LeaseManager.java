package rk.projects.titan.commons.lease;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface LeaseManager {

  CompletableFuture<Lease> acquire(Duration leaseDuration);

  CompletableFuture<Lease> extend(Lease lease, Duration leaseDuration);

  CompletableFuture<Integer> total();

  CompletableFuture<Integer> totalAvailable();
}
