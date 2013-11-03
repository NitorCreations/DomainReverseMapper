package com.nitorcreations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nitorcreations.mappers.CompositionMapper;
import com.nitorcreations.mappers.InheritanceMapper;

public class DomainMapper {
    final Logger logger = LoggerFactory.getLogger(DomainMapper.class);
    public static final String DOMAIN_DECLARATION = "digraph domain {\n";
    public static final String DEFAULTS = "  edge [ fontsize = 11 ];\n  node [ shape=box style=rounded ];";
    private static final String INHERITANCE_STYLE = "arrowhead=empty color=slategray";
    private final List<Class<?>> classes;
    private final CompositionMapper compositionMapper;
    private final InheritanceMapper inheritanceMapper;

    public DomainMapper(final List<Class<?>> classes) throws ClassNotFoundException {
        this.classes = classes;
        compositionMapper = new CompositionMapper(classes);
        inheritanceMapper = new InheritanceMapper(classes);
    }

    private String describeLink(final CompositionLink link) {
        return String.format("%s -> %s [%s];", link.getA().getSimpleName(), link.getB().getSimpleName(), getEdgeDescription(link));
    }

    private Object getEdgeDescription(final CompositionLink link) {
        StringBuilder sb = new StringBuilder();
        if (link.getBtoAname() != null) {
            sb.append(" headlabel = \"").append(link.getBtoAname()).append("\"");
        }
        if (link.getAtoBname() != null) {
            sb.append(" taillabel = \"").append(link.getAtoBname()).append("\"");
        }
        sb.append(" ").append(linkDirection(link));
        return sb.toString();
    }

    private String linkDirection(final CompositionLink link) {
        if (link.getAtoBname() == null) {
            return "dir=back arrowtail=open";
        }
        if (link.getBtoAname() == null) {
            return "dir=forward arrowhead=open";
        }
        return "dir=both arrowhead=open arrowtail=open";
    }

    public String describeDomain() throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder(DOMAIN_DECLARATION);
        sb.append(DEFAULTS).append("\n");
        sb.append(describePackages());
        sb.append(describeCompositions());
        sb.append(describeInheritance());
        sb.append("}");
        return sb.toString();
    }

    private String describeInheritance() {
        StringBuilder sb = new StringBuilder();
        for (Link link : inheritanceMapper.getLinks()) {
            sb.append(String.format("  %s -> %s [%s];\n", link.getA().getSimpleName(), link.getB().getSimpleName(), INHERITANCE_STYLE));
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
                List<Class<?>> list = new ArrayList<Class<?>>();
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
        for (CompositionLink link : compositionMapper.getLinks()) {
            sb.append("  ").append(describeLink(link)).append("\n");
        }
        return sb.toString();
    }

    public List<Class<?>> getClasses() {
        return classes;
    }
}
