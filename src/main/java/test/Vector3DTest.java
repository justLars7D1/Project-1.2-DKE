package test;

import org.junit.jupiter.api.Test;
import physicsengine.Vector;
import physicsengine.Vector3d;

import static org.junit.jupiter.api.Assertions.*;

class Vector3DTest extends Vector {

    @Test
    void testMagnitude() {
        Vector3d v = new Vector3d(3, 4);
        assertEquals(5, v.magnitude());
    }

    @Test
    void testNormalize() {
        Vector3d v = new Vector3d(3, 4);
        v.normalize();
        boolean t = (v.get_x() == 3/5.) && (v.get_z() == 4/5.);
        assertTrue(t);
    }

}