package com.iluwatar;

import com.iluwatar.domain.DomainClass;
import com.iluwatar.domain.Edge;
import com.iluwatar.presenters.Presenter;
import com.iluwatar.presenters.Representation;
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
    private final Presenter presenter;

    DomainMapper(Presenter presenter, final List<Class<?>> classes) {
        this.presenter = presenter;
        this.classes = classes;
        fieldScanner = new FieldScanner(classes);
        hierarchyScanner = new HierarchyScanner(classes);
    }

    public Representation describeDomain() throws ClassNotFoundException {
        List<Edge> edges = new ArrayList<>();
        edges.addAll(fieldScanner.getEdges());
        edges.addAll(hierarchyScanner.getEdges());
        List<DomainClass> domainObjects = classes.stream().map(DomainClass::new).collect(Collectors.toList());
        return presenter.describe(domainObjects, edges);
    }

    public List<Class<?>> getClasses() {
        return classes;
    }

    public static DomainMapper create(Presenter presenter, List<String> packages, List<String> ignores,
                                      URLClassLoader classLoader) throws ClassNotFoundException {
        List<Class<?>> allClasses = DomainClassFinder.findClasses(packages, ignores, classLoader);
        log.debug("Found " + allClasses.size() + " classes.");
        allClasses.stream().forEach(clazz -> log.debug(clazz.getName()));
        return new DomainMapper(presenter, allClasses);
    }

    public static DomainMapper create(Presenter presenter, final List<String> packages, List<String> ignores)
            throws ClassNotFoundException {
        return create(presenter, packages, ignores, null);
    }

    public static DomainMapper create(Presenter presenter, final List<String> packages) throws ClassNotFoundException {
        return create(presenter, packages, new ArrayList<>(), null);
    }
}
