package com.nitorcreations.domain;

public enum EdgeType {

    ONE_TO_ONE, MANY_TO_ONE, MANY_TO_MANY, ONE_TO_MANY, EXTENDS;

    public static EdgeType resolveEdgeType(EdgeType source, EdgeType target) {
        if (source.equals(target)) {
            switch (source) {
                case ONE_TO_ONE:
                case MANY_TO_MANY:
                    return source;
                case MANY_TO_ONE:
                case ONE_TO_MANY:
                    return MANY_TO_MANY;
                case EXTENDS:
                    throw new RuntimeException("impossible");
            }
        }
        if (source.equals(ONE_TO_ONE)) {
            return target;
        } else if (target.equals(ONE_TO_ONE)) {
            return source;
        }
        throw new RuntimeException("impossible");
    }

}
