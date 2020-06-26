package rk.projects.titan;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import rk.projects.titan.commons.sqs.MessageOrchestrator;

public class ImmediateFeature implements Feature {

  @Inject
  public ImmediateFeature(ServiceLocator locator) {
    ServiceLocatorUtilities.enableImmediateScope(locator);
  }

  @Override
  public boolean configure(FeatureContext context) {
    return true;
  }
}