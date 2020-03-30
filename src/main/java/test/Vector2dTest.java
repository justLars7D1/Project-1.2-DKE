package test;

import org.junit.jupiter.api.Test;
import physicsengine.Vector;
import physicsengine.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest extends Vector {

    @Test
    void testMagnitude() {
        Vector2d v = new Vector2d(3, 4);
        assertEquals(5, v.magnitude());
    }

    @Test
    void testNormalize() {
        Vector2d v = new Vector2d(3, 4);
        v.normalize();
        boolean t = (v.get_x() == 3/5.) && (v.get_y() == 4/5.);
        assertTrue(t);
    }

}