package com.iluwatar.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by moe on 10.04.16.
 */
public class DomainField {
    private Field field;

    public DomainField(Field field) {
        this.field = field;
    }

    public String getUmlName() {
        return field.getName() + " : " + field.getType().getSimpleName();
    }

    public Visibility getVisibility() {
        if (Modifier.isPublic(field.getModifiers())) {
            return Visibility.PUBLIC;
        } else if (Modifier.isProtected(field.getModifiers())) {
            return Visibility.PROTECTED;
        } else if (Modifier.isPrivate(field.getModifiers())) {
            return Visibility.PRIVATE;
        } else {
            return Visibility.DEFAULT;
        }
    }

    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(field.getModifiers());
    }

}
