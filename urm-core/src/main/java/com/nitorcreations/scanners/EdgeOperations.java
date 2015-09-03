package com.nitorcreations.scanners;

import com.google.common.collect.Lists;
import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;

import java.util.Collection;
import java.util.List;

import static com.nitorcreations.domain.Direction.BI_DIRECTIONAL;
import static com.nitorcreations.domain.Direction.UNI_DIRECTIONAL;
import static com.nitorcreations.domain.EdgeType.resolveEdgeType;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class EdgeOperations {

    public static Edge createEdge(Class<?> sourceClass, Class<?> field, EdgeType type, String name) {
        DomainObject source = new DomainObject(sourceClass, name);
        DomainObject target = new DomainObject(field);
        return new Edge(source, target, type, UNI_DIRECTIONAL);
    }

    public static List<Edge> mergeBiDirectionals(List<Edge> edges) {
        Collection<List<Edge>> groupedEdges = groupEdges(edges);
        List<Edge> uniDirectionals = takeSingleItemsGroups(groupedEdges);
        List<Edge> biDirectionals = mergeNonSingleGroups(groupedEdges);
        List<Edge> mergedEdges = Lists.newArrayList();
        mergedEdges.addAll(uniDirectionals);
        mergedEdges.addAll(biDirectionals);
        return mergedEdges;
    }

    private static List<Edge> takeSingleItemsGroups(Collection<List<Edge>> groupedEdges) {
        return groupedEdges.stream()
                .filter(edgeGroup -> edgeGroup.size() == 1)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private static List<Edge> mergeNonSingleGroups(Collection<List<Edge>> groupedEdges) {
        List<List<List<Edge>>> edgeGroups = groupedEdges.stream()
                .filter(edgeGroup -> edgeGroup.size() > 1)
                .map(EdgeOperations::groupBySource)
                .collect(toList());
        List<Edge> multiReferenceUniDirectionals = edgeGroups.stream()
                .filter(sourceGroups -> sourceGroups.size() == 1)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .collect(toList());
        List<Edge> biDirectionals = edgeGroups.stream()
                .filter(sourceGroups -> sourceGroups.size() == 2)
                .map(EdgeOperations.Tuple::createPairs)
                .flatMap(Collection::stream)
                .map(EdgeOperations::mergeEdges)
                .collect(toList());
        List<Edge> newEdges = Lists.newArrayList();
        newEdges.addAll(multiReferenceUniDirectionals);
        newEdges.addAll(biDirectionals);
        return newEdges;
    }

    private static Collection<List<Edge>> groupEdges(List<Edge> edges) {
        return edges.stream()
                .collect(groupingBy(EdgeOperations::sameSourceAndTarget))
                .values();
    }

    private static List<List<Edge>> groupBySource(List<Edge> edges) {
        return Lists.newArrayList(edges.stream()
                .collect(groupingBy(edge -> edge.source.className))
                .values());
    }

    private static Edge mergeEdges(Tuple<Edge, Edge> edgePair) {
        Edge source = edgePair.left;
        Edge target = edgePair.right;
        return new Edge(source.source, target.source, resolveEdgeType(source.type, target.type), BI_DIRECTIONAL);
    }

    private static UnorderedTuple<?, ?> sameSourceAndTarget(Edge edge) {
        String sourceId = edge.source.packageName + "." + edge.source.className;
        String targetId = edge.target.packageName + "." + edge.target.className;
        return UnorderedTuple.of(sourceId, targetId);
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
                UnorderedTuple<?, ?> tuple = (UnorderedTuple) obj;
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

    private static class Tuple<X, Y> {
        protected final X left;
        protected final Y right;

        public Tuple(X left, Y right) {
            this.left = left;
            this.right = right;
        }

        public static <T> List<Tuple<T, T>> createPairs(List<List<T>> listOfTwoGroups) {
            List<T> a = listOfTwoGroups.get(0);
            List<T> b = listOfTwoGroups.get(1);
            return makePairs(a, b);
        }

        private static <T> List<Tuple<T, T>> makePairs(List<T> a, List<T> b) {
            List<Tuple<T, T>> pairs = Lists.newArrayList();
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
    }
}
