package project12.gameelements;

import project12.physicsengine.Function2d;
import project12.physicsengine.Vector2d;

class PuttingCourse {

    Function2d height;

    Vector2d flag;
    Vector2d start;

    //TODO: Hole tolerance and friction coefficient? Are we allowed to change the constructor?
    public PuttingCourse(Function2d height, Vector2d flag, Vector2d start) {
        this.height = height;
        this.flag = flag;
        this.start = start;
    }

    public PuttingCourse(Function2d height, Vector2d flag) {
        this.height = height;
        this.flag = flag;
        this.start = new Vector2d();
    }

    public Function2d get_height() {
        return this.height;
    }

    public Vector2d get_flag_position() {
        return this.flag;
    }

    public Vector2d get_start_position() {
        return this.start;
    }

//    TODO: Implement these methods somehow
//    public double get_friction_coefficient();
//    public double get_maximum_velocity();
//    public double get_hole_tolerance();

}

