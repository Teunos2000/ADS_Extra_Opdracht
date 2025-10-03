package route_planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JunctionTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void checkHashCodeAndEquals() {
        Junction a1 = new Junction("Amsterdam");
        Junction a2 = new Junction(new String("Amsterdam"));
        Junction r = new Junction("Rotterdam");
        assertEquals(a1, a1);
        assertEquals(a1, a2,
                "Junctions with the same Id should be equal");
        assertEquals(a1.hashCode(), a2.hashCode(),
                "Equal Junctions should have the same hash code");
        assertNotSame(a1.getName(), a2.getName(), "The setup of this test should involve different strings for Amsterdam");
        assertNotEquals(a1, r,
                "Junctions with a different Id should be different");
        assertNotEquals(a1.hashCode(), r.hashCode(),
                "It is allowed but un-likely that different junctions have the same hash code");
    }

    @Test
    void testBuildWithOnlyName() {
        Junction j = new Junction.Builder()
                .name("Rotterdam")
                .build();

        assertEquals("Rotterdam", j.getName());
        assertNull(j.getProvince());
        assertEquals(0.0, j.getLocationX());
        assertEquals(0.0, j.getLocationY());
        assertEquals(0, j.getPopulation());
    }

    @Test
    void testMethodChaining() {
        Junction.Builder builder = new Junction.Builder();
        // method chaining should work without errors
        builder.name("Utrecht").province("Utrecht").population(357179).location(120.5, 488.0);

        Junction j = builder.build();
        assertEquals("Utrecht", j.getName());
        assertEquals("Utrecht", j.getProvince());
        assertEquals(357179, j.getPopulation());
    }

    @Test
    void testTwoDifferentJunctions() {
        Junction j1 = new Junction.Builder()
                .name("Leiden")
                .province("Zuid-Holland")
                .population(124899)
                .location(122.0, 486.0)
                .build();

        Junction j2 = new Junction.Builder()
                .name("Groningen")
                .province("Groningen")
                .population(202567)
                .location(130.0, 500.0)
                .build();

        assertNotEquals(j1.getName(), j2.getName());
        assertNotEquals(j1.getProvince(), j2.getProvince());
        assertTrue(j2.getPopulation() > j1.getPopulation());
    }
}
