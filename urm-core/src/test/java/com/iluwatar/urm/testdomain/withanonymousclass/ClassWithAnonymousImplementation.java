package com.iluwatar.urm.testdomain.withanonymousclass;

public class ClassWithAnonymousImplementation {

  private ClassWithAnonymousImplementation self;

  /**
   * empty runnable method.
   */
  public ClassWithAnonymousImplementation() {
    Runnable anonImplementation = new Runnable() {
      @Override
      public void run() {
      }
    };
  }
}
