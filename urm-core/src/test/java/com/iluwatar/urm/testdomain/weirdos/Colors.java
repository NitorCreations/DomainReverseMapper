package com.iluwatar.urm.testdomain.weirdos;

public enum Colors {
  RED,
  BARON_RED,
  BLOOD_RED,
  DARK_RED,
  SLIGHTLY_DARKER_RED,
  A_LOT_DARKER_RED,
  SO_RED_YOU_CANT_EVEN_IMAGINE;

  public int getRedness() {
    return ordinal();
  }

}
