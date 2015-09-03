package com.nitorcreations.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Edge {

    public final DomainObject source;
    public final DomainObject target;
    public final EdgeType type;
    public final Direction direction;

    public Edge(DomainObject source, DomainObject target, EdgeType type, Direction direction) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.direction = direction;
    }

    public Edge(DomainObject source, DomainObject target, EdgeType type) {
        this(source, target, type, null);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
