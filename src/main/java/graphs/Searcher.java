package graphs;

import java.util.*;
import java.util.function.Function;

public class Searcher {

    /**
     * represents a path of connected vertices and edges in the graph
     */
    public static class DGPath<V extends Identifiable> {
        private final SinglyLinkedList<V> vertices = new SinglyLinkedList<>();
        private final Set<V> visited = new HashSet<>();
        private double totalWeight = 0.0;

        /**
         * representation invariants:
         * 1. vertices contains a sequence of vertices that are connected in the graph by a directed edge,
         * i.e. FOR ALL i: 0 < i < vertices.length: this.getEdge(vertices[i-1],vertices[i]) will provide edge information of the connection
         * 2. a path with one vertex has no edges
         * 3. a path without vertices is empty
         * totalWeight is a helper attribute to capture additional info from searches, not a fundamental property of a path
         * visited is a helper set to be able to track visited vertices in searches, not a fundamental property of a path
         **/

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(
                    String.format("Weight=%f Length=%d visited=%d (",
                            totalWeight, vertices.size(), visited.size()));
            String separator = "";
            for (V v : vertices) {
                sb.append(separator).append(v.getId());
                separator = ", ";
            }
            sb.append(")");
            return sb.toString();
        }

        public SinglyLinkedList<V> getVertices() {
            return vertices;
        }

        public double getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(double totalWeight) {
            this.totalWeight = totalWeight;
        }

        public Set<V> getVisited() {
            return visited;
        }
    }


    /**
     * Uses a depth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     *
     * @param startId
     * @param targetId
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public static <V extends Identifiable, E> DGPath<V> depthFirstSearch(DirectedGraph<V, E> graph, String startId, String targetId) {

        V start = graph.getVertexById(startId);
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        DGPath<V> path = new DGPath<>();

        // TODO calculate the path from start to target by recursive depth-first-search

        return null;
    }


    /**
     * Uses a breadth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.
     *
     * @param startId
     * @param targetId
     * @return the path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public static <V extends Identifiable, E> DGPath<V> breadthFirstSearch(DirectedGraph<V, E> graph, String startId, String targetId) {

        V start = graph.getVertexById(startId);
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath<V> path = new DGPath<>();
        path.getVisited().add(start);

        // easy target
        if (start.equals(target)) {
            path.getVertices().add(target);
            return path;
        }

        // TODO calculate the path from start to target by breadth-first-search

        return null;
    }

    // helper class to build the spanning tree of visited vertices in dijkstra's shortest path algorithm
    // your may change this class or delete it altogether follow a different approach in your implementation
    private static class DSPNode<V> implements Comparable<DSPNode<V>> {
        protected V vertex;                // the graph vertex that is concerned with this DSPNode
        protected V fromVertex = null;     // the parent's node vertex that has an edge towards this node's vertex
        protected boolean marked = false;  // indicates DSP processing has been marked complete for this vertex
        protected double weightSumTo = Double.MAX_VALUE;   // sum of weights of current shortest path to this node's vertex

        private DSPNode(V vertex) {
            this.vertex = vertex;
        }

        // comparable interface helps to find a node with the shortest current path, sofar
        @Override
        public int compareTo(DSPNode dspv) {
            return Double.compare(weightSumTo, dspv.weightSumTo);
        }
    }

    /**
     * Calculates the edge-weighted shortest path from start to target
     * according to Dijkstra's algorithm of a minimum spanning tree
     *
     * @param startId      id of the start vertex of the search
     * @param targetId     id of the target vertex of the search
     * @param weightMapper provides a function, by which the weight of an edge can be retrieved or calculated
     * @return the shortest path from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public static <V extends Identifiable, E> DGPath<V> dijkstraShortestPath(
            DirectedGraph<V, E> graph, String startId, String targetId,
            Function<E, Double> weightMapper) {

        V start = graph.getVertexById(startId);
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath<V> path = new DGPath<>();
        path.visited.add(start);

        // easy target
        if (start.equals(target)) {
            path.vertices.add(start);
            return path;
        }

        // keep track of the DSP status of all visited nodes
        // you may choose a different approach of tracking progress of the algorithm, if you wish
        Map<V, DSPNode<V>> progressData = new HashMap<>();

        // initialise the progress of the start node
        DSPNode<V> nextDspNode = new DSPNode<>(start);
        nextDspNode.weightSumTo = 0.0;
        progressData.put(start, nextDspNode);

        while (nextDspNode != null) {

            // TODO continue Dijkstra's algorithm to process nextDspNode
            //  mark nodes as you complete their processing
            //  register all visited vertices while going for statistical purposes
            //  if you hit the target: complete the path and bail out !!!


            /// TODO find the next nearest node that is not marked yet

        }


        // no path found, graph was not connected ???
        return null;
    }
}
