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
        // Implement insertion sort algorithm
        // Start from index 1 because index 0 is already "sorted" by itself
        for (int i = 1; i < items.size(); i++) {
            // Store the current element that needs to be inserted into the sorted portion
            E currentElement = items.get(i);

            // Start comparing with elements in the sorted portion (left side)
            // j represents the position we're checking in the sorted portion
            int j = i - 1;

            // Shift elements to the right while they are greater than currentElement
            // Keep moving left through the sorted portion until we find the right spot
            while (j >= 0 && comparator.compare(items.get(j), currentElement) > 0) {
                // Move the larger element one position to the right
                items.set(j + 1, items.get(j));

                // Move to the previous element in the sorted portion
                j--;
            }

            // Insert currentElement at its correct position in the sorted portion
            // j+1 because j was decremented one extra time in the while loop
            items.set(j + 1, currentElement);
        }

        // Return the sorted list (sorted in place)
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
        // Call the recursive helper method with initial bounds
        // Start with the entire list from index 0 to size-1
        quickSortRecursive(items, 0, items.size() - 1, comparator);

        // Return the sorted list (sorted in place)
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
        // Base case: if low >= high, we have 0 or 1 elements, so it's already sorted
        if (low >= high) {
            return;
        }

        // Choose the middle element as pivot (different from typical first/last element)
        // This helps avoid worst-case O(n²) for already sorted lists
        int pivotIndex = low + (high - low) / 2;
        E pivot = items.get(pivotIndex);

        // Swap pivot with the last element to get it out of the way
        swap(items, pivotIndex, high);

        // Partition the array around the pivot
        // After partitioning, all elements < pivot will be on the left
        // and all elements >= pivot will be on the right
        int partitionIndex = partition(items, low, high, pivot, comparator);

        // Recursively sort the left partition (elements less than pivot)
        quickSortRecursive(items, low, partitionIndex - 1, comparator);

        // Recursively sort the right partition (elements greater than or equal to pivot)
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

        // Put the pivot (currently at high) in its final sorted position
        swap(items, i, high);

        // Return the final position of the pivot
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
        // Only swap if indices are different to avoid unnecessary operations
        if (i != j) {
            E temp = items.get(i);
            items.set(i, items.get(j));
            items.set(j, temp);
        }
    }

}
