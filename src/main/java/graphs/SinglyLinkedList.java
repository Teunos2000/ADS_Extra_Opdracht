package graphs;


import java.util.Iterator;

/**
 * A doubly ended singly linked list.
 * Supports adding elements at the front and end,
 * retrieving by index, checking size, and iterating over elements.
 *
 * @param <E> the type of elements stored in the list
 */
public class SinglyLinkedList<E> implements Iterable<E> {
    private Node<E> head;       // first node containing the first item in the linked list
    private Node<E> tail;       // last node containing the last item in the linked list
    private int size;           // current number of items in the linked list


    /**
     * Inner class representing a node in the singly linked list.
     */
    private static class Node<E> {
        // TODO: add necessary attributes (data, next reference) & a constructor
    }

    public SinglyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds an item to the end of the list
     *
     * @param item the element to add
     */
    public void add(E item) {
        // TODO: create a new node and attach it at the end of the list
        // TODO: increment size
    }

    /**
     * Adds an item to the beginning of the list
     *
     * @param item the element to add
     */
    public void addFirst(E item) {
        // TODO: create a new node and set it as the new head
    }

    /**
     * Gets the element at the specified index
     *
     * @param index the index of the element to retrieve
     * @return the item at the given position
     * @throws IndexOutOfBoundsException if index is not within range
     */
    public E get(int index) {
        // TODO: find the node at the index position and return the item from the node

         return null;
    }

    /**
     * @return the number of elements in the list
     *
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an {@link Iterator} over the elements in this list
     * @throws java.util.NoSuchElementException if the {@link Iterator#next()} method is called
     *         when no more elements are available
     */
    @Override
    public Iterator<E> iterator() {
        // TODO replace below iterator by a more memory efficient, true iterator instance
        //      that iterates along all items in the list.


        return null;
    }
}
