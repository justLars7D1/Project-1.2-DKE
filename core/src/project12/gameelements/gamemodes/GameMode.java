package project12.gameelements.gamemodes;

import project12.physicsengine.Vector2d;

public interface GameMode {
    Vector2d takeTurn(Vector2d shotVelocity);
}
