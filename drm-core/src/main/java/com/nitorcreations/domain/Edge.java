package com.nitorcreations.domain;

public class Edge {

    final DomainObject source;
    final DomainObject target;
    final EdgeType type;
    final String name;

    public Edge(DomainObject source, DomainObject target, EdgeType type, String name) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.name = name;
    }

}
