package rk.projects.titan.di.factory;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import rk.projects.titan.di.ManagedObject;

public class FactoryBinder extends AbstractBinder {

  /**
   * Implement to provide binding definitions using the exposed binding
   * methods.
   */
  @Override
  protected void configure() {
    this.bindFactory(ManagedObjectFactory.class)
        .to(ManagedObject.class);
  }
}
