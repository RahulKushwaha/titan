package rk.projects.titan.commons;

import rk.projects.titan.commons.annotationprocessing.ToBuilder;

@ToBuilder
public abstract class Person {

  private final String firstName;
  private final String lastName;
  private final String email;

  public Person(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
}
