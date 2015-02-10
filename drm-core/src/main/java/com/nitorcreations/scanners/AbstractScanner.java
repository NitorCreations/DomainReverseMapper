package com.nitorcreations.scanners;

import java.util.List;

public class AbstractScanner {
    protected final List<Class<?>> classes;

    public AbstractScanner(final List<Class<?>> classes) {
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
            return convertFromAsmToComplete(fullToString.substring(6));
        }
        return convertFromAsmToComplete(fullToString);
    }

    String convertFromAsmToComplete(String original) {
        return original.replace('/', '.');
    }

}
