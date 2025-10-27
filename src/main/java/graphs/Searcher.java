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
        path.getVisited().add(start);

        // easy target
        if (start.equals(target)) {
            path.getVertices().add(target);
            return path;
        }

        path.getVertices().add(start);

        //Recursive helper method
        if(dfsRecursive(graph, start, target, path)) {
            return path;
        }
        return null;
    }

    /**
     * Recursive helper method for depth first search
     * @return true if target is found, false otherwise
     */
    private static<V extends Identifiable, E> boolean dfsRecursive(DirectedGraph<V, E> graph, V current, V target, DGPath<V> path) {
        // Target found
        if (current.equals(target)) {
            return true;
        }

        Collection<V> neighbors = graph.getNeighbours(current);
            if(neighbors != null ) {
                for (V neighbor : neighbors) {
                    if(!path.getVisited().contains(neighbor)) {
                        path.getVisited().add(neighbor);
                        path.getVertices().add(neighbor);

                        // Recursively search from this neighbor.
                        if (dfsRecursive(graph, neighbor, target, path)) {
                            return true;  // Target found in this path
                        }

                            path.getVertices().removeLast();
                    }
                }
            }
      return false; //If we did not reach the target
    }


    /**
     * Uses a breadth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.
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

        Queue<V> queue = new LinkedList<>();

        // Track visited vertices to avoid cycles
        Set<V> visited = new HashSet<>();

        // Track the parent of each vertex to reconstruct the path
        Map<V, V> parentMap = new HashMap<>();

        // Initialize: start vertex has no parent and is visited
        queue.offer(start);
        visited.add(start);
        parentMap.put(start, null);

        while (!queue.isEmpty()) {
            V current = queue.poll();

            // Add to path.visited for statistics
            path.getVisited().add(current);

            // Check if we've reached the target
            if (current.equals(target)) {
                // Found the target! Reconstruct the path using the parent map
                reconstructPath(path, parentMap, start, target);
                return path;
            }

            // Get all neighbors of the current vertex
            Collection<V> neighbors = graph.getNeighbours(current);

            // Process all unvisited neighbors
            if (neighbors != null) {
                for (V neighbor : neighbors) {
                    // Only process unvisited neighbors to avoid cycles
                    if (!visited.contains(neighbor)) {
                        // Mark neighbor as visited
                        visited.add(neighbor);

                        // Record parent for path reconstruction
                        parentMap.put(neighbor, current);

                        // Add neighbor to queue for future processing
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // No path found / target is not reachable from start
        return null;
    }

    /**
     * Helper method to reconstruct the path from start to target using the parent map
     * @param path the path object to populate with vertices
     * @param parentMap map containing parent relationships
     * @param start the start vertex
     * @param target the target vertex (where we reconstruct from)
     */
    private static <V extends Identifiable> void reconstructPath(DGPath<V> path, Map<V, V> parentMap, V start, V target) {
        // Build path backwards from target to start using parent references
        LinkedList<V> reversePath = new LinkedList<>();

        V current = target;
        // Follow parent references until we reach the start (which has null parent)
        while (current != null) {
            reversePath.addFirst(current);  // Add to front to reverse the order
            current = parentMap.get(current);  // Move to parent
        }

        // Add all vertices to the path in correct order (from start to target)
        for (V vertex : reversePath) {
            path.getVertices().add(vertex);
        }
    }

        // helper class to represent a node in Dijkstra's shortest path.
    private static class DSPNode<V> implements Comparable<DSPNode<V>> {
        protected V vertex;                // the graph vertex that is concerned with this DSPNode
        protected V fromVertex = null;     // the parent's node vertex that has an edge towards this node's vertex
        protected boolean marked = false;  // indicates DSP processing has been marked complete for this vertex
        protected double weightSumTo = Double.MAX_VALUE;   // sum of weights of current shortest path to this node's vertex

        private DSPNode(V vertex) {
            this.vertex = vertex;
        }

        @Override
        public int compareTo(DSPNode dspv) {
            return Double.compare(weightSumTo, dspv.weightSumTo);
        }
    }

    /**
     * Calculates the edge-weighted shortest path from start to target
     * according to Dijkstra's algorithm of a minimum spanning tree
     * @param startId      id of the start vertex of the search
     * @param targetId     id of the target vertex of the search
     * @param weightMapper provides a function, by which the weight of an edge can be retrieved or calculated
     * @return the shortest DGPath from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public static <V extends Identifiable, E> DGPath<V> dijkstraShortestPath(DirectedGraph<V, E> graph, String startId, String targetId, Function<E, Double> weightMapper) { //Function parameter is just so we can have the input of E and output of Double to represent the weight
        V start = graph.getVertexById(startId);
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        DGPath<V> path = new DGPath<>();
        path.visited.add(start);

        if (start.equals(target)) {
            path.vertices.add(start);
            return path;
        }

        Map<V, DSPNode<V>> dspProgress = new HashMap<>();

        PriorityQueue<DSPNode<V>> unvisitedQueue = new PriorityQueue<>(
                Comparator.comparingDouble(a -> a.weightSumTo)
        );

        DSPNode<V> startNode = new DSPNode<>(start);
        startNode.weightSumTo = 0.0;
        dspProgress.put(start, startNode);
        unvisitedQueue.offer(startNode);


        while (!unvisitedQueue.isEmpty()) {
            DSPNode<V> currentNode = unvisitedQueue.poll();

            if (currentNode.marked) {
                continue;
            }

            currentNode.marked = true;
            V currentVertex = currentNode.vertex;

            path.visited.add(currentVertex);

            if(currentVertex.equals(target)) {
                path.totalWeight = currentNode.weightSumTo;
                LinkedList<V> reversePath = new LinkedList<>();
                DSPNode<V> node = currentNode;

                while (node != null) {
                    reversePath.addFirst(node.vertex);
                    if (node.fromVertex != null) {
                        node = dspProgress.get(node.fromVertex);
                    } else {
                        node = null;
                    }
                }

                for (V vertex : reversePath) {
                    path.vertices.add(vertex);
                }

                return path;
            }

            Collection<V> neighbors = graph.getNeighbours(currentVertex);
            if (neighbors != null) {
                for (V neighbor : neighbors) {
                    E edge = graph.getEdge(currentVertex, neighbor);
                    if (edge == null) continue;

                    double edgeWeight = weightMapper.apply(edge);
                    double newDistance = currentNode.weightSumTo + edgeWeight;

                    DSPNode<V> neighborNode = dspProgress.get(neighbor);
                    if (neighborNode == null) {
                        neighborNode = new DSPNode<>(neighbor);
                        dspProgress.put(neighbor, neighborNode);
                        path.visited.add(neighbor); //Added this line to match assignment console output
                    }

                    if (!neighborNode.marked && newDistance < neighborNode.weightSumTo) {
                        neighborNode.weightSumTo = newDistance;
                        neighborNode.fromVertex = currentVertex;

                        unvisitedQueue.offer(neighborNode);
                    }
                }
            }
        }
        return null;
    }//End of dijkstras function
}//End of searcher class
