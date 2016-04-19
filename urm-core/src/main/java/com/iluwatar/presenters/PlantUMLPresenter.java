package com.iluwatar.presenters;

import com.iluwatar.domain.DomainClass;
import com.iluwatar.domain.Edge;
import com.iluwatar.domain.EdgeType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

/**
 * Created by moe on 06.04.16.
 *
 * More info about syntax: http://de.plantuml.com/classes.html
 */
public class PlantUMLPresenter implements Presenter {

    public static final String FILE_PREAMBLE = "@startuml";
    public static final String FILE_POSTAMBLE = "@enduml";

    private final AtomicInteger count = new AtomicInteger();

    private Object getEdgeDescription(Edge edge) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(linkDirection(edge));
        return sb.toString();
    }

    private String linkDirection(Edge edge) {
        if (edge.source.getDescription() == null) {
            return "--o";
        }
        if (edge.target.getDescription() == null) {
            return "o--";
        }
        //return "dir=both arrowhead=none arrowtail=none color=slategray";
        return "--";
    }

    private String describeInheritance(List<Edge> edges) {
        return edges.stream()
                .filter(e -> e.type == EdgeType.EXTENDS)
                .map(this::describeInheritance)
                .collect(joining());
    }

    private String describeInheritance(Edge hierarchyEdge) {
        return String.format("  %s --|> %s \n",
                hierarchyEdge.source.getClassName(),
                hierarchyEdge.target.getClassName());
    }

    private String describePackages(List<DomainClass> domainClasss) {
        count.set(0);
        return domainClasss.stream()
                .collect(groupingBy(DomainClass::getPackageName))
                .entrySet().stream()
                .map(this::describePackage)
                .collect(joining());
    }

    private String describePackage(Map.Entry<String, List<DomainClass>> entry) {
        return String.format("  package %s {\n%s  }\n",
                entry.getKey(),
                listDomainClasss(entry.getValue()));
    }

    private String listDomainClasss(List<DomainClass> domainClasss) {
        return domainClasss.stream()
                .map(domainClass -> describeDomainClass(domainClass))
                .distinct()
                .collect(joining());
    }

    private String describeDomainClass(DomainClass domainClass){
        return String.format("    class %s {\n      %s\n      %s\n    }\n",
                domainClass.getClassName(),
                describeDomainClassFields(domainClass),
                describeDomainClassMethods(domainClass));
    }

    private String describeDomainClassFields(DomainClass domainClass) {
        return domainClass.getFields().stream().map((f) -> f.getVisibility() + " " + f.getUmlName()
                + (f.isStatic() ? " {static}" : "") + (f.isAbstract() ? " {abstract}" : ""))
                .collect(Collectors.joining("\n      "));
    }

    private String describeDomainClassMethods(DomainClass domainClass) {
        return domainClass.getMethods().stream().map((m) -> m.getVisibility() + " " + m.getUmlName()
                + (m.isStatic() ? " {static}" : "") + (m.isAbstract() ? " {abstract}" : ""))
                .collect(Collectors.joining("\n      "));
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
        return String.format("%s %s %s",
                edge.source.getClassName(), getEdgeDescription(edge), edge.target.getClassName());
    }

    @Override
    public Representation describe(List<DomainClass> domainClasss, List<Edge> edges) {
        String content = FILE_PREAMBLE + "\n"
                + describePackages(domainClasss)
                + describeCompositions(edges)
                + describeInheritance(edges)
                + "\n" + FILE_POSTAMBLE;
        return new Representation(content, "puml");
    }
}
