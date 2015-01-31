package com.nitorcreations.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.nitorcreations.domain.CompositionLink;
import com.nitorcreations.domain.Link;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositionMapper extends AbstractMapper {
    final Logger logger = LoggerFactory.getLogger(CompositionMapper.class);

    final List<CompositionLink> links = new LinkedList<>();

    public CompositionMapper(final List<Class<?>> classes) throws ClassNotFoundException {
        super(classes);
        gatherLinks();
    }

    private void gatherLinks() {
        for (final Class<?> clazz : classes) {
            try {
                InputStream is = clazz.getClassLoader().getResourceAsStream(clazz.getName().replace(".", "/") + ".class");
                ClassReader reader = new ClassReader(is);
                reader.accept(new ClassVisitor(Opcodes.ASM4) {
                    @Override
                    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                        try {
                            addLink(clazz, clazz.getDeclaredField(name));
                        } catch (NoSuchFieldException e) {
                            // should never happen
                        } catch (NoClassDefFoundError e) {
                            logger.warn("Skipped field " + name + " in class " + clazz.getName() + " because it's type class is not available. Field description: " + desc);
                        }
                        return super.visitField(access, name, desc, signature, value);
                    }
                }, ClassReader.SKIP_CODE);
            } catch (IOException e) {
                logger.warn("Failed to read bytecode for class " + clazz.getName(), e);
            }
        }
    }

    private void addLink(Class<?> clazz, Field field) {
        CompositionLink link = createLink(clazz, field);
        if (link != null) {
            CompositionLink match = findMatch(link);
            if (match == null || link.isDoubleReferer(match)) {
                getLinks().add(link);
            } else {
                removeMatch(link);
                getLinks().add(new CompositionLink(link.getA(), link.getAtoBname(), link.isCollectionA(), match.getB(), match.getBtoAname(), match.isCollectionB()));
            }
        }
    }

    private void removeMatch(final CompositionLink link) {
        getLinks().remove(link);
        getLinks().remove(link.getInverse());
    }

    private CompositionLink findMatch(final CompositionLink link) {
        Link inverse = link.getInverse();
        for (CompositionLink matched : getLinks()) {
            if (matched.equals(link)) {
                return matched;
            }
            if (matched.equals(inverse)) {
                return matched.getInverse();
            }
        }
        return null;
    }

    private CompositionLink createLink(final Class<?> clazz, final Field field) {
        if (isDomainClass(field.getType())) {
            return new CompositionLink(clazz, field.getName(), false, getKnownClass(field.getType().getCanonicalName()), null, false);
        }
        if (isCollection(field)) {
            Type type = getDomainClassFromCollection(field);
            if (type != null) {
                return new CompositionLink(clazz, field.getName(), true, getKnownClass(stripClassHeader(type.toString())), null, false);
            }
        }
        return null;
    }

    private Class getKnownClass(String canonicalName) {
        try {
            return Class.forName(canonicalName, false,
                this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            logger.warn("ClassNotFoundException thrown although it never should be. Problematic class: " + canonicalName, e);
            throw new IllegalStateException();
        }
    }

    public List<CompositionLink> getLinks() {
        return links;
    }

    private Type getDomainClassFromCollection(final Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            for (Type t : pt.getActualTypeArguments()) {
                if (isDomainClass(t.toString())) {
                    return t;
                }
            }
        }
        return null;
    }

    private boolean isCollection(final Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }
}
