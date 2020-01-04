package com.iluwatar.urm.testdomain;

import com.iluwatar.urm.testdomain.person.Employee;
import com.iluwatar.urm.testdomain.person.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task {
  private final List<Employee> assignedEmployees = new ArrayList<>();
  private final Manager manager;
  private boolean completed;
  private final String description;

  /**
   * constructor for the class.
   * @param description type of string
   * @param manager type of Manager class
   * @param employees type of Employee class
   */
  public Task(final String description, final Manager manager, final Employee... employees) {
    this.description = description;
    this.manager = manager;
    assignedEmployees.addAll(Arrays.asList(employees));
    completed = false;
  }

  public Manager getManager() {
    return manager;
  }

  public List<Employee> getAssignedEmployees() {
    return assignedEmployees;
  }

  public void addEmployee(final Employee e) {
    assignedEmployees.add(e);
  }

  public void removeEmployee(final Employee e) {
    assignedEmployees.remove(e);
  }

  public void completeTask() {
    completed = true;
  }
}
