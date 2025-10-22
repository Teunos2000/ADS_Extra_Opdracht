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
     * Onthoud hierbij: depth = diep, Hij gaat diep zoeken. Dus hij kijkt naar de neighboor van de node waar hij is. En gaat daar op verder dan kijkt hij naar de nieuwe neighboors van die node en gaat daarop verder. Hij gaat elke tak af tot hij niet verder kan en dan gaat hij terug
     */
    public static <V extends Identifiable, E> DGPath<V> depthFirstSearch(DirectedGraph<V, E> graph, String startId, String targetId) {
        V start = graph.getVertexById(startId);
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        DGPath<V> path = new DGPath<>(); //Create a new path to return later on
        path.getVisited().add(start); //Voegt start toe aan de lijst met alle nodes die we gevisit hebben"“the places I’ve already explored so I don’t go back.”"

        // easy target
        if (start.equals(target)) {
            path.getVertices().add(target);
            return path;
        }

        path.getVertices().add(start);  //Voegt start toe aan de soort van de “the current route I’m walking.”

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

        //Een belangrijk deel waar ik mee struggelde is dat stel je komt op vertice C uit die geen neighbors meer heeft dan is onderstaande collection neighbors niet null maar een empty list
        Collection<V> neighbors = graph.getNeighbours(current);//Vraag zou kunnen zijn: waarom een collection ipv een set?
            if(neighbors != null ) { //Als er neighbors zijn. Onthoud het is een directedGraph. Dus als je bijv van B -> C gaat. Betekent niet dat B een neighbor is van C dus stel C is de laatste node op die branch dan
                for (V neighbor : neighbors) { //Loop over neighbors heen
                    if(!path.getVisited().contains(neighbor)) { //Als de neighbor nog niet gevisit, is execute code
                        path.getVisited().add(neighbor); //Voeg neighbor toe aan visited
                        path.getVertices().add(neighbor); //Voeg neighbor toe aan current route

                        // Recursively search from this neighbor.
                        if (dfsRecursive(graph, neighbor, target, path)) { //Dit is een belangrijk deel. Hij roept dus nu opnieuw z'n eigen methode aan. Dat maakt het recursivea
                            return true;  // Target found in this path
                        }

                            path.getVertices().removeLast(); //BELANGRIJK: Stel we zijn van  B -> C aan het gaan, en C heeft geen nieuwe neighboors dan
                           // return dat hierboven (if (dfsRecursive etc)) dus false omdat neighbors een lijst returnt met 0 iteraties waardoor de loop niet wordt uitgevoerd en de methode
                           // HIERBOVEN dus false teruggeeft. Maar onthoud dat we dus op dit punt dus: path.getVertices().removeLast(); nog steeds in het blok code zitten
                           // van vertice B. Waardoor de vertice van de "current path" lijst wordt weggehaald. Onthoud dat C dus wel wordt toegevoegd aan de lijst
                           // met visited omdat in de neighbors lijst van B, C zit. En elke neighbor van B weer in de loop wordt toegevoegd aan visited.
                           // Dit was dus eerst heel verwarrend maar toen ik begreep dat je momenteel in het blok code van B zit en niet C snapte ik het. Onthoud dus
                           // dat in mijn voorbeeld C wel wordt toegevoegd in visited lijst, maar false returned waardoor if(dfsRecurse) niet wordt uitgevoerd en
                           // removeLast wel. OMDAT we nog in de iteratie zitten van vertice B.
                    }
                }
            }
      return false; //If we did not reach the target
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
     * Onthoud: Breadth = Breed hij zoekt breed ipv zoals bij depthFirstSearch waarbij hij heel diep gaat op 1 node zoekt breadthFirstSearch echt geleidelijk alle neighbors af en gaat niet door naar een volgende neighbor als de huidige neighbors nog niet explored zijn
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

        // Calculate the path from start to target by breadth-first-search
        // Use a queue for BFS (FIFO - First In First Out)
        Queue<V> queue = new LinkedList<>(); //Vraag: hoezo hebben we een queue nodig?
        //We hebben een queue nodig voor FIFO. First in first out. Zie het meer als een REGEL voor de lijst. Het garuantees dat je nooit in het midden opeens iets
        //kan plaatsen. Het garuentees dat items die eerst worden toegevoegd ALTIJD aan het begin staan. Voor verdere functies kijk hieronder:

        //Queue<String> queue = new LinkedList<>();
        //queue.add("A");   // queue: [A]
        //queue.add("B");   // queue: [A, B]
        //queue.offer("C"); // queue: [A, B, C]
        //System.out.println(queue.poll()); // removes and prints A -> queue: [B, C]
        //System.out.println(queue.peek()); // prints B, queue stays [B, C]
        //queue.remove();                   // removes B -> queue: [C]

        // Track visited vertices to avoid cycles
        Set<V> visited = new HashSet<>(); //Vraag: Hoezo kunnen ew niet gewoon zoals bij depthFirst path.getVisited.add doen ipv een nieuwe set voor visited

        // Track the parent of each vertex to reconstruct the path
        Map<V, V> parentMap = new HashMap<>(); //Een hashmap om bij te houden van welke vertice je vandaan komt. Dus het gaat om de prior vertex

        // Initialize: start vertex has no parent and is visited
        queue.offer(start);  // Add start to the queue, veiliger dan .add want .offer returned false als het niet lukt terwijl .add een exception gooit als het niet lukt.
        visited.add(start);  // Mark start as visited
        parentMap.put(start, null);  // Start has no parent

        // BFS main loop: process vertices level by level
        while (!queue.isEmpty()) {
            // Get the next vertex from the queue (FIFO order)
            V current = queue.poll(); //MAAKT CURRENT HET EERST VOLGENDE ITEM IN DE QUEUE (DUS START AAN HET BEGIN) EN REMOVED HEM DAARNA VAN DE QUEUE

            // Add to path.visited for statistics [TOT HIER BEETJE GELEERD]
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

        // No path found - target is not reachable from start
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
     * @return the shortest DGPath from start to target
     * returns null if either start or target cannot be matched with a vertex in the graph
     * or no path can be found from start to target
     */
    public static <V extends Identifiable, E> DGPath<V> dijkstraShortestPath(DirectedGraph<V, E> graph, String startId, String targetId, Function<E, Double> weightMapper) { //Function parameter is just so we can have the input of E and output of Double to represent the weight
        V start = graph.getVertexById(startId);
        V target = graph.getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath<V> path = new DGPath<>(); //Creates a new DGPath object, this is based on the inner class at the top and
        path.visited.add(start); //Adds the start vertice to visited because thats where you start

        // easy target (origin same as destination)
        if (start.equals(target)) {
            path.vertices.add(start); //If the destination is the same as the origin just return the path with the one vertice
            return path;
        }

        // keep track of the DSP status of all visited nodes
        Map<V, DSPNode<V>> dspProgress = new HashMap<>(); //Een Hashmap (box) met als key een Vertice en als value de DSPNode van de helper class. Dit zorgt ervoor dat je alle nodes kunt bijhouden in deze functie. Anders dan de visited lijst helemaal bovenin die puur over het resultaat gaat. Het is eigenlijk een soort notitieboekje die je helpt dingen bij te houden.

        // Priority queue for efficient retrieval of minimum weight node - O(log n) operations
        // Uses a comparator to order nodes by their weightSumTo value. Vraag zou zijn waarom PriorityQueue ipv normale Queue
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

            // Skip if already processed (can happen with duplicate entries) Also not sure if this is needed cause I think the double comparotar does this already
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

                //Nu voeg je voor alle vertices van reversePath doe aan de vertices singlylinkedlist toe waardoor je dus een mooie lijst krijgt met de snelste route van vertices
                for (V vertex : reversePath) {
                    path.vertices.add(vertex);
                }

                return path; //Return the correct DGPath
            }//End of the method that saves the path if the destination is reached

            Collection<V> neighbors = graph.getNeighbours(currentVertex); //Maakt collectie van alle neighbours van de huidige vertex
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
