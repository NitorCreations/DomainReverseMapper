package com.iluwatar.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by moe on 09.04.16.
 */
public class DomainClass {

    private static final Logger log = LoggerFactory.getLogger(DomainClass.class);

    private static final List<String> IGNORED_METHODS = Arrays.asList("private static boolean[] $jacocoInit()");
    private static final List<String> IGNORED_FIELDS = Arrays.asList("private static boolean[] $jacocoInit()");

    private Class<?> clazz;
    private String description;
    private List<DomainField> fieldList;
    private List<DomainMethod> methodList = new ArrayList<>();

    private boolean isLambda(String s) {
        return s.contains("lambda$");
    }

    public DomainClass(Class<?> clazz, String description) {
        this.clazz = clazz;
        this.description = description;

        for (Method jm: clazz.getDeclaredMethods()) {
            DomainMethod dm = new DomainMethod(jm);
            if (!IGNORED_METHODS.contains(dm.getUmlName()) && !isLambda(dm.getUmlName())) {
                methodList.add(dm);
            }
        }
        methodList.sort(Comparator.comparing(DomainMethod::getUmlName));

        fieldList = Arrays.stream(clazz.getDeclaredFields())
                .map(DomainField::new)
                .filter(f -> !IGNORED_FIELDS.contains(f))
                .sorted(Comparator.comparing(DomainField::getUmlName))
                .collect(Collectors.toList());
    }

    public DomainClass(Class<?> clazz) {
        this(clazz, null);
    }

    public String getPackageName() {
        return clazz.getPackage().getName();
    }

    public String getClassName() {
        return clazz.getSimpleName();
    }

    public String getDescription() {
        return description;
    }

    public List<DomainMethod> getMethods() {
        return methodList;
    }

    public List<DomainField> getFields() {
        return fieldList;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
