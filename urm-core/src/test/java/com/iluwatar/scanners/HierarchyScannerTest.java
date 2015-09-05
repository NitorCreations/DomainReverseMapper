package com.iluwatar.scanners;

import com.google.common.collect.Lists;
import com.iluwatar.domain.DomainObject;
import com.iluwatar.domain.Edge;
import com.iluwatar.domain.EdgeType;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class HierarchyScannerTest {

    private Edge parentToChild = new Edge(new DomainObject(Child.class),
            new DomainObject(Parent.class), EdgeType.EXTENDS);
    private Edge childToGrandChild = new Edge(new DomainObject(GrandChild.class),
            new DomainObject(Child.class), EdgeType.EXTENDS);

    @Test
    public void createsProperHierarchy() {
        List<Class<?>> hierarchicalClasses = Lists.newArrayList();
        hierarchicalClasses.add(Child.class);
        hierarchicalClasses.add(GrandChild.class);
        hierarchicalClasses.add(Parent.class);
        HierarchyScanner scanner = new HierarchyScanner(hierarchicalClasses);

        List<Edge> edges = scanner.getEdges();
        assertThat(edges, containsInAnyOrder(parentToChild, childToGrandChild));
    }

    @Test
    public void doesNotIncludeNonDomainParents() {
        List<Class<?>> hierarchicalClasses = Lists.newArrayList();
        hierarchicalClasses.add(Child.class);
        hierarchicalClasses.add(GrandChild.class);
        HierarchyScanner scanner = new HierarchyScanner(hierarchicalClasses);

        List<Edge> edges = scanner.getEdges();
        assertThat(edges, contains(childToGrandChild));
    }

    private static class Parent {

    }

    private static class Child extends Parent {

    }

    private static class GrandChild extends Child {

    }
}
