package route_planner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class that provides analysis methods for junctions (cities) and roads.
 */
public class RoadNetworkAnalysis {

    /**
     * Represents the road network as a mapping between junctions and roads.
     */
    private final Map<Junction, Road> roadNetwork;

    public RoadNetworkAnalysis(Map<Junction, Road> connections) {
        this.roadNetwork = connections;
    }

    /**
     * Finds all cities (junctions) in the same province as the given city.
     * Returns an empty list if no cities match.
     */
    public List<Junction> citiesInSameProvince(Junction city) {
        // Get the province of the given city
        String targetProvince = city.getProvince();

        // Filter all junctions that are in the same province as the given city
        // Stream through all keys (junctions) in the roadNetwork map
        return roadNetwork.keySet().stream()
                .filter(junction -> junction.getProvince().equals(targetProvince))  // Keep only junctions in same province
                .collect(Collectors.toList());  // Collect results into a List
    }

    /**
     * Calculates the total length of all roads.
     */
    public double totalRoadLength() {
        // Stream through all road values in the network
        // Map each road to its length (double value)
        // Sum all the lengths together
        return roadNetwork.values().stream()
                .mapToDouble(Road::getLength)  // Convert each Road to its length (double)
                .sum();  // Sum all lengths together
    }

    /**
     * Returns the names of the top 5 most populated cities in the road network.
     */
    public List<String> top5CityNamesByPopulation() {
        // Stream through all junctions (keys of the map)
        // Sort by population in descending order (highest first)
        // Take only the first 5
        // Map to city names
        // Collect as a list
        return roadNetwork.keySet().stream()
                .sorted(Comparator.comparing(Junction::getPopulation).reversed())  // Sort by population descending
                .limit(5)  // Take only top 5
                .map(Junction::getName)  // Convert Junction objects to their names (String)
                .collect(Collectors.toList());  // Collect names into a List
    }

    /**
     * Finds the total length of all roads starting from cities with population above a threshold.
     */
    public double totalLengthFromBigCities(int minPopulation) {
        // Filter entries where the junction (key) has population >= minPopulation
        // Map to the road lengths
        // Sum the lengths
        return roadNetwork.entrySet().stream()
                .filter(entry -> entry.getKey().getPopulation() >= minPopulation)  // Keep only big cities
                .map(Map.Entry::getValue)  // Get the Road from each entry
                .mapToDouble(Road::getLength)  // Convert each Road to its length
                .sum();  // Sum all lengths
    }

    /**
     * Calculates the total length of all roads for each province.
     *
     * @return a map where the key is the province name and the value is the total
     * length of roads in that province; provinces with no roads will
     * not appear in the map.
     */
    public Map<String, Double> totalRoadLengthPerProvince() {
        // Group entries by province name
        // For each province group, sum the road lengths
        // Use Collectors.groupingBy with a downstream collector to sum
        return roadNetwork.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().getProvince(),  // Group by province name
                        Collectors.summingDouble(entry -> entry.getValue().getLength())  // Sum road lengths for each group
                ));
    }

    /**
     * Returns all roads where the speed limit is higher than the average speed limit.
     */
    public List<Road> roadsFasterThanAverage() {
        // First calculate the average speed of all roads
        double averageSpeed = roadNetwork.values().stream()
                .mapToDouble(Road::getMaxSpeed)  // Map each road to its max speed
                .average()  // Calculate average
                .orElse(0.0);  // Default to 0 if no roads exist

        // Filter roads with speed higher than average
        // Return as a list
        return roadNetwork.values().stream()
                .filter(road -> road.getMaxSpeed() > averageSpeed)  // Keep only roads faster than average
                .collect(Collectors.toList());  // Collect into a List
    }

    /**
     * Finds all provinces that have more than X cities.
     * Use Collectors.groupingBy(), Collectors.counting(),
     */
    public List<String> provincesWithMoreThanXCities(int x) {
        // Group junctions by province and count how many cities per province
        // Filter provinces with more than x cities
        // Return province names as a list
        return roadNetwork.keySet().stream()
                .collect(Collectors.groupingBy(
                        Junction::getProvince,  // Group by province name
                        Collectors.counting()    // Count junctions in each province
                ))
                .entrySet().stream()  // Stream through the province->count map
                .filter(entry -> entry.getValue() > x)  // Keep provinces with more than x cities
                .map(Map.Entry::getKey)  // Get the province name (key)
                .collect(Collectors.toList());  // Collect province names into a List
    }
}