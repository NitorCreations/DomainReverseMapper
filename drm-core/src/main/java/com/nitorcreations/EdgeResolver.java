package com.nitorcreations;

import com.google.common.collect.Lists;
import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
                .flatMap(EdgeResolver::mergeEdges)
                .collect(toList());
    }

    private static UnorderedTuple<?, ?> sameSourceAndTarget(Edge edge) {
        String sourceId = edge.source.packageName + "." + edge.source.className;
        String targetId = edge.target.packageName + "." + edge.target.className;
        return UnorderedTuple.of(sourceId, targetId);
    }

    private static Stream<Edge> mergeEdges(List<Edge> edges) {
        if (edges.size() == 1) {
            return edges.stream();
        } else {
            List<Edge> merged = new ArrayList<>();
            List<List<Edge>> edgesWithSameSourceType = Lists.newArrayList(edges.stream()
                    .collect(groupingBy(edge -> edge.source.className))
                    .values());
            if (edgesWithSameSourceType.size() == 1) {
               edgesWithSameSourceType.get(0).forEach(merged::add);
            } else if (edgesWithSameSourceType.size() == 2) {
                List<Edge> a = edgesWithSameSourceType.get(0);
                List<Edge> b = edgesWithSameSourceType.get(1);
                List<Tuple<Edge,Edge>> mergePairs = makePairs(a, b);
                mergePairs.forEach(edgePair -> {
                    Edge source = edgePair.left;
                    Edge target = edgePair.right;
                    Edge edge = new Edge(source.source, target.source, resolveEdgeType(source.type, target.type), BI_DIRECTIONAL);
                    merged.add(edge);
                });
            } else {
                throw new RuntimeException("Inputted Edge list contained more than 2 type of edges");
            }
            return merged.stream();
        }
    }

    /**
     * Order of joined lists must preserve.
     */
    private static <T> List<Tuple<T, T>> makePairs(List<T> a, List<T> b) {
        List<Tuple<T,T>> pairs = Lists.newArrayList();
        if (a.size() > b.size()) {
            for (int i = 0; i < a.size(); i++) {
                pairs.add(new Tuple<>(a.get(i), b.get(i % b.size())));
            }
        } else {
            for (int i = 0; i < b.size(); i++) {
                pairs.add(new Tuple<>(a.get(i % a.size()), b.get(i)));
            }
        }
        return pairs;
    }

    private static class UnorderedTuple<X, Y> extends Tuple<X, Y> {

        public UnorderedTuple(X left, Y right) {
            super(left, right);
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

    private static class Tuple<X,Y> {
        protected final X left;
        protected final Y right;

        public Tuple(X left, Y right) {
            this.left = left;
            this.right = right;
        }
    }
}
