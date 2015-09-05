package com.iluwatar;

import com.iluwatar.domain.DomainObject;
import com.iluwatar.domain.Edge;
import com.iluwatar.presenters.DefaultGraphvizPresenter;
import com.iluwatar.presenters.Presenter;
import com.iluwatar.scanners.FieldScanner;
import com.iluwatar.scanners.HierarchyScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DomainMapper {

    private static final Logger log = LoggerFactory.getLogger(DomainMapper.class);
    private final FieldScanner fieldScanner;
    private final HierarchyScanner hierarchyScanner;
    private final List<Class<?>> classes;
    private final Presenter presenter = new DefaultGraphvizPresenter();

    DomainMapper(final List<Class<?>> classes) {
        this.classes = classes;
        fieldScanner = new FieldScanner(classes);
        hierarchyScanner = new HierarchyScanner(classes);
    }

    public String describeDomain() throws ClassNotFoundException {
        List<Edge> edges = new ArrayList<>();
        edges.addAll(fieldScanner.getEdges());
        edges.addAll(hierarchyScanner.getEdges());
        List<DomainObject> domainObjects = classes.stream().map(DomainObject::new).collect(Collectors.toList());
        return presenter.describe(domainObjects, edges);
    }

    public List<Class<?>> getClasses() {
        return classes;
    }

    public static DomainMapper create(List<String> packages, URLClassLoader classLoader) throws ClassNotFoundException {
        List<Class<?>> allClasses = DomainClassFinder.findClasses(packages, classLoader);
        log.debug("Found " + allClasses.size() + " classes.");
        return new DomainMapper(allClasses);
    }

    public static DomainMapper create(final List<String> packages) throws ClassNotFoundException {
        return create(packages, null);
    }
}
