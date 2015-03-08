package com.nitorcreations.presenters;

import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DefaultGraphvizPresenter implements Presenter {

    public static final String DOMAIN_DECLARATION = "digraph domain {\n";
    public static final String DEFAULTS = "  edge [ fontsize = 11 ];\n  node [ shape=box style=rounded ];";
    private static final String INHERITANCE_STYLE = "arrowhead=empty color=slategray";
    private AtomicInteger count = new AtomicInteger();

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
        StringBuilder sb = new StringBuilder();
        for (Edge hierarchyEdge : edges.stream().filter(e -> e.type == EdgeType.EXTENDS).collect(Collectors.toList())) {
            sb.append(String.format("  %s -> %s [%s];\n", hierarchyEdge.source.className, hierarchyEdge.target.className, INHERITANCE_STYLE));
        }
        return sb.toString();
    }

    private String describePackages(List<DomainObject> domainObjects) {
        count.set(0);
        return domainObjects.stream().collect(Collectors.groupingBy(DomainObject::getPackageName)).entrySet().stream().map(this::describePackage).collect(Collectors.joining());

    }

    private String describePackage(Map.Entry<String, List<DomainObject>> entry) {
        return "  subgraph cluster_" + count.getAndIncrement() + " {\n" +
                "    label = \"" + entry.getKey() + "\";\n" +
                entry.getValue().stream().map(domainObject -> "    " + domainObject.className + "\n").distinct().collect(Collectors.joining())
                + "  }\n";
    }

    private String describeCompositions(List<Edge> edges) {
        StringBuilder sb = new StringBuilder();
        for (Edge fieldEdge : edges.stream().filter(e -> e.type != EdgeType.EXTENDS).collect(Collectors.toList())) {
            sb.append("  ").append(describeEdge(fieldEdge)).append("\n");
        }
        return sb.toString();
    }


    @Override
    public String describe(List<DomainObject> domainObjects, List<Edge> edges) {
        return DOMAIN_DECLARATION + DEFAULTS + "\n" + describePackages(domainObjects) + describeCompositions(edges) + describeInheritance(edges) + "}";
    }
}
