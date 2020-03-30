package ui;

import gameelements.PuttingSimulator;
import org.joml.Vector3f;
import physicsengine.Vector2d;
import ui.entities.Camera;
import ui.entities.UIPlayer;
import ui.models.RawModel;
import ui.models.TexturedModel;
import ui.objloader.ModelData;
import ui.objloader.OBJLoader;
import ui.renderEngine.GameMap;
import ui.renderEngine.Loader;
import ui.textures.ModelTexture;

public class GamePlayer {

    private final UIPlayer uiPlayer;
    private final String type;

    private final PuttingSimulator simulator;

    private int numMovesMade;

    public GamePlayer(String type, Loader loader, Vector3f position, GameMap map, PuttingSimulator simulator) {
        this.simulator = simulator;
        this.type = type;
        this.uiPlayer = createPlayer(loader);
        uiPlayer.setPosition(position);
        map.addPlayers(uiPlayer);
    }

    public void takeShot(Vector2d ballVelocity) {
        this.numMovesMade++;
        simulator.take_shot(ballVelocity, uiPlayer);
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

    public Vector2d getPosition() {
        return simulator.get_ball_position();
    }

    public void setPosition(Vector3f position) {
        this.uiPlayer.setPosition(position);
        this.simulator.set_ball_position(new Vector2d(position.x, position.z));
    }

    public void setPosition(Vector2d position) {
        simulator.set_ball_position(position);
        Vector3f newPos = new Vector3f((float)position.get_x(), (float)simulator.getCourse().get_height().evaluate(position), (float)position.get_y());
        uiPlayer.setPosition(newPos);
    }

    public UIPlayer getUiPlayer() {
        return uiPlayer;
    }

    public String getType() {
        return type;
    }

}
