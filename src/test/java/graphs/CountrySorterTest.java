package graphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

public class CountrySorterTest {

    private Sorter<Country> sorter;
    private List<Country> countries;
    private final Comparator<Country> populationRanking = Comparator.comparing(Country::getPopulation).thenComparing(Country::getName);
    private final Comparator<Country> nameRanking = Comparator.comparing(Country::getName);

    @BeforeEach
    void setUp() {
        sorter = new Sorter<>();
        countries = new ArrayList<>(List.of(
                new Country("NL", 17000000),
                new Country("BE", 11500000),
                new Country("DE", 83000000),
                new Country("LUX", 650000),
                new Country("FR", 67000000),
                new Country("UK", 67000000),
                new Country("RO", 19000000),
                new Country("HU", 9700000)
        ));
    }

    @Test
    void insertionSortAndCollectionSortYieldSameOrder() {
        customSortAndCollectionSortResultInSameOrder(sorter::insertionSort);
    }

    @Test
    void quickSortAndCollectionSortYieldSameOrder() {
        customSortAndCollectionSortResultInSameOrder(sorter::quickSort);
    }

    @Test
    void sortingEmptyListShouldNotFail() {
        List<Country> empty = new ArrayList<>();
        sorter.insertionSort(empty, nameRanking);
        sorter.quickSort(empty, populationRanking);
        assertTrue(empty.isEmpty());
    }

    @Test
    void sortingSingleElementListShouldNotChangeIt() {
        List<Country> single = new ArrayList<>(List.of(new Country("AA", 123)));
        sorter.quickSort(single, nameRanking);
        sorter.insertionSort(single, populationRanking);
        assertEquals("AA", single.get(0).getName());
    }

    @Test
    void sortingAlreadySortedListShouldNotChangeOrder() {
        List<Country> sortedByName = new ArrayList<>(countries);
        sortedByName.sort(nameRanking);

        List<Country> copy = new ArrayList<>(sortedByName);
        sorter.insertionSort(copy, nameRanking);
        assertEquals(sortedByName, copy);

        copy = new ArrayList<>(sortedByName);
        sorter.quickSort(copy, nameRanking);
        assertEquals(sortedByName, copy);
    }

    @Test
    void sortingReverseSortedListShouldWork() {
        List<Country> reverse = new ArrayList<>(countries);
        reverse.sort(populationRanking.reversed());

        List<Country> expected = new ArrayList<>(countries);
        expected.sort(populationRanking);

        sorter.quickSort(reverse, populationRanking);
        assertEquals(expected, reverse);
    }

    @Test
    void sortingListWithDuplicatesShouldBeConsistent() {
        List<Country> dupes = new ArrayList<>(countries);
        dupes.add(new Country("FakeFR", 67000000)); // duplicate population

        List<Country> expected = new ArrayList<>(dupes);
        expected.sort(populationRanking);

        sorter.insertionSort(dupes, populationRanking);
        assertEquals(expected, dupes);
    }

    @Test
    void sortingWithNullComparatorShouldThrow() {
        List<Country> list = new ArrayList<>(countries);
        assertThrows(NullPointerException.class, () -> sorter.quickSort(list, null));
    }


    private void customSortAndCollectionSortResultInSameOrder(
            BiFunction<List<Country>, Comparator<Country>, List<Country>> sorterMethod) {


        List<Country> fewShuffled = new ArrayList<>(countries);
        Collections.shuffle(fewShuffled);

        sorterMethod.apply(fewShuffled, nameRanking);
        List<Country> expectedFew = new ArrayList<>(countries);
        expectedFew.sort(nameRanking);
        String diff = findFirstDifference(expectedFew, fewShuffled, nameRanking, 3);
        assertNull(diff, diff);


        List<Country> manyShuffled = new ArrayList<>(countries);
        Collections.shuffle(manyShuffled);

        sorterMethod.apply(manyShuffled, populationRanking);
        List<Country> expectedMany = new ArrayList<>(countries);
        expectedMany.sort(populationRanking);
        diff = findFirstDifference(expectedMany, manyShuffled, populationRanking, 3);
        assertNull(diff, diff);
    }


    private <E> String findFirstDifference(List<E> expected, List<E> actual,
                                           Comparator<E> ranker, int displayLength) {
        if (expected.size() != actual.size()) {
            return String.format("Expected list with size=%d, got %d", expected.size(), actual.size());
        }
        for (int i = 0; i < expected.size(); i++) {
            if (ranker.compare(actual.get(i), expected.get(i)) != 0) {
                int subListEnd = Integer.min(i + displayLength, expected.size());
                return String.format("Expected items[%d..%d] = %s,\n   got: %s",
                        i, subListEnd - 1,
                        expected.subList(i, subListEnd),
                        actual.subList(i, subListEnd));
            }
        }
        return null;
    }

}
