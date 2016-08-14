package de.markusmo3.urm.domain;

import com.thoughtworks.paranamer.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * Created by moe on 09.04.16.
 */
public abstract class DomainExecutable<T extends Executable> {

    private static final boolean useParameterNames = Boolean.parseBoolean(System.getProperty("useParameterNames", "true"));
    private static final String JAVA_IDENTIFIER_REGEX = "([a-zA-Z_$][a-zA-Z\\d_$]*)";

    private static final Paranamer PARANAMER = new AdaptiveParanamer(new DefaultParanamer(),
            new BytecodeReadingParanamer(), new NullParanamer());
    private T executable;

    public DomainExecutable(T executable) {
        this.executable = executable;
    }

    protected T getExecutable() {
        return executable;
    }

    protected String getName() {
        return executable.getName();
    }

    public String getUmlName() {
        // Have to do the old for loop way because Paranamer doesnt have a nice interface
        String[] parameterNames = PARANAMER.lookupParameterNames(executable, false);
        Parameter[] parameters = executable.getParameters();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            String paraName = ((parameterNames.length != 0 && useParameterNames) ? parameterNames[i] + " : " : "");
            sb.append(paraName + TypeUtils.getSimpleName(parameters[i].getParameterizedType()));

            if (i != parameters.length - 1) {
                sb.append(", ");
            }
        }

        return getName() + "(" + sb.toString() + ")";
    }

    public Class<?> getDeclaringClass() {
        return executable.getDeclaringClass();
    }

    public Visibility getVisibility() {
        return TypeUtils.getVisibility(executable.getModifiers());
    }

    public boolean isStatic() {
        return Modifier.isStatic(executable.getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(executable.getModifiers());
    }

    @Override
    public String toString() {
        return getUmlName();
    }
}
