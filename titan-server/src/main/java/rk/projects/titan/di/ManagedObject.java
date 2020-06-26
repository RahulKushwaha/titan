package rk.projects.titan.di;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagedObject {

  private static final Logger logger = LoggerFactory.getLogger(ManagedObject.class);

  @Inject
  public ManagedObject() {
    logger.info("Constructor Called");
  }

  @PostConstruct
  public void init() {
    logger.info("Managed Object Init called");
  }

  @PreDestroy
  public void killingIt() {
    logger.info("ManagedObject Destroyed");
  }
}
