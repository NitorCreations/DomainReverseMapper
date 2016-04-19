package com.iluwatar.domain;

import com.thoughtworks.paranamer.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * Created by moe on 09.04.16.
 */
public class DomainMethod {

    private static final boolean useParameterNames = Boolean.parseBoolean(System.getProperty("useParameterNames", "true"));
    private static final String JAVA_IDENTIFIER_REGEX = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)";

    private Paranamer paranamer = new AdaptiveParanamer(new DefaultParanamer(),
            new BytecodeReadingParanamer(), new NullParanamer());
    private Method method;

    public DomainMethod(Method method) {
        this.method = method;
    }

    public String getUmlName() {
        // Have to do the old for loop way because Paranamer doesnt have a nice interface
        String[] parameterNames = paranamer.lookupParameterNames(method, false);
        Parameter[] parameters = method.getParameters();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            String paraName = (parameterNames.length != 0 && useParameterNames ? parameterNames[i] + " : " : "");
            sb.append(paraName + cutPackages(parameters[i].getParameterizedType().getTypeName()));

            if (i != parameters.length - 1) {
                sb.append(", ");
            }
        }

        return method.getName() + "("
                + sb.toString()
                + ") : " + method.getReturnType().getSimpleName();
    }

    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    public Visibility getVisibility() {
        if (Modifier.isPublic(method.getModifiers())) {
            return Visibility.PUBLIC;
        } else if (Modifier.isProtected(method.getModifiers())) {
            return Visibility.PROTECTED;
        } else if (Modifier.isPrivate(method.getModifiers())) {
            return Visibility.PRIVATE;
        } else {
            return Visibility.DEFAULT;
        }
    }

    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    private static String cutPackages(String parameter) {
        return parameter.replaceAll(JAVA_IDENTIFIER_REGEX + "*\\.", "");
    }

}
