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
        for (int i = 1; i < items.size(); i++) {
            E currentElement = items.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(items.get(j), currentElement) > 0) {
                items.set(j + 1, items.get(j));

                j--;
            }

            items.set(j + 1, currentElement);
        }

        return items;
    }

    /**
     * Sorts all items by quick sort using the provided comparator
     * for deciding relative ordening of two items.
     * Items are sorted 'in place' without use of an auxiliary list or array
     * Quicksort is o(n log n)
     * Je geeft
     * @param items
     * @param comparator
     * @return the items sorted in place
     */
    public List<E> quickSort(List<E> items, Comparator<E> comparator) {
        quickSortRecursive(items, 0, items.size() - 1, comparator);

        return items;
    }

    /**
     * Recursive helper method for quicksort algorithm
     * Sorts the portion of the list between low and high indices
     *
     * @param items the list to sort
     * @param low the starting index of the portion to sort
     * @param high the ending index of the portion to sort
     * @param comparator the comparator to determine element ordering
     */
    private void quickSortRecursive(List<E> items, int low, int high, Comparator<E> comparator) {
        if (low >= high) {
            return;
        }

        int pivotIndex = low + (high - low) / 2;
        E pivot = items.get(pivotIndex);
        swap(items, pivotIndex, high);

        int partitionIndex = partition(items, low, high, pivot, comparator);

        quickSortRecursive(items, low, partitionIndex - 1, comparator);
        quickSortRecursive(items, partitionIndex + 1, high, comparator);
    }

    /**
     * Partitions the list around a pivot element
     * All elements smaller than pivot go to the left, larger go to the right
     * @param items the list to partition
     * @param low the starting index for partitioning
     * @param high the ending index (where pivot is stored)
     * @param pivot the pivot element to partition around
     * @param comparator the comparator for element comparison
     * @return the final position of the pivot after partitioning
     */
    private int partition(List<E> items, int low, int high, E pivot, Comparator<E> comparator) {
        // i keeps track of the position where the next smaller element should go
        int i = low;

        // j scans through the array looking for elements smaller than pivot
        for (int j = low; j < high; j++) {
            // If current element is less than pivot, it should go to the left partition
            if (comparator.compare(items.get(j), pivot) < 0) {
                // Swap the smaller element to position i
                swap(items, i, j);
                // Move i forward for the next smaller element
                i++;
            }
        }

        swap(items, i, high);

        return i;
    }

    /**
     * Helper method to swap two elements in the list
     *
     * @param items the list containing elements to swap
     * @param i index of first element
     * @param j index of second element
     */
    private void swap(List<E> items, int i, int j) {
        if (i != j) {
            E temp = items.get(i);
            items.set(i, items.get(j));
            items.set(j, temp);
        }
    }

}
