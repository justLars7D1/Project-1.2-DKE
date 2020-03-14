package project12.ai;

import project12.gameelements.PuttingCourse;
import project12.physicsengine.Vector2d;

public interface PuttingBot {
    Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
