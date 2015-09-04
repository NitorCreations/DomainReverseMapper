package com.iluwatar.scanners;

import com.iluwatar.domain.DomainObject;
import com.iluwatar.domain.Edge;
import com.iluwatar.domain.EdgeType;

import java.util.ArrayList;
import java.util.List;

public class HierarchyScanner extends AbstractScanner {

    public HierarchyScanner(List<Class<?>> classes) {
        super(classes);
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Class<?> superclass = clazz.getSuperclass();
            if (isDomainClass(superclass)) {
                DomainObject child = new DomainObject(clazz);
                DomainObject parent = new DomainObject(superclass);
                edges.add(new Edge(child, parent, EdgeType.EXTENDS));
            }
        }
        return edges;
    }
}
