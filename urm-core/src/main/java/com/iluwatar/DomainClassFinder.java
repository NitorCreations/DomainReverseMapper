package com.iluwatar;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class DomainClassFinder {

    public static List<Class<?>> findClasses(final List<String> packages, final URLClassLoader classLoader) {
        return packages.stream()
                .map(packageName -> getClasses(classLoader, packageName))
                .flatMap(Collection::stream)
                .filter(DomainClassFinder::isNotPackageInfo)
                .filter(DomainClassFinder::isNotAnonymousClass)
                .collect(Collectors.toList());
    }

    private static boolean isNotPackageInfo(Class<?> clazz) {
        return !clazz.getSimpleName().equals("package-info");
    }

    private static boolean isNotAnonymousClass(Class<?> clazz) {
        return !clazz.getSimpleName().equals("");
    }

    private static Set<Class<?>> getClasses(URLClassLoader classLoader, String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false), classLoader);
        return reflections.getSubTypesOf(Object.class);
    }

    private DomainClassFinder() {
        // private constructor for utility class
    }
}
