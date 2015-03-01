package com.nitorcreations.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DomainObject {

    public final String packageName;
    public final String className;
    public final String description;

    public DomainObject(String packageName, String className, String description) {
        this.packageName = packageName;
        this.className = className;
        this.description = description;
    }

    public DomainObject(Class<?> clazz, String description) {
        this(clazz.getPackage().getName(), clazz.getSimpleName(), description);
    }

    public DomainObject(Class<?> clazz) {
        this(clazz, null);
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
