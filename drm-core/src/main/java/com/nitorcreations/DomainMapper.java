package com.nitorcreations;

import com.nitorcreations.domain.Edge;
import com.nitorcreations.scanners.FieldScanner;
import com.nitorcreations.scanners.HierarchyScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DomainMapper {
    static final Logger log = LoggerFactory.getLogger(DomainMapper.class);
    public static final String DOMAIN_DECLARATION = "digraph domain {\n";
    public static final String DEFAULTS = "  edge [ fontsize = 11 ];\n  node [ shape=box style=rounded ];";
    private static final String INHERITANCE_STYLE = "arrowhead=empty color=slategray";
    private final List<Class<?>> classes;
    private final FieldScanner fieldScanner;
    private final HierarchyScanner hierarchyScanner;

    DomainMapper(final List<Class<?>> classes) throws ClassNotFoundException {
        this.classes = classes;
        fieldScanner = new FieldScanner(classes);
        hierarchyScanner = new HierarchyScanner(classes);
    }

    private String describe(Edge edge) {
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

    public String describeDomain() throws ClassNotFoundException {
        return DOMAIN_DECLARATION + DEFAULTS + "\n" + describePackages() + describeCompositions() + describeInheritance() + "}";
    }

    private String describeInheritance() {
        StringBuilder sb = new StringBuilder();
        for (Edge hierarchyEdge : hierarchyScanner.getEdges()) {
            sb.append(String.format("  %s -> %s [%s];\n", hierarchyEdge.source.className, hierarchyEdge.target.className, INHERITANCE_STYLE));
        }
        return sb.toString();
    }

    private String describePackages() {
        Map<String, List<Class<?>>> map = new LinkedHashMap<>();
        for (Class<?> clazz : getClasses()) {
            String packageName = clazz.getPackage().getName();
            if (map.containsKey(packageName)) {
                map.get(packageName).add(clazz);
            } else {
                List<Class<?>> list = new ArrayList<>();
                list.add(clazz);
                map.put(packageName, list);
            }
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Entry<String, List<Class<?>>> entry : map.entrySet()) {
            sb.append("  subgraph cluster_").append(count).append(" {\n");
            sb.append("    label = \"").append(entry.getKey()).append("\";\n");
            for (Class<?> clazz : entry.getValue()) {
                sb.append("    ").append(clazz.getSimpleName()).append("\n");
            }
            sb.append("  }\n");
            count++;
        }
        return sb.toString();
    }

    private String describeCompositions() {
        StringBuilder sb = new StringBuilder();
        for (Edge fieldEdge : fieldScanner.getEdges()) {
            sb.append("  ").append(describe(fieldEdge)).append("\n");
        }
        return sb.toString();
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
