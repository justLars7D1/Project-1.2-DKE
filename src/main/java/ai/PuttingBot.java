package ai;

import gameelements.PuttingCourse;
import physicsengine.Vector2d;

public interface PuttingBot {
    Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
