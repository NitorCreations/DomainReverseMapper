package com.nitorcreations.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Edge {

    public final DomainObject source;
    public final DomainObject target;
    public final EdgeType type;
    public final String name;

    public Edge(DomainObject source, DomainObject target, EdgeType type, String name) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.name = name;
    }

    public Edge(DomainObject source, DomainObject target, EdgeType type) {
        this(source, target, type, null);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
