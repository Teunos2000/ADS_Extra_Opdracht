package graphs;

import java.util.*;
import java.util.function.Function;

public class Searcher {

    /**
     * represents a path of connected vertices and edges in the graph
     */
    public static class DGPath<V extends Identifiable> {
        private final SinglyLinkedList<V> vertices = new SinglyLinkedList<>(); //Linked list voor alle vertices die afgelegd zijn in de final afgelegde path. "The sequence of vertices that form the final path from start to target."
        private final Set<V> visited = new HashSet<>();  //Dit is een lijst met alle vertices die bekeken zijn, een HashSet voor snelle lookup O(1) en geen duplicates waardoor je niet vertices revist
        private double totalWeight = 0.0; //Het totale gewicht van de route dus als je bijv van A -> B gaat met weight 2 en B -> C met weight 3 dan is de total weight 5
        //Geen lijst met unvisited omdat dat altijd gewoon de totale lijst is minus de visited.

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

    // helper class to represent a node in Dijkstra's shortest path.
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
    public static <V extends Identifiable, E> DGPath<V> dijkstraShortestPath(DirectedGraph<V, E> graph, String startId, String targetId, Function<E, Double> weightMapper) { //Function parameter is just so we can have the input of E and output of Double to represent the weight
        V start = graph.getVertexById(startId); //Dit maakt variabelen van de meegegeven vertices ID's
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath<V> path = new DGPath<>(); //Creates a new DGPath object, this is based on the inner class at the top
        path.visited.add(start); //Adds the start vertice to visited because thats where you start

        // easy target (origin same as destination)
        if (start.equals(target)) {
            path.vertices.add(start); //If the destination is the same as the origin just return the path with the one vertice
            return path;
        }

        // keep track of the DSP status of all visited nodes
        Map<V, DSPNode<V>> dspProgress = new HashMap<>(); //Een Hashmap (box) met als key een Vertice en als value de DSPNode van de helper class. Dit zorgt ervoor dat je alle nodes kunt bijhouden in deze functie. Anders dan de visited lijst helemaal bovenin die puur over het resultaat gaat. Het is eigenlijk een soort notitieboekje die je helpt dingen bij te houden.

        // Priority queue for efficient retrieval of minimum weight node - O(log n) operations
        // Uses a comparator to order nodes by their weightSumTo value
        PriorityQueue<DSPNode<V>> unvisitedQueue = new PriorityQueue<>(
                Comparator.comparingDouble(a -> a.weightSumTo)
        );

        // Initialize the progress of the start node
        DSPNode<V> startNode = new DSPNode<>(start);
        startNode.weightSumTo = 0.0;  // Distance to start is 0
        dspProgress.put(start, startNode); //Start being the key(id) and startnode being the actual value. Dit kan omdat je in het begin zegt V start = blabla.id. Dus het type van start is daardoor V. En op line 158 waar je die map maakt zeg je dat de key van de hashmap V moet zijn. (Doordat het de eerste type is)
        unvisitedQueue.offer(startNode);  // Add to priority queue


        while (!unvisitedQueue.isEmpty()) {
            DSPNode<V> currentNode = unvisitedQueue.poll(); //Gets the unvisited node with minimum weight which at this point is always the start node

            // Skip if already processed (can happen with duplicate entries)
            if (currentNode.marked) {
                continue; //Goes to the next iteration in the while loop
            }

            currentNode.marked = true;
            V currentVertex = currentNode.vertex;

            //Path visited vertices [Dit verduidelijken in rapport dat dit dus dubbel kan omdat het om een HashSet gaat
            path.visited.add(currentVertex); //Dit kan dus nog een keer omdat het een HashSet is waardoor duplicates automatisch voorkomen worden. Kijk naar regel 13, daar zie je dat het om een HashSet gaat

            //If and only if we've reached our destination (target node) this code will be executed. This target node is provided in the parameter of the entire method.
            //Point of this is to save the shortest path once you've reached it.
            if(currentVertex.equals(target)) {
                path.totalWeight = currentNode.weightSumTo; //Save the total distance
                LinkedList<V> reversePath = new LinkedList<>(); //Empty list to store the route
                DSPNode<V> node = currentNode; //Create a new node equals to the current node which at this point will always be the target node

                //Loop over the entire route starting at the target node and
                while (node != null) {
                    reversePath.addFirst(node.vertex); //Voeg die target node van toe aan de linkedlist als eerste item wnat we zijn m aan het reversen
                    if (node.fromVertex != null) { //Als vervolgens de fromVertex van die node hierboven niet null is pak de node die een maak hem gelijk aan node waardoor "node" dus niet meer null is waardoor er nog een keer overheen wordt geloopt
                        node = dspProgress.get(node.fromVertex);
                    } else {
                        node = null;
                    }
                }

                // Add vertices to path in correct order, ff checken hoezo dit dan niet in reverse komt
                for (V vertex : reversePath) {
                    path.vertices.add(vertex);
                }

                return path;
            }//End of the method that saves the path if the destination is reached

            // Explore all neighbors of current vertex
            Collection<V> neighbors = graph.getNeighbours(currentVertex);
            if (neighbors != null) {
                for (V neighbor : neighbors) { //V neighbor is now a new variable for each "neighbor" in the neighbors list. So neighbor is an individual neighbor now (vertex) for each neighboor in the collection. This is an foreach loop
                    // Get the edge from current to neighbor
                    E edge = graph.getEdge(currentVertex, neighbor); //Pakt edge/road van de current vertex naar DE neighbor. Dat is dus 1 neighbor per iteratie
                    if (edge == null) continue; //Als er geen edge is volgende iteratie in deze for each loop

                    // Calculate the weight of this edge
                    double edgeWeight = weightMapper.apply(edge);
                    double newDistance = currentNode.weightSumTo + edgeWeight;

                    //Op dit punt heb je alle info je hebt een collection van neighbors op de current vertex waarbij de neigbor 1 van deze instanties is, je hebt de edge van currentVertex naar deze neighbor en z'n weight
                    DSPNode<V> neighborNode = dspProgress.get(neighbor); //Checkt of huidige neighbor al in de lijst zit, door de neighbor als key meetegeven. Hij zegt dus eigenlijk: Zoek in dspProgress op dit label. Want het gaat dus om de key. Als die er wel inzit wordt neighborNode dus een Vertex en als die er nog niet inzit wordt het null waardoor het blok hieronder wordt uitgevoerd
                    if (neighborNode == null) { //Als die nog niet in de lijst zit wordt het null
                        neighborNode = new DSPNode<>(neighbor);//Maak de neighborNode een DSPNode
                        dspProgress.put(neighbor, neighborNode); //Op de key neighbor voeg neighbornode toe.
                    }

                    // Update neighbor if we found a shorter path and its not marked
                    if (!neighborNode.marked && newDistance < neighborNode.weightSumTo) { //Als neighborNode niet marked is en de huidige distance (binnen deze instantie) minder is dan de weightSum van de Node daarvoor execute de code
                        neighborNode.weightSumTo = newDistance; //Update de weight naar deze node waardoor deze node nu de shortest path heeft
                        neighborNode.fromVertex = currentVertex;

                        // Add to queue (may create duplicates, but we handle that with the marked check)
                        unvisitedQueue.offer(neighborNode);
                    }
                }
            }
        }
        return null;
    }//End of dijkstras function
}//End of searcher class
