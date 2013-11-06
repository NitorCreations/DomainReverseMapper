package com.nitorcreations;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainMapperFactory {
    private static final Logger log = LoggerFactory.getLogger(DomainMapperFactory.class);

    public DomainMapper create(final String[] packages, final URLClassLoader classLoader) throws ClassNotFoundException {
        List<Class<?>> allClasses = findClasses(packages, classLoader);
        log.debug("Found " + allClasses.size() + " classes.");
        return new DomainMapper(allClasses);
    }

    public DomainMapper create(final String[] packages) throws ClassNotFoundException {
        return create(packages, null);
    }

    private List<Class<?>> findClasses(final String[] packages, final URLClassLoader classLoader) {
        List<Class<?>> allClasses = new ArrayList<Class<?>>();
        for (String packageName : packages) {
            Reflections reflections = new Reflections(packageName, new SubTypesScanner(false), classLoader);
            allClasses.addAll(reflections.getSubTypesOf(Object.class));
        }
        return allClasses;
    }
}
