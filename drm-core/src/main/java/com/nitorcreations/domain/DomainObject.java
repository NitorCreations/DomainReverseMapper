package com.nitorcreations.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DomainObject {

    public final String packageName;
    public final String className;

    public DomainObject(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public DomainObject(Class<?> clazz) {
        this.packageName = clazz.getPackage().getName();
        this.className = clazz.getSimpleName();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
