package com.nitorcreations.presenters;

import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class DefaultGraphvizPresenter implements Presenter {

    public static final String DOMAIN_DECLARATION = "digraph domain {\n";
    public static final String DEFAULTS = "  edge [ fontsize = 11 ];\n  node [ shape=box style=rounded ];";
    private static final String INHERITANCE_STYLE = "arrowhead=empty color=slategray";
    private final AtomicInteger count = new AtomicInteger();

    private String describeEdge(Edge edge) {
        return String.format("%s -> %s [%s];", edge.source.className, edge.target.className, getEdgeDescription(edge));
    }

    private Object getEdgeDescription(Edge edge) {
        StringBuilder sb = new StringBuilder();
        if (edge.target.description != null) {
            sb.append(" headlabel = \"").append(edge.target.description).append("\"");
        }
        if (edge.source.description != null) {
            sb.append(" taillabel = \"").append(edge.source.description).append("\"");
        }
        sb.append(" ").append(linkDirection(edge));
        return sb.toString();
    }

    private String linkDirection(Edge edge) {
        if (edge.source.description == null) {
            return "dir=back arrowtail=open";
        }
        if (edge.target.description == null) {
            return "dir=forward arrowhead=open";
        }
        return "dir=both arrowhead=open arrowtail=open";
    }


    private String describeInheritance(List<Edge> edges) {
        return edges.stream().filter(e -> e.type == EdgeType.EXTENDS).map(hierarchyEdge -> String.format("  %s -> %s [%s];\n", hierarchyEdge.source.className, hierarchyEdge.target.className, INHERITANCE_STYLE)).collect(joining());
    }

    private String describePackages(List<DomainObject> domainObjects) {
        count.set(0);
        return domainObjects.stream().collect(groupingBy(DomainObject::getPackageName)).entrySet().stream().map(this::describePackage).collect(joining());

    }

    private String describePackage(Map.Entry<String, List<DomainObject>> entry) {
        return String.format("  subgraph cluster_%s {\n    label = \"%s\";\n%s  }\n",
                count.getAndIncrement(), entry.getKey(), entry.getValue().stream().map(domainObject -> "    " + domainObject.className + "\n").distinct().collect(joining()));
    }

    private String describeCompositions(List<Edge> edges) {
        return edges.stream().filter(e -> e.type != EdgeType.EXTENDS).map(e -> String.format("  %s\n", describeEdge(e))).collect(joining());
    }


    @Override
    public String describe(List<DomainObject> domainObjects, List<Edge> edges) {
        return DOMAIN_DECLARATION + DEFAULTS + "\n" + describePackages(domainObjects) + describeCompositions(edges) + describeInheritance(edges) + "}";
    }
}
