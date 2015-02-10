package com.nitorcreations.domain;

public class DomainObject {

    final String packageName;
    final String className;

    public DomainObject(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }
}
