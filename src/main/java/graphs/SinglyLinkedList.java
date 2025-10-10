package graphs;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A doubly ended singly linked list.
 * Supports adding elements at the front and end,
 * retrieving by index, checking size, and iterating over elements.
 *
 * @param <E> the type of elements stored in the list
 */
public class SinglyLinkedList<E> implements Iterable<E> {
    private Node<E> head;       // first node containing the first item in the linked list
    private Node tail;       // last node containing the last item in the linked list
    private int size;           // current number of items in the linked list


    /**
     * Inner class representing a node in the singly linked list.
     * Based on this every node has a value of the generic type E
     */
    private static class Node<E> {
        private E value; //Contains the value of the node
        private Node<E> next; //Points ot the next node

        //Constructor for node
        public Node(E value) {
            this.value = value; //Assigns the value
            this.next = null;
        }
    }

    public SinglyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds an item to the end of the list
     * @param item the element to add
     */
    public void add(E item) {
        Node<E> newNode = new Node<>(item);

        //If the list is empty make the new node both head and tail. This makes sure adding is O(1) instead of O(n)
        if(head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode; //Adds the new Node to the next node of tail which was null
            tail = newNode; //Makes sure the tail is now the new Node
        }
        size++;
    }

    /**
     * Adds an item to the beginning of the list
     * @param item the element to add
     */
    public void addFirst(E item) {
        Node<E> newNode = new Node<>(item);

        //If the list is empty make the new node both head and tail. This makes sure adding is O(1) instead of O(n)
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head; //Makes the new node.next the (current) head, so the previous first item in the list
            head = newNode; //Then makes it the first item.
        }
        size++;
    }

    /**
     * Gets the element at the specified index but does not remove the node.
     * @param index the index of the element to retrieve
     * @return the item at the given position
     * @throws IndexOutOfBoundsException if index is not within range
     */
    public E get(int index) {
        //Check index bounds by checking if its smaller than 0 or exceeds the size
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current = head; //Start at the head

        // Traverse to the index position
        for (int i = 0; i < index; i++) {
            current = current.next;  //Move to next node
        }
         return current.value;
    }

    /**
     * @return the number of elements in the list
     *
     */
    public int size() {
        return this.size;
    }

    /**
     * Iterator is like a pointer that moves through the list. It lets me use the linkedlist in loops
     * Returns an iterator over the elements in this list. Which is of the generic type <E> because we want to return the same datatype as the value of the node
     * @return an {@link Iterator} over the elements in this list
     * @throws java.util.NoSuchElementException if the {@link Iterator#next()} method is called
     *         when no more elements are available
     */
    @Override
    public Iterator<E> iterator() {
        //Return the iterator because thats the return type of the function
        return new Iterator<E>() {
            // Keep track of current position starting at head
            private Node<E> current = head;

            //A boolean method to check if there is a next node. If not it will return false and it will return true if current has a value
            @Override
            public boolean hasNext() {
                // Returns true if there's a node to visit
                return current != null;
            }

            @Override
            public E next() {
                // Check if hasNext is false, if its false we throw the exception
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                // Get the data from current node, because we don't want to return the entire node
                E value = current.value;

                // Move to next node for next call
                current = current.next;

                // Return the data
                return value;
            }
        };
    }
}
