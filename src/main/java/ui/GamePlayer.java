package ui;

import gameelements.PuttingSimulator;
import org.joml.Vector3f;
import physicsengine.Vector3d;
import ui.entities.UIPlayer;
import ui.models.RawModel;
import ui.models.TexturedModel;
import ui.objloader.ModelData;
import ui.objloader.OBJLoader;
import ui.renderEngine.GameMap;
import ui.renderEngine.Loader;
import ui.textures.ModelTexture;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePlayer {

    private final UIPlayer uiPlayer;
    private String type;

    private final PuttingSimulator simulator;

    private int numMovesMade;

    private Vector3d[] shotsInFile;
    int currentShotIndex;

    public GamePlayer(String type, Loader loader, Vector3f position, GameMap map, PuttingSimulator simulator) {
        this.simulator = simulator;
        this.type = type;
        this.uiPlayer = createPlayer(loader);
        uiPlayer.setPosition(position);
        map.addPlayers(uiPlayer);
        if (type.equals("file")) {
            getShotDataFromFile();
        }
    }

    public void takeShot(Vector3d ballVelocity) {
        this.numMovesMade++;
        if (type.equals("user")) {
            simulator.take_shot(ballVelocity, uiPlayer);
        } else if (type.equals("file")) {
            if (currentShotIndex < shotsInFile.length) {
                simulator.take_shot(shotsInFile[currentShotIndex++], uiPlayer);
            } else {
                type = "user";
            }
        }
    }

    private static UIPlayer createPlayer(Loader loader) {
        ModelData data = OBJLoader.loadOBJ("game/entities/golfBall/golfBall");
        RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("game/entities/bunny/white"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        return new UIPlayer(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, .1f);
    }

    public void addMove() {
        this.numMovesMade++;
    }

    public int getNumMovesMade() {
        return numMovesMade;
    }

    public Vector3d getPosition() {
        return simulator.get_ball_position();
    }

    public void setPosition(Vector3f position) {
        this.uiPlayer.setPosition(position);
        this.simulator.set_ball_position(new Vector3d(position.x, position.z));
    }

    public void setPosition(Vector3d position) {
        simulator.set_ball_position(position);
        Vector3f newPos = new Vector3f((float)position.get_x(), (float)simulator.getCourse().get_height().evaluate(position), (float)position.get_z());
        uiPlayer.setPosition(newPos);
    }

    private void getShotDataFromFile() {
        Thread t = new Thread(() -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load shots from file!");
            JFrame f = new JFrame();
            f.setVisible(true);
            f.toFront();
            f.setVisible(false);
            int res = chooser.showOpenDialog(f);
            f.dispose();
            if (res == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = chooser.getSelectedFile();
                try {
                    parseShotsFileToVectors(new BufferedReader(new FileReader(fileToLoad)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    private void parseShotsFileToVectors(BufferedReader reader) throws IOException {
        List<Vector3d> shotVectors = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "");
            String[] data = line.split(",");
            shotVectors.add(new Vector3d(Double.parseDouble(data[0]), Double.parseDouble(data[1])));
        }
        this.shotsInFile = shotVectors.toArray(new Vector3d[0]);
    }

    public UIPlayer getUiPlayer() {
        return uiPlayer;
    }

    public String getType() {
        return type;
    }

}
