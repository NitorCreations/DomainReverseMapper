package com.nitorcreations;

import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;

import java.util.List;
import java.util.Map;

import static com.nitorcreations.domain.Direction.BI_DIRECTIONAL;
import static com.nitorcreations.domain.Direction.UNI_DIRECTIONAL;
import static com.nitorcreations.domain.EdgeType.resolveEdgeType;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class EdgeResolver {

    public static Edge createEdge(Class<?> sourceClass, Class<?> field, EdgeType type, String name) {
        DomainObject source = new DomainObject(sourceClass, name);
        DomainObject target = new DomainObject(field);
        return new Edge(source, target, type, UNI_DIRECTIONAL);
    }

    public static List<Edge> mergeBiDirectionals(List<Edge> edges) {
        Map<?, List<Edge>> groupedEdges = edges.stream()
                .collect(groupingBy(EdgeResolver::sameSourceAndTarget));
        return groupedEdges
                .values().stream()
                .map(EdgeResolver::mergeEdges)
                .collect(toList());
    }

    private static UnorderedTuple<?, ?> sameSourceAndTarget(Edge edge) {
        String sourceId = edge.source.packageName + "." + edge.source.className;
        String targetId = edge.target.packageName + "." + edge.target.className;
        return UnorderedTuple.of(sourceId, targetId);
    }

    private static Edge mergeEdges(List<Edge> edges) {
        if (edges.size() == 1) {
            return edges.get(0);
        } else if (edges.size() == 2) {
            Edge source = edges.get(0);
            Edge target = edges.get(1);
            return new Edge(source.source, target.source, resolveEdgeType(source.type, target.type), BI_DIRECTIONAL);
        } else {
            //something very exotic?
            return null;
        }
    }

    private static class UnorderedTuple<X, Y> {
        private X left;
        private Y right;

        public UnorderedTuple(X left, Y right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int hashCode() {
            return left.hashCode() + right.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj instanceof UnorderedTuple) {
                UnorderedTuple<?,?> tuple = (UnorderedTuple) obj;
                return this.left.equals(tuple.left) && this.right.equals(tuple.right) ||
                        this.left.equals(tuple.right) && this.right.equals(tuple.left);
            } else {
                return false;
            }
        }

        public static <X, Y> UnorderedTuple<X, Y> of(X source, Y target) {
            return new UnorderedTuple<>(source, target);
        }
    }
}
