package project12.physicsengine.test;

import project12.physicsengine.CourseFunction;
import project12.physicsengine.Vector2d;

public class TestEngine {
    public static void main(String[] args) {

        CourseFunction f = new CourseFunction();

        Vector2d gradient = f.gradient(new Vector2d(1,0));
        //gradient.normalize();
        System.out.println(gradient);

    }
}
