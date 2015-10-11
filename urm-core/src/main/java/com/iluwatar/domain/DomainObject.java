package com.iluwatar.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DomainObject {

    private static final Logger log = LoggerFactory.getLogger(DomainObject.class);

    public final String packageName;
    public final String className;
    public final String description;
    public List<String> fields = new ArrayList<>();
    public List<String> methods = new ArrayList<>();

    public DomainObject(String packageName, String className, String description) {
        this.packageName = packageName;
        this.className = className;
        this.description = description;
        Class<?> aClass = null;
        final String fqn = String.format("%s.%s", packageName, className);
        try {
            aClass = Class.forName(fqn);
            for (Field f: aClass.getFields()) {
                fields.add(formatFieldName(f));
            }
            for (Method m: aClass.getDeclaredMethods()) {
                methods.add(formatMethodName(m));
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not get class for name {}", fqn);
        }
    }

    public DomainObject(Class<?> clazz, String description) {
        this(clazz.getPackage().getName(), clazz.getSimpleName(), description);
    }

    public DomainObject(Class<?> clazz) {
        this(clazz, null);
    }

    private String formatMethodName(Method method) {
        final String replaceOld = method.getReturnType().getName();
        final String replaceNew = method.getReturnType().getSimpleName();
        final String remove = String.format("%s.%s.", packageName, className);
        final String s = method.toString().replace(remove, "").replace(replaceOld, replaceNew);
        return s;
    }

    private String formatFieldName(Field field) {
        return field.toString();
    }

    public String getPackageName() {
        return packageName;
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
