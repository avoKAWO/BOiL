package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    private Node node;

    @BeforeEach
    public void setUp() {
        node = new Node("A", 3.0);
    }

    @Test
    public void testInitialValues() {
        assertEquals("A", node.getName());
        assertEquals(3.0, node.getActivityDuration());
        assertEquals(0.0, node.getReserve());
        assertEquals(0.0, node.getEarlyStartTime());
        assertEquals(0.0, node.getEarlyEndTime());
        assertEquals(0.0, node.getLateStartTime());
        assertEquals(0.0, node.getLateEndTime());
        assertTrue(node.children.isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        node.setName("B");
        node.setActivityDuration(5.0);
        node.setReserve(2.0);
        node.setEarlyStartTime(1.0);
        node.setEarlyEndTime(4.0);
        node.setLateStartTime(2.0);
        node.setLateEndTime(7.0);

        assertEquals("B", node.getName());
        assertEquals(5.0, node.getActivityDuration());
        assertEquals(2.0, node.getReserve());
        assertEquals(1.0, node.getEarlyStartTime());
        assertEquals(4.0, node.getEarlyEndTime());
        assertEquals(2.0, node.getLateStartTime());
        assertEquals(7.0, node.getLateEndTime());
    }

    @Test
    public void testAddChild() {
        Node child = new Node("C", 2.0);
        node.addChild(child);
        assertTrue(node.children.contains(child));
        assertEquals(1, node.children.size());
    }

    @Test
    public void testRemoveChild() {
        Node child = new Node("C", 2.0);
        node.addChild(child);
        node.removeChild(child);
        assertFalse(node.children.contains(child));
        assertEquals(0, node.children.size());
    }
}
