package graphs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Performance benchmarking utility for comparing Insertion Sort vs QuickSort algorithms.
 * Measures execution times across varying dataset sizes for empirical complexity analysis.
 * <p>
 * Note: This is not a traditional unit test but a timing measurement tool that
 * generates data for external performance analysis and Big-O verification.
 * <p>
 * Key features:
 * - Measures runtime from 100 to 5,000,000 elements (or 20-second timeout)
 * - Ensures fair comparison through dataset replication
 * - Validates sorting correctness against Collections.sort()
 * - Controls JVM state for consistent timing measurements
 */
public class SorterPerformanceTest {

    private static final long SEED = 20211220L;
    private static final Random RANDOM = new Random(SEED);


    @Test
    void measureEfficiency() {
        // TODO Benchmarking code here...

    }


    // Private helper method
    private List<Country> generateCountryDataset(int size) {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String name = "Country_" + String.format("%06d", i);
            int population = RANDOM.nextInt(100_000_000) + 1;
            countries.add(new Country(name, population));
        }
        Collections.shuffle(countries, RANDOM); // same seed = same shuffle
        return countries;
    }

}
