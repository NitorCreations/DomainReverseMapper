package com.nitorcreations.scanners;

import java.util.ArrayList;
import java.util.List;

import com.nitorcreations.domain.Link;

public class HierarchyScanner extends AbstractScanner {
    private final List<Link> links = new ArrayList<>();

    public HierarchyScanner(final List<Class<?>> classes) {
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
