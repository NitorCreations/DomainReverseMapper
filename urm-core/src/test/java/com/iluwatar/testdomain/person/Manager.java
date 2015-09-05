package com.iluwatar.testdomain.person;

public class Manager extends Person {
    private final String name;

    public Manager(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
