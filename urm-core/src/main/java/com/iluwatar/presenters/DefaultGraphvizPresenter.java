package com.iluwatar.presenters;

import com.iluwatar.domain.DomainObject;
import com.iluwatar.domain.Edge;
import com.iluwatar.domain.EdgeType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class DefaultGraphvizPresenter implements Presenter {

    public static final String DOMAIN_DECLARATION = "digraph domain {\n";
    public static final String DEFAULTS = "  edge [ fontsize = 11 ];\n  node [ shape=record ];";
    private static final String INHERITANCE_STYLE = "arrowhead=empty color=slategray";
    private final AtomicInteger count = new AtomicInteger();

    private Object getEdgeDescription(Edge edge) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(linkDirection(edge));
        return sb.toString();
    }

    private String linkDirection(Edge edge) {
        if (edge.source.description == null) {
            return "dir=forward arrowhead=odiamond color=slategray";
        }
        if (edge.target.description == null) {
            return "dir=back arrowtail=odiamond color=slategray";
        }
        return "dir=both arrowhead=none arrowtail=none color=slategray";
    }

    private String describeInheritance(List<Edge> edges) {
        return edges.stream()
                .filter(e -> e.type == EdgeType.EXTENDS)
                .map(this::describeInheritance)
                .collect(joining());
    }

    private String describeInheritance(Edge hierarchyEdge) {
        return String.format("  %s -> %s [%s];\n",
                hierarchyEdge.source.className,
                hierarchyEdge.target.className,
                INHERITANCE_STYLE);
    }

    private String describePackages(List<DomainObject> domainObjects) {
        count.set(0);
        return domainObjects.stream()
                .collect(groupingBy(DomainObject::getPackageName))
                .entrySet().stream()
                .map(this::describePackage)
                .collect(joining());
    }

    private String describePackage(Map.Entry<String, List<DomainObject>> entry) {
        return String.format("  subgraph cluster_%s {\n    label = \"%s\";\n%s  }\n",
                count.getAndIncrement(),
                entry.getKey(),
                listDomainObjects(entry.getValue()));
    }

    private String listDomainObjects(List<DomainObject> domainObjects) {
        return domainObjects.stream()
                .map(domainObject -> describeDomainObject(domainObject))
                .distinct()
                .collect(joining());
    }

    private String describeDomainObject(DomainObject domainObject){
        return String.format("    %s [ label = \"{%s | %s}\" ] \n", domainObject.className, domainObject.className, describeDomainObjectMethods(domainObject));
    }

    private String describeDomainObjectMethods(DomainObject domainObject) {
        StringBuilder sb = new StringBuilder();
        domainObject.methods.stream().forEach((m) -> sb.append("+ " + m + "\\l"));
        return sb.toString();
    }

    private String describeCompositions(List<Edge> edges) {
        return edges.stream()
                .filter(e -> e.type != EdgeType.EXTENDS)
                .map(this::describeComposition)
                .collect(joining());
    }

    private String describeComposition(Edge compositionEdge) {
        return String.format("  %s\n", describeEdge(compositionEdge));
    }

    private String describeEdge(Edge edge) {
        return String.format("%s -> %s [%s];", edge.source.className, edge.target.className, getEdgeDescription(edge));
    }

    @Override
    public String describe(List<DomainObject> domainObjects, List<Edge> edges) {
        return DOMAIN_DECLARATION + DEFAULTS + "\n"
                + describePackages(domainObjects)
                + describeCompositions(edges)
                + describeInheritance(edges)
                + "}";
    }
}
