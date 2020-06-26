package rk.projects.titan.di.factory;

import javax.inject.Inject;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rk.projects.titan.di.ManagedObject;

public class ManagedObjectFactory implements Factory<ManagedObject> {

  private final ServiceLocator serviceLocator;

  private static final Logger logger = LoggerFactory.getLogger(ManagedObjectFactory.class);

  @Inject
  public ManagedObjectFactory(final ServiceLocator serviceLocator) {
    this.serviceLocator = serviceLocator;
  }

  /**
   * This method will create instances of the type of this factory.  The provide
   * method must be annotated with the desired scope and qualifiers.
   *
   * @return The produces object
   */
  @Override
  public ManagedObject provide() {
    // Create an instance of Managed Object.
    return this.serviceLocator.createAndInitialize(ManagedObject.class);
  }

  /**
   * This method will dispose of objects created with this scope.  This method should
   * not be annotated, as it is naturally paired with the provide method
   *
   * @param instance The instance to dispose of
   */
  @Override
  public void dispose(ManagedObject instance) {
    instance.killingIt();
  }
}
