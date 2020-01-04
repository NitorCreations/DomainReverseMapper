package com.iluwatar.urm.testdomain.person;

public class Employee extends Person {
  private final String name;
  private final String department;

  public Employee(final String name, final String department) {
    this.name = name;
    this.department = department;
  }

  public String getName() {
    return name;
  }

  public String getDepartment() {
    return department;
  }
}
