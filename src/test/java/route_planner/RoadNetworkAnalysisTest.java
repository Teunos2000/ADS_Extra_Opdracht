package route_planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RoadNetworkAnalysisTest {

    private Junction amsterdam, rotterdam, utrecht, haarlem, delft;
    private Road road1, road2, road3, road4, road5;
    private RoadNetworkAnalysis analysis;


    @BeforeEach
    void setUp() {
        // Create junctions
        amsterdam = new Junction.Builder()
                .name("Amsterdam")
                .province("Noord-Holland")
                .population(1000000)
                .build();

        rotterdam = new Junction.Builder()
                .name("Rotterdam")
                .province("Zuid-Holland")
                .population(650000)
                .build();

        utrecht = new Junction.Builder()
                .name("Utrecht")
                .province("Utrecht")
                .population(350000)
                .build();

        haarlem = new Junction.Builder()
                .name("Haarlem")
                .province("Noord-Holland")
                .population(160000)
                .build();

        delft = new Junction.Builder()
                .name("Delft")
                .province("Zuid-Holland")
                .population(100000)
                .build();

        // Create roads
        road1 = new Road("A1", 100, 120);
        road2 = new Road("A2", 80, 100);
        road3 = new Road("A3", 60, 90);
        road4 = new Road("A4", 120, 130);
        road5 = new Road("A5", 50, 70);

        // Map junctions to roads
        Map<Junction, Road> roadMap = new HashMap<>();
        roadMap.put(amsterdam, road1);
        roadMap.put(rotterdam, road2);
        roadMap.put(utrecht, road3);
        roadMap.put(haarlem, road4);
        roadMap.put(delft, road5);

        // Initialize analysis
        analysis = new RoadNetworkAnalysis(roadMap);
    }

    @Test
    void testCitiesInSameProvince() {
        List<Junction> NHCities = analysis.citiesInSameProvince(amsterdam);
        assertEquals(1, NHCities.size());
        assertTrue(NHCities.contains(haarlem));

        List<Junction> ZHCities = analysis.citiesInSameProvince(rotterdam);
        assertEquals(1, ZHCities.size());
        assertTrue(ZHCities.contains(delft));

        List<Junction> utrechtCities = analysis.citiesInSameProvince(utrecht);
        assertTrue(utrechtCities.isEmpty());
    }

    @Test
    void testTotalRoadLength() {
        double expectedLength = road1.getLength() + road2.getLength() + road3.getLength() + road4.getLength() + road5.getLength();
        assertEquals(expectedLength, analysis.totalRoadLength(), 0.001);
    }

    @Test
    void testTop5CityNamesByPopulation() {
        List<String> topCities = analysis.top5CityNamesByPopulation();
        assertEquals(5, topCities.size());
        assertEquals("Amsterdam", topCities.get(0));
        assertEquals("Rotterdam", topCities.get(1));
        assertEquals("Utrecht", topCities.get(2));
        assertEquals("Haarlem", topCities.get(3));
        assertEquals("Delft", topCities.get(4));
    }

    @Test
    void testTotalLengthFromBigCities() {
        double length = analysis.totalLengthFromBigCities(500_000);
        // Only Amsterdam and Rotterdam exceed 500_000
        assertEquals(road1.getLength() + road2.getLength(), length, 0.001);

        // Threshold above all populations
        assertEquals(0.0, analysis.totalLengthFromBigCities(2_000_000), 0.001);
    }

    @Test
    void testTotalRoadLengthPerProvince() {
        Map<String, Double> lengths = analysis.totalRoadLengthPerProvince();
        assertEquals(3, lengths.size());
        assertEquals(road1.getLength() + road4.getLength(), lengths.get("Noord-Holland"), 0.001);
        assertEquals(road2.getLength() + road5.getLength(), lengths.get("Zuid-Holland"), 0.001);
    }

    @Test
    void testRoadsFasterThanAverage() {
        List<Road> fastRoads = analysis.roadsFasterThanAverage();
        // Average max speed
        double avg = (road1.getMaxSpeed() + road2.getMaxSpeed() + road3.getMaxSpeed() + road4.getMaxSpeed() + road5.getMaxSpeed()) / 5.0;
        for (Road r : fastRoads) {
            assertTrue(r.getMaxSpeed() > avg);
        }
    }

    @Test
    void testProvincesWithMoreThanXCities() {
        List<String> provinces = analysis.provincesWithMoreThanXCities(1);
        assertEquals(2, provinces.size());
        assertTrue(provinces.contains("Noord-Holland"));
        assertTrue(provinces.contains("Zuid-Holland"));

        provinces = analysis.provincesWithMoreThanXCities(2);
        assertTrue(provinces.isEmpty());
    }

    @Test
    void testEmptyRoadNetwork() {
        RoadNetworkAnalysis emptyAnalysis = new RoadNetworkAnalysis(new HashMap<>());

        assertTrue(emptyAnalysis.citiesInSameProvince(amsterdam).isEmpty());
        assertEquals(0.0, emptyAnalysis.totalRoadLength(), 0.001);
        assertTrue(emptyAnalysis.top5CityNamesByPopulation().isEmpty());
        assertEquals(0.0, emptyAnalysis.totalLengthFromBigCities(0), 0.001);
        assertTrue(emptyAnalysis.totalRoadLengthPerProvince().isEmpty());
        assertTrue(emptyAnalysis.roadsFasterThanAverage().isEmpty());
        assertTrue(emptyAnalysis.provincesWithMoreThanXCities(0).isEmpty());
    }
}
