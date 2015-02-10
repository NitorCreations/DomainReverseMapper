package com.nitorcreations;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> findClasses(final List<String> packages, final URLClassLoader classLoader) {
        List<Class<?>> allClasses = new ArrayList<>();
        for (String packageName : packages) {
            Reflections reflections = new Reflections(packageName, new SubTypesScanner(false), classLoader);
            allClasses.addAll(reflections.getSubTypesOf(Object.class));
        }
        return allClasses;
    }
}
