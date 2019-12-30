package com.iluwatar.urm.domain;

import java.lang.reflect.Constructor;

/**
 * Created by moe on 26.04.16.
 */
public class DomainConstructor extends DomainExecutable<Constructor> {

  public DomainConstructor(Constructor constructor) {
    super(constructor);
  }

  @Override
  protected String getName() {
    // to prevent it from printing FQN
    return TypeUtils.getSimpleName(getExecutable().getDeclaringClass());
  }
}
