package com.nitorcreations.mappers;

import java.util.ArrayList;
import java.util.List;

import com.nitorcreations.domain.Link;

public class InheritanceMapper extends AbstractMapper {
    private final List<Link> links = new ArrayList<>();

    public InheritanceMapper(final List<Class<?>> classes) {
        super(classes);
        gatherLinks();
    }

    private void gatherLinks() {
        for (Class<?> clazz : classes) {
            Class<?> superclass = clazz.getSuperclass();
            if (isDomainClass(superclass)) {
                links.add(new Link(clazz, superclass));
            }
        }
    }

    public List<Link> getLinks() {
        return links;
    }
}
