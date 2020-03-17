package project12.gameelements.gamemodes;

import project12.gameelements.PuttingSimulator;
import project12.physicsengine.Vector2d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileReaderMode implements GameMode {

    private PuttingSimulator simulator;
    private BufferedReader velocityReader;

    public FileReaderMode(PuttingSimulator simulator, String velocityFilePath) {
        this.simulator = simulator;
        try {
            this.velocityReader = new BufferedReader(new FileReader(new File(velocityFilePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector2d takeTurn(Vector2d shotVelocity) {
        return null;
    }

}
