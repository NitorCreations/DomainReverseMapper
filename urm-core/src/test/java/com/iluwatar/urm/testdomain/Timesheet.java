package com.iluwatar.urm.testdomain;

import com.iluwatar.urm.testdomain.person.Employee;

import java.util.List;

public class Timesheet {
  private final Employee who;
  private final Task task;
  private Integer hours;
  private List<String> stuff;

  /**
   * constructor for the class.
   * @param who type of Employee
   * @param task type of Task
   * @param hours type of Integer
   */
  public Timesheet(final Employee who, final Task task, final Integer hours) {
    this.who = who;
    this.task = task;
    this.hours = hours;
  }

  public Employee getWho() {
    return who;
  }

  public Task getTask() {
    return task;
  }

  public Integer getHours() {
    return hours;
  }

  /**
   * Manager can alter hours before closing task.
   * @param hours New amount of hours
   */
  public void alterHours(final Integer hours) {
    this.hours = hours;
  }

  @Override
  public String toString() {
    return "Timesheet [who=" + who + ", task=" + task + ", hours=" + hours + "]";
  }
}
