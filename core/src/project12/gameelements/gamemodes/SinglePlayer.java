package project12.gameelements.gamemodes;

import project12.gameelements.PuttingSimulator;
import project12.physicsengine.Vector2d;

public class SinglePlayer implements GameMode {

    private PuttingSimulator simulator;

    public SinglePlayer(PuttingSimulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public Vector2d takeTurn(Vector2d shotVelocity) {
        simulator.take_shot(shotVelocity);
        return simulator.get_ball_position();
    }

}
