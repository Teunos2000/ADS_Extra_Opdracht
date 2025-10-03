package graphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SinglyLinkedListTest {

    private SinglyLinkedList<String> list;

    @BeforeEach
    void setup() {
        list = new SinglyLinkedList<>();
    }

    @Test
    void testNewListIsEmpty() {
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void testAddAppendsElement() {
        list.add("A");
        list.add("B");

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    void testAddFirstPrependsElement() {
        list.add("B");
        list.addFirst("A");

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    void testGetWithInvalidIndexThrows() {
        list.add("Only");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testIteratorTraversesElements() {
        list.add("A");
        list.add("B");

        Iterator<String> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIteratorThrowsWhenExhausted() {
        list.add("X");
        Iterator<String> it = list.iterator();
        it.next(); // consume X
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testForEachLoopWorks() {
        list.add("A");
        list.add("B");
        list.add("C");

        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }

        assertEquals("ABC", sb.toString());
    }
}
