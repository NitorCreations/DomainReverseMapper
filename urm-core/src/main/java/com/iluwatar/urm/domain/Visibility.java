package com.iluwatar.urm.domain;

public enum Visibility {
  PUBLIC("+"), PROTECTED("#"), DEFAULT("~"), PRIVATE("-");

  private String umlRepresentation;

  Visibility(String umlRepresentation) {
    this.umlRepresentation = umlRepresentation;
  }

  public String getUmlRepresentation() {
    return umlRepresentation;
  }

  @Override
  public String toString() {
    return getUmlRepresentation();
  }
}
