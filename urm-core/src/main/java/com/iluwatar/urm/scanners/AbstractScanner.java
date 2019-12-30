package com.iluwatar.urm.scanners;

import java.util.List;

class AbstractScanner {
  final List<Class<?>> classes;

  AbstractScanner(final List<Class<?>> classes) {
    this.classes = classes;
  }

  boolean isDomainClass(final Class<?> clazz) {
    return classes.contains(clazz);
  }

  boolean isDomainClass(final String name) {
    for (Class<?> clazz : classes) {
      if (clazz.getName().equals(stripClassHeader(name))) {
        return true;
      }
    }
    return false;
  }

  String stripClassHeader(final String fullToString) {
    if (fullToString.startsWith("class ")) {
      return convertFromAsmToComplete(fullToString.substring(6));
    } else if (fullToString.startsWith("interface ")) {
      return convertFromAsmToComplete(fullToString.substring(10));
    }
    return convertFromAsmToComplete(fullToString);
  }

  String convertFromAsmToComplete(String original) {
    return original.replace('/', '.');
  }

}
