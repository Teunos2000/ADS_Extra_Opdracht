package graphs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CountrySearcherTest {

    Country nl, be, de, lux, fr, uk, ro, hu;
    DirectedGraph<Country, Integer> europe = new DirectedGraph<>();

    @BeforeAll
    static void beforeAll() {
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

    @Test
    void checkDFSearch() {
        Searcher.DGPath<Country> path = Searcher.depthFirstSearch(europe, "UK", "LUX");
        assertNotNull(path);
        assertSame(europe.getVertexById("UK"), path.getVertices().get(0));
        assertSame(europe.getVertexById("LUX"), path.getVertices().get(path.getVertices().size() - 1));
        assertTrue(path.getVertices().size() >= 3);
        assertTrue(path.getVisited().size() >= path.getVertices().size());
    }

    @Test
    void checkDFSearchStartIsTarget() {
        Searcher.DGPath<Country> path = Searcher.depthFirstSearch(europe, "HU", "HU");
        assertNotNull(path);
        assertSame(europe.getVertexById("HU"), path.getVertices().get(0));
        assertEquals(1, path.getVertices().size());
        assertEquals(1, path.getVisited().size());
    }

    @Test
    void checkDFSearchUnconnected() {
        Searcher.DGPath<Country> path = Searcher.depthFirstSearch(europe, "UK", "HU");
        assertNull(path);
    }

    @Test
    void checkBFSearch() {
        Searcher.DGPath<Country> path = Searcher.breadthFirstSearch(europe, "UK", "LUX");
        assertNotNull(path);
        assertSame(europe.getVertexById("UK"), path.getVertices().get(0));
        assertSame(europe.getVertexById("LUX"), path.getVertices().get(path.getVertices().size() - 1));
        assertEquals(3, path.getVertices().size());
        assertTrue(path.getVisited().size() >= path.getVertices().size());
    }

    @Test
    void checkBFSearchStartIsTarget() {
        Searcher.DGPath<Country> path = Searcher.breadthFirstSearch(europe, "HU", "HU");
        assertNotNull(path);
        assertSame(europe.getVertexById("HU"), path.getVertices().get(0));
        assertEquals(1, path.getVertices().size());
        assertEquals(1, path.getVisited().size());
    }

    @Test
    void checkBFSearchUnconnected() {
        Searcher.DGPath<Country> path = Searcher.breadthFirstSearch(europe, "UK", "HU");
        assertNull(path);
    }

    @Test
    void checkDSPSearch() {
        Searcher.DGPath<Country> path = Searcher.dijkstraShortestPath(europe, "UK", "LUX", b -> 2.0);
        assertNotNull(path);
        assertSame(europe.getVertexById("UK"), path.getVertices().get(0));
        assertSame(europe.getVertexById("LUX"), path.getVertices().get(path.getVertices().size() - 1));
        assertEquals(4.0, path.getTotalWeight(), 0.0001);
        assertEquals(path.getTotalWeight(), 2.0 * (path.getVertices().size() - 1), 0.0001);
        assertTrue(path.getVisited().size() >= path.getVertices().size());
    }

    @Test
    void checkDSPSearchStartIsTarget() {
        Searcher.DGPath<Country> path = Searcher.dijkstraShortestPath(europe, "HU", "HU", b -> 2.0);
        assertNotNull(path);
        assertEquals(europe.getVertexById("HU"), path.getVertices().get(0));
        assertEquals(0.0, path.getTotalWeight(), 0.0001);
        assertEquals(1, path.getVertices().size());
        assertEquals(1, path.getVisited().size());
    }

    @Test
    void checkDSPSearchUnconnected() {
        Searcher.DGPath<Country> path = Searcher.dijkstraShortestPath(europe, "UK", "HU", b -> 2.0);
        assertNull(path);
    }
}
