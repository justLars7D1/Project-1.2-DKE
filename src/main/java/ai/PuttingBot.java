package ai;

import gameelements.PuttingCourse;
import physicsengine.Vector3d;

public interface PuttingBot {
    Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position);
}
