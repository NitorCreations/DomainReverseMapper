package com.nitorcreations.mappers;

import java.util.List;

public class AbstractMapper {
    protected final List<Class<?>> classes;

    public AbstractMapper(final List<Class<?>> classes) {
        this.classes = classes;
    }

    protected boolean isDomainClass(final Class<?> clazz) {
        return classes.contains(clazz);
    }

    protected boolean isDomainClass(final String name) {
        for (Class<?> clazz : classes) {
            if (clazz.getName().equals(stripClassHeader(name))) {
                return true;
            }
        }
        return false;
    }

    String stripClassHeader(final String fullToString) {
        if (fullToString.startsWith("class ")) {
            return fullToString.substring(6);
        }
        return fullToString;
    }
}
