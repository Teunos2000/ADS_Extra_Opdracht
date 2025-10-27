package graphs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Performance benchmarking utility for comparing Insertion Sort vs QuickSort algorithms.
 * Measures execution times across dataset sizes for empirical complexity analysis.
 * Note: This is not a traditional unit test but a timing measurement tool that
 * generates data for external performance analysis and Big-O verification.
 * Key features:
 * - Measures runtime from 100 to 5,000,000 elements (or 20-second timeout)
 * - Ensures fair comparison through dataset replication
 * - Validates sorting correctness against Collections.sort()
 * - Controls JVM state for consistent timing measurements
 */
public class SorterPerformanceTest {

    private static final long SEED = 20211220L;
    private static final Random RANDOM = new Random(SEED);
    private static final long TIMEOUT_MS = 20_000; // 20 seconds in milliseconds
    private static final int MAX_SIZE = 5_000_000; // Maximum dataset size

    @Test
    void measureEfficiency() {
        System.out.println("=== SORTER PERFORMANCE BENCHMARK BY TEUN HILBERS ===");
        System.out.println("Comparing Insertion Sort vs QuickSort");
        System.out.println("(Note: Run with -Xlint on in run configs)\n");

        // Print header for the results table
        System.out.printf("%-10s | %-15s | %-15s | %-15s | %-10s%n",
                "Size", "Insertion (ms)", "QuickSort (ms)", "Java Sort (ms)", "Verified");
        System.out.println("-".repeat(80));

        // Create the Sorter instance for both algorithms
        Sorter<Country> sorter = new Sorter<>();

        // Comparator to sort countries by population
        Comparator<Country> comparator = Comparator.comparing(Country::getPopulation);

        // Track if we hit timeout for each algorithm
        boolean insertionTimeout = false;
        boolean quickSortTimeout = false;

        // Start with size 100 and double each time
        int size = 100;

        while (size <= MAX_SIZE) {
            // Generate the original dataset once for this size
            List<Country> originalData = generateCountryDataset(size);

            // Variables to store timing results
            long insertionTime = -1;
            long quickSortTime = -1;
            long javaSortTime = -1;
            boolean correctlySorted = true;

            // --- Test Insertion Sort ---
            if (!insertionTimeout) {
                // Create a copy for insertion sort
                List<Country> insertionData = new ArrayList<>(originalData);

                // Force garbage collection before timing
                System.gc();

                // Measure insertion sort time
                long startTime = System.currentTimeMillis();
                sorter.insertionSort(insertionData, comparator);
                long endTime = System.currentTimeMillis();

                insertionTime = endTime - startTime;

                // Check if we exceeded timeout
                if (insertionTime > TIMEOUT_MS) {
                    insertionTimeout = true;
                }

                // Verify correctness by comparing with Java's sort
                List<Country> verificationList = new ArrayList<>(originalData);
                Collections.sort(verificationList, comparator); //Collections.sort uses list.sort which uses array.sort which is a double pivot quicksort
                correctlySorted = insertionData.equals(verificationList);
            }

            // --- Test QuickSort ---
            if (!quickSortTimeout) {
                // Create a copy for quicksort
                List<Country> quickSortData = new ArrayList<>(originalData);

                // Force garbage collection before timing
                System.gc();

                // Measure quicksort time
                long startTime = System.currentTimeMillis();
                sorter.quickSort(quickSortData, comparator);
                long endTime = System.currentTimeMillis();

                quickSortTime = endTime - startTime;

                // Check if we exceeded timeout
                if (quickSortTime > TIMEOUT_MS) {
                    quickSortTimeout = true;
                }

                // Verify correctness if not already done
                if (insertionTimeout && correctlySorted) {
                    List<Country> verificationList = new ArrayList<>(originalData);
                    Collections.sort(verificationList, comparator);
                    correctlySorted = quickSortData.equals(verificationList);
                }
            }

            // --- Test Java's Built-in Sort for Reference ---
            // Create a copy for Java's sort
            List<Country> javaSortData = new ArrayList<>(originalData);

            // Force garbage collection before timing
            System.gc();

            // Measure Java's sort time
            long startTime = System.currentTimeMillis();
            Collections.sort(javaSortData, comparator);
            long endTime = System.currentTimeMillis();

            javaSortTime = endTime - startTime;

            // Print results for this size
            System.out.printf("%-10d | %-15s | %-15s | %-15d | %-10s%n",
                    size,
                    insertionTimeout ? "TIMEOUT" : String.valueOf(insertionTime),
                    quickSortTimeout ? "TIMEOUT" : String.valueOf(quickSortTime),
                    javaSortTime,
                    correctlySorted ? "Verified" : "Failed");

            // Stop if both algorithms have timed out
            if (insertionTimeout && quickSortTimeout) {
                System.out.println("\nBoth algorithms exceeded timeout. Stopping benchmark.");
                break;
            }

            // Double the size for next iteration
            size *= 2;
        }

        // Print analysis hints
        System.out.println("\n=== NOTES: ===");
        System.out.println("1. Insertion Sort: Expected O(n²) - time should quadruple when size doubles");
        System.out.println("2. QuickSort: Expected O(n log n) - time should slightly more than double");
        System.out.println("3. Java Sort: Uses TimSort, O(n log n) - highly optimized");
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