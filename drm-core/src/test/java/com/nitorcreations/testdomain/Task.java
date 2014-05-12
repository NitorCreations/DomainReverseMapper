package com.nitorcreations.testdomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitorcreations.testdomain.person.Employee;
import com.nitorcreations.testdomain.person.Manager;

public class Task {
    private final List<Employee> assignedEmployees = new ArrayList<Employee>();
    private final Manager manager;
    private boolean completed;
    private final String description;

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
