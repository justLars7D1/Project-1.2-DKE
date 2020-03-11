package project12.gameelements;

import project12.physicsengine.PhysicsEngine;
import project12.physicsengine.Vector2d;

class PuttingSimulator {

    PuttingCourse course;
    PhysicsEngine engine;

    Vector2d ballPosition;

    public PuttingSimulator(PuttingCourse course, PhysicsEngine engine) {
        this.course = course;
        this.engine = engine;
        this.ballPosition = course.get_start_position();
    }

    public void set_ball_position(Vector2d p) {
        this.ballPosition = p;
    }

    public Vector2d get_ball_position() {
        return this.ballPosition;
    }

    public void take_shot(Vector2d initial_ball_velocity) {
        // Code for taking the shot (so using the provided physics engine and course)
    }

}

