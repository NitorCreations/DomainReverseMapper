package com.nitorcreations.mappers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.nitorcreations.CompositionLink;
import com.nitorcreations.Link;

public class CompositionMapper extends AbstractMapper {
    final List<CompositionLink> links = new LinkedList<CompositionLink>();

    public CompositionMapper(final List<Class<?>> classes) throws ClassNotFoundException {
        super(classes);
        gatherLinks();
    }

    public void gatherLinks() throws ClassNotFoundException {
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                CompositionLink link = createLink(clazz, field);
                if (link != null) {
                    CompositionLink match = findMatch(link);
                    if (match == null) {
                        getLinks().add(link);
                    } else {
                        removeMatch(link);
                        getLinks().add(new CompositionLink(link.getA(), link.getAtoBname(), link.isCollectionA(), match.getB(), match.getBtoAname(), match.isCollectionB()));
                    }
                }
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

    private CompositionLink createLink(final Class<?> clazz, final Field field) throws ClassNotFoundException {
        if (isDomainClass(field.getType())) {
            return new CompositionLink(clazz, field.getName(), false, Class.forName(field.getType().getCanonicalName()), null, false);
        }
        if (isCollection(field)) {
            Type type = getDomainClassFromCollection(field);
            if (type != null) {
                return new CompositionLink(clazz, field.getName(), true, Class.forName(stripClassHeader(type.toString())), null, false);
            }
        }
        return null;
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
