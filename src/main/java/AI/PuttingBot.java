package AI;

import PhysicsEngine.Vector2d;

interface PuttingBot {
    Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
