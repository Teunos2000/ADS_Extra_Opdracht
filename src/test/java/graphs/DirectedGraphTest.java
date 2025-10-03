package graphs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphTest {

    Country nl, be, de, lux, fr, uk, ro, hu;
    DirectedGraph<Country, Integer> europe = new DirectedGraph<>();
    DirectedGraph<Country, Integer> africa = new DirectedGraph<>();
    DirectedGraph<Country, Integer> emptyGraph = new DirectedGraph<>();


    @BeforeAll
    static void beforeALl() {
        Locale.setDefault(new Locale("nl", "NL"));
    }

    @BeforeEach
    void setUp() {
        nl = this.europe.addOrGetVertex(new Country("NL"));
        be = this.europe.addOrGetVertex(new Country("BE"));
        this.europe.addConnection("BE", "NL", 100);
        de = this.europe.addOrGetVertex(new Country("DE"));
        this.europe.addConnection("NL", "DE", 200);
        this.europe.addConnection("BE", "DE", 30);
        lux = this.europe.addOrGetVertex(new Country("LUX"));
        this.europe.addConnection("LUX", "BE", 60);
        this.europe.addConnection("LUX", "DE", 50);
        fr = this.europe.addOrGetVertex(new Country("FR"));
        this.europe.addConnection("FR", "LUX", 30);
        this.europe.addConnection("FR", "BE", 110);
        this.europe.addConnection("FR", "DE", 50);

        uk = this.europe.addOrGetVertex(new Country("UK"));
        this.europe.addConnection("UK", "BE", 70);
        this.europe.addConnection("UK", "FR", 150);
        this.europe.addConnection("UK", "NL", 250);

        ro = this.europe.addOrGetVertex(new Country("RO"));
        hu = this.europe.addOrGetVertex(new Country("HU"));
        this.europe.addConnection("RO", "HU", 250);
    }

    @AfterEach
    void checkRepresentationInvariants() {
        assertEquals(8, europe.getNumVertices());
        assertEquals(24, europe.getNumEdges());
        for (Country from : europe.getVertices()) {
            for (Country to : europe.getNeighbours(from)) {
                assertSame(europe.getEdge(from, to), europe.getEdge(to, from),
                        "Border between two countries should be the same object instance");
            }
        }
    }

    @Test
    void checkGetVertexById() {
        assertEquals(nl, europe.getVertexById("NL"));
        assertEquals(be, europe.getVertexById("BE"));
        assertNull(europe.getVertexById("XX"));
        assertNull(africa.getVertexById("XX"));
    }

    @Test
    void checkAddOrGetVertex() {
        int oldNumV = europe.getNumVertices();
        int oldNumE = europe.getNumEdges();
        assertSame(nl, europe.addOrGetVertex(new Country("NL")),
                "A duplicate instance of the same country should not be added, but return its primary instance");
        assertSame(lux, europe.addOrGetVertex(new Country("LUX")),
                "A duplicate instance of the same country should not be added, but return its primary instance");

        assertEquals(oldNumV, europe.getNumVertices());
        assertEquals(oldNumE, europe.getNumEdges());
    }

    @Test
    void checkAddEdge() {
        assertEquals(0, africa.getNumVertices());
        assertEquals(0, africa.getNumEdges());

        assertTrue(africa.addEdge(new Country("MO"), new Country("AL"), 200),
                "New countries should be added as part of adding a border");
        assertEquals(2, africa.getNumVertices());
        assertEquals(1, africa.getNumEdges());
        assertFalse(africa.addEdge(new Country("MO"), new Country("AL"), 300),
                "Only one directed border per from-country and to-country can be added");
        assertTrue(africa.addEdge("AL", "MO", 200));
        assertFalse(africa.addEdge("AL", "MO", 200),
                "Only one directed border per from-country and to-country can be added");
        assertFalse(africa.addEdge("MO", "AL", 300),
                "Only one directed border per from-country and to-country can be added");
        assertFalse(africa.addEdge("MO", "XX", 300),
                "No border can be added if one of the country id's doesn't match a country");
        assertFalse(africa.addEdge("XX", "AL", 300),
                "No border can be added if one of the country id's doesn't match a country");

        assertEquals(2, africa.getNumVertices());
        assertEquals(2, africa.getNumEdges());
    }

    @Test
    void checkBorderLength() {
        assertEquals(550, europe.getEdges("NL").stream().reduce(Integer::sum).orElse(0));
        assertEquals(370, europe.getEdges("BE").stream().reduce(Integer::sum).orElse(0));
    }

    @Test
    void checkGetEdge() {
        // Test getEdge with vertex objects
        assertEquals(Integer.valueOf(100), europe.getEdge(nl, be));
        assertEquals(Integer.valueOf(100), europe.getEdge(be, nl));
        assertEquals(Integer.valueOf(200), europe.getEdge(nl, de));
        assertEquals(Integer.valueOf(250), europe.getEdge(ro, hu));

        // Test getEdge with vertex ids
        assertEquals(Integer.valueOf(30), europe.getEdge("BE", "DE"));
        assertEquals(Integer.valueOf(60), europe.getEdge("LUX", "BE"));
        assertEquals(Integer.valueOf(150), europe.getEdge("UK", "FR"));

        // Test non-existent edges
        assertNull(europe.getEdge(nl, ro));
        assertNull(europe.getEdge("NL", "RO"));

        // Test null parameters
        assertNull(europe.getEdge(null, nl));
        assertNull(europe.getEdge(nl, null));
        assertNull(europe.getEdge("XX", "NL"));
        assertNull(europe.getEdge("NL", "XX"));
    }

    @Test
    void checkEmptyGraphBehavior() {
        assertEquals(0, emptyGraph.getNumVertices());
        assertEquals(0, emptyGraph.getNumEdges());

        Collection<Country> vertices = emptyGraph.getVertices();
        assertNotNull(vertices);
        assertTrue(vertices.isEmpty());

        assertNull(emptyGraph.getVertexById("XX"));
        assertNull(emptyGraph.getNeighbours("XX"));
        assertNull(emptyGraph.getEdges("XX"));
        assertNull(emptyGraph.getEdge("XX", "YY"));

        // Adding first vertex should work
        Country firstCountry = emptyGraph.addOrGetVertex(new Country("FIRST"));
        assertNotNull(firstCountry);
        assertEquals(1, emptyGraph.getNumVertices());
        assertEquals(0, emptyGraph.getNumEdges());
    }
}
