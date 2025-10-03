package graphs;

import java.util.Comparator;
import java.util.List;

public class Sorter<E> {

    /**
     * Sorts all items by insertion sort using the provided comparator
     * for deciding relative ordening of two items.
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items
     * @param comparator
     * @return the items sorted in place
     */
    public List<E> insertionSort(List<E> items, Comparator<E> comparator) {
        // TODO implement selection sort or insertion sort or bubble sort

        return items;   // replace as you find appropriate

    }

    /**
     * Sorts all items by quick sort using the provided comparator
     * for deciding relative ordening of two items.
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items
     * @param comparator
     * @return the items sorted in place
     */
    public List<E> quickSort(List<E> items, Comparator<E> comparator) {
        // TODO provide a recursive quickSort implementation,
        //  that is different from the example given in the lecture

        return items;   // replace as you find appropriate
    }

}
