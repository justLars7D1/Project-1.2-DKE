package project12.test;

import org.junit.jupiter.api.Test;
import project12.physicsengine.functions.CourseFunction;
import project12.physicsengine.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

class CourseFunctionTest {

    @Test
    void evaluate() {
        CourseFunction f = new CourseFunction(0, 0, 0, 0, 0, 1);
        assertEquals(1, f.evaluate(new Vector2d()));
    }

    @Test
    void gradient() {
        CourseFunction f = new CourseFunction(0, 0, 0, 0, 0, 1);
        Vector2d gradient = f.gradient(new Vector2d(1, 1));
        assertTrue((gradient.get_x() == 0) && (gradient.get_y() == 0));
    }
}