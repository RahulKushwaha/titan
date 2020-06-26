package rk.projects.titan.resource;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rk.projects.titan.di.ManagedObject;

public class TestResource implements TestService {

  private static final Logger logger = LoggerFactory.getLogger(TestResource.class);

  private final ManagedObject managedObject;

  @Inject
  public TestResource(final ManagedObject managedObject) {
    this.managedObject = managedObject;
  }

  @Override
  public String ping(String pong) {
    return "pong-" + pong;
  }

  @PreDestroy
  public void dispose() {
    logger.info("Dispose called");
    this.managedObject.killingIt();
  }
}