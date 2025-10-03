package route_planner;

import graphs.Searcher;
import graphs.Sorter;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Main entry point for the HvA RoutePlanner application.
 */
public class RoutePlannerMain {

    private static final long RANDOM_SEED = 20211220L;

    // Small demo map
    private static final String JUNCTIONS_SMALL_FILE = "Junctions0.csv";
    private static final String ROADS_SMALL_FILE = "Roads0.csv";

    // Full map
    private static final String JUNCTIONS_FILE = "Junctions.csv";
    private static final String ROADS_FILE = "Roads.csv";

    // Demo travel route
    private static final String FROM_ID = "Amsterdam";
    private static final String TO_ID = "Meppel";

    public static void main(String[] args) {
        System.out.println("🚗 Welcome to the HvA RoutePlanner Adventure!");

        loadSmallMapDemo();

        RoadMap roadMap = loadFullMapDemo();
        simulateAccidentDemo(roadMap);
        analyzeRoadNetwork(roadMap);
        runSortingDemo(roadMap);

        System.out.println("\n🎉 Trip complete! Thanks for using HvA RoutePlanner 🚀");
    }

    /**
     * Loads and demonstrates the small demo map with simple path searches.
     */
    private static void loadSmallMapDemo() {
        System.out.println("\n📍 Loading small demo map...");
        RoadMap.reSeedRandomizer(RANDOM_SEED);
        RoadMap roadMap = new RoadMap(JUNCTIONS_SMALL_FILE, ROADS_SMALL_FILE);
        System.out.println(roadMap);

        roadMap.svgDrawMap("RoadmapAMS.svg", null);

        System.out.println("🗺️ Exploring Oostzaan → Ouder-Amstel");
        doPathSearches(roadMap, "Oostzaan", "Ouder-Amstel");
    }

    /**
     * Loads the full map of the Netherlands and runs path searches.
     *
     * @return the loaded full RoadMap
     */
    private static RoadMap loadFullMapDemo() {
        System.out.println("\n🌍 Loading full map of the Netherlands...");
        RoadMap.reSeedRandomizer(RANDOM_SEED);
        RoadMap roadMap = new RoadMap(JUNCTIONS_FILE, ROADS_FILE);

        roadMap.svgDrawMap("RoadmapNL.svg", null);
        System.out.printf("🚦 Planning road trip: %s → %s%n", FROM_ID, TO_ID);
        doPathSearches(roadMap, FROM_ID, TO_ID);

        return roadMap;
    }

    /**
     * Simulates an accident between Diemen and Weesp and finds alternative routes.
     */
    private static void simulateAccidentDemo(RoadMap roadMap) {
        System.out.println("\n⚠️ Accident detected between Diemen and Weesp...");
        roadMap.getEdge("Diemen", "Weesp").setMaxSpeed(5);

        Searcher.DGPath<Junction> path =
                Searcher.dijkstraShortestPath(roadMap, FROM_ID, TO_ID,
                        r -> r.getLength() / r.getMaxSpeed());

        System.out.println("➡️ Fastest alternative route avoiding accident: " + path);
        roadMap.svgDrawMap(String.format("DSPACC-%s-%s.svg", FROM_ID, TO_ID), path);
    }

    /**
     * Runs different network analysis queries on the road map.
     */
    private static void analyzeRoadNetwork(RoadMap roadMap) {
        System.out.println("\n📊 Analyzing road network...");

        // Pick one outgoing road for each junction to construct the network map
        Map<Junction, Road> connections = new HashMap<>();
        for (Junction j : roadMap.getVertices()) {
            Collection<Road> outgoing = roadMap.getEdges(j);
            if (outgoing != null && !outgoing.isEmpty()) {
                connections.put(j, outgoing.iterator().next());
            }
        }

        RoadNetworkAnalysis analysis = new RoadNetworkAnalysis(connections);

        System.out.println("🏙️ Top 5 biggest cities: " + analysis.top5CityNamesByPopulation());
        System.out.println("🛣️ Total road length: " + analysis.totalRoadLength());
        System.out.println("⚡ Amount of roads faster than average: " + analysis.roadsFasterThanAverage().size());
        System.out.println("📌 Provinces with >10 cities: " + analysis.provincesWithMoreThanXCities(10));
        System.out.println("📍 Road length per province: " + analysis.totalRoadLengthPerProvince());
    }

    private static void doPathSearches(RoadMap roadMap, String fromId, String toId) {
        System.out.printf("\nResults from path searches from %s to %s:\n", fromId, toId);
        Searcher.DGPath<Junction> path;

        // find the routes by depth-first-search
        path = Searcher.depthFirstSearch(roadMap, fromId, toId);
        System.out.println("Depth-first-search: " + path);
        roadMap.svgDrawMap(String.format("DFS-%s-%s.svg", fromId, toId), path);

        // find the routes by breadth-first-search
        path = Searcher.breadthFirstSearch(roadMap, fromId, toId);
        System.out.println("Breadth-first-search: " + path);
        roadMap.svgDrawMap(String.format("BFS-%s-%s.svg", fromId, toId), path);

        // Dijkstra shortest by distance
        path = Searcher.dijkstraShortestPath(roadMap, fromId, toId, Road::getLength);
        System.out.println("Dijkstra-Shortest-Path (distance): " + path);
        roadMap.svgDrawMap(String.format("DSP-%s-%s.svg", fromId, toId), path);

        // Dijkstra fastest by travel time
        path = Searcher.dijkstraShortestPath(roadMap, fromId, toId, r -> r.getLength() / r.getMaxSpeed());
        System.out.println("Dijkstra-Fastest-Route (time): " + path);
        roadMap.svgDrawMap(String.format("DFR-%s-%s.svg", fromId, toId), path);
    }

    private static void runSortingDemo(RoadMap roadMap) {
        System.out.println("\n--- Sorting Demo ---");
        if (roadMap.getVertices().isEmpty()) {
            System.out.println("No junctions to sort");
            return;
        }

        List<Junction> original = new ArrayList<>(roadMap.getVertices());
        Sorter<Junction> sorter = new Sorter<>();

        benchmarkSort("Quick Sort (by population)", original,
                Comparator.comparingInt(Junction::getPopulation),
                sorter::quickSort,
                3);

        benchmarkSort("Quick Sort (by name)", original,
                Comparator.comparing(Junction::getId),
                sorter::quickSort,
                3);
    }

    private static void benchmarkSort(String sortName, List<Junction> original,
                                      Comparator<Junction> comparator,
                                      BiConsumer<List<Junction>, Comparator<Junction>> sortFunction,
                                      int runs) {
        System.out.printf("%n%s (%d runs):%n", sortName, runs);

        long totalTime = 0;
        for (int i = 0; i < runs; i++) {
            List<Junction> copy = new ArrayList<>(original);
            long start = System.nanoTime();
            sortFunction.accept(copy, comparator);
            long end = System.nanoTime();
            totalTime += (end - start);

            if (i == 0) {
                System.out.print("  Sample first 5: ");
                copy.stream().limit(5).forEach(j -> System.out.print(j.getId() + " "));
                System.out.println();
            }
        }
        double avgTime = totalTime / (double) runs / 1_000_000.0;
        System.out.printf("  Avg: %.3f ms%n", avgTime);
    }
}
