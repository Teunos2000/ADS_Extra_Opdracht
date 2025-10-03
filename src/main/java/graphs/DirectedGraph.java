package graphs;

import java.util.*;
import java.util.stream.Collectors;

public class DirectedGraph<V extends Identifiable, E> {

    private final Map<String, V> vertices = new HashMap<>();
    private final Map<V, Map<V, E>> edges = new HashMap<>();

    /**
     * representation invariants:
     * 1.  the vertices map stores all vertices by their identifying id (which prevents duplicates)
     * 2.  the edges map stores all directed outgoing edges by their from-vertex and then in the nested map by their to-vertex
     * 3.  there can only be two directed edges between any two given vertices v1 and v2:
     * one from v1 to v2 in edges.get(v1).get(v2)
     * one from v2 to v1 in edges.get(v2).get(v1)
     * 4.  every vertex instance in the key-sets of edges shall also occur in the vertices map and visa versa
     **/

    public DirectedGraph() {
    }

    public Collection<V> getVertices() {
        return vertices.values();
    }

    /**
     * finds the vertex in the graph identified by the given id
     *
     * @param id
     * @return the vertex that matches the given id
     * null if none of the vertices matches the id
     */
    public V getVertexById(String id) {
        return vertices.get(id);
    }

    /**
     * Adds newVertex to the graph, if not yet present and in a way that maintains the representation invariants.
     * If a duplicate of newVertex (with the same id) already exists in the graph,
     * nothing will be added, and the existing duplicate will be kept and returned.
     *
     * @param newVertex
     * @return the duplicate of newVertex with the same id that already exists in the graph,
     * or newVertex itself if it has been added.
     */
    public V addOrGetVertex(V newVertex) {
        // TODO add and return the newVertex, or return the existing duplicate vertex with the same Id
        //  pay attention to sustaining representation invariant items 1. and 4.


        // a proper vertex shall be returned at all times
        return null;
    }

    /**
     * retrieves the collection of neighbour vertices that can be reached directly
     * via an out-going directed edge from 'fromVertex'
     *
     * @param fromVertex
     * @return null if fromVertex cannot be found in the graph
     * an empty collection if fromVertex has no neighbours
     */
    public Collection<V> getNeighbours(V fromVertex) {
        if (fromVertex == null) return null;

        // TODO retrieve the collection of neighbour vertices of fromVertex out of the edges data structure

        return null;
    }

    public Collection<V> getNeighbours(String fromVertexId) {
        return getNeighbours(getVertexById(fromVertexId));
    }

    /**
     * Adds a new, directed edge 'newEdge' from vertex 'fromVertex' to vertex 'toVertex'
     * Adds fromVertex or toVertex to the graph first if these don't exist yet
     * No change shall be made if a directed edge already exists between these vertices
     *
     * @param fromVertex the start vertex of the directed edge
     * @param toVertex   the target vertex of the directed edge
     * @param newEdge    the instance with edge information
     * @return whether the edge has been added successfully
     */
    public boolean addEdge(V fromVertex, V toVertex, E newEdge) {
        // TODO add (directed) newEdge to the graph between fromVertex and toVertex

        return false;
    }

    /**
     * Adds a new, directed edge 'newEdge' from vertex with id=fromId to vertex with id=toId
     * No change shall be made if a directed edge already exists between these vertices
     * or if no vertices can be found with id=fromId or id=toId
     *
     * @param fromId  the id of the start vertex of the outgoing edge
     * @param toId    the id of the target vertex of the directed edge
     * @param newEdge the instance with edge information
     * @return whether the edge has been added successfully
     */
    public boolean addEdge(String fromId, String toId, E newEdge) {
        // TODO add (directed) newEdge to the graph between fromId and toId

        return false;
    }

    /**
     * retrieves the collection of edges
     * which connects the 'fromVertex' with its neighbours
     * (only the out-going edges directed from 'fromVertex' towards a neighbour shall be included
     *
     * @param fromVertex
     * @return null if fromVertex cannot be found in the graph
     * an empty collection if fromVertex has no out-going edges
     */
    public Collection<E> getEdges(V fromVertex) {
        if (fromVertex == null) return null;

        // TODO retrieve the collection of out-going edges which connect fromVertex with a neighbour in the edges data structure

        return null;
    }

    public Collection<E> getEdges(String fromId) {
        return getEdges(getVertexById(fromId));
    }

    /**
     * retrieves the directed edge between 'fromVertex' and 'toVertex' from the graph, if any
     *
     * @param fromVertex the start vertex of the designated edge
     * @param toVertex   the end vertex of the designated edge
     * @return the designated directed edge that has been registered in the graph
     * returns null if no connection has been set up between these vertices in the specified direction
     */
    public E getEdge(V fromVertex, V toVertex) {
        if (fromVertex == null || toVertex == null) return null;
        // TODO retrieve the directed edge between vertices fromVertex and toVertex from the graph

        return null;
    }

    public E getEdge(String fromId, String toId) {
        return getEdge(vertices.get(fromId), vertices.get(toId));
    }


    /**
     * Adds two directed edges: one from v1 to v2 and one from v2 to v1
     * both with the same edge information
     *
     * @param v1
     * @param v2
     * @param newEdge
     * @return whether both edges have been added
     */
    public boolean addConnection(V v1, V v2, E newEdge) {
        return addEdge(v1, v2, newEdge) && addEdge(v2, v1, newEdge);
    }

    /**
     * Adds two directed edges: one from id1 to id2 and one from id2 to id1
     * both with the same edge information
     *
     * @param id1
     * @param id2
     * @param newEdge
     * @return whether both edges have been added
     */
    public boolean addConnection(String id1, String id2, E newEdge) {
        return addEdge(id1, id2, newEdge) && addEdge(id2, id1, newEdge);
    }

    /**
     * @return the total number of vertices in the graph
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * calculates and returns the total number of directed edges in the graph data structure
     *
     * @return the total number of edges in the graph
     */
    public int getNumEdges() {
        // TODO calculate and return the total number of directed edges in the graph

        return 0;
    }

    /**
     * Remove vertices without any connection from the graph
     */
    protected void removeUnconnectedVertices() {
        edges.entrySet().removeIf(e -> e.getValue().isEmpty());
        vertices.entrySet().removeIf(e -> !edges.containsKey(e.getValue()));
    }


    @Override
    public String toString() {
        return getVertices().stream()
                .map(v -> v.toString() + ": " +
                        edges.get(v).entrySet().stream()
                                .map(e -> e.getKey().toString() + "(" + e.getValue().toString() + ")")
                                .collect(Collectors.joining(",", "[", "]"))
                )
                .collect(Collectors.joining(",\n  ", "{ ", "\n}"));
    }
}
