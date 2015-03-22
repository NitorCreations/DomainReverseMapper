package com.nitorcreations;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class DomainClassFinder {

    private static boolean shouldBeIncluded(Class<?> clazz) {
        return !clazz.getSimpleName().equals("package-info");
    }

    public static List<Class<?>> findClasses(final List<String> packages, final URLClassLoader classLoader) {
        return packages.stream()
                .map(packageName -> getClasses(classLoader, packageName))
                .flatMap(Collection::stream)
                .filter(DomainClassFinder::shouldBeIncluded)
                .collect(Collectors.toList());
    }

    private static Set<Class<?>> getClasses(URLClassLoader classLoader, String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false), classLoader);
        return reflections.getSubTypesOf(Object.class);
    }

    public static List<Class<?>> findClasses(final List<String> packages) {
        return findClasses(packages, null);
    }

    private DomainClassFinder() {
        // private constructor for utility class
    }
}
