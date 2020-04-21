package ui;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import physicsengine.Vector3d;
import ui.entities.Camera;
import ui.entities.PlayerCamera;
import ui.entities.Light;
import ui.fontMeshCreator.FontType;
import ui.fontMeshCreator.GUIText;
import ui.fontRendering.TextMaster;
import ui.renderEngine.GameMap;
import ui.renderEngine.Loader;
import ui.renderEngine.MasterRenderer;
import ui.renderEngine.Window;
import ui.status.StatusMessage;
import ui.water.WaterRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameScreen {

    private final FontType font;
    private GUIText velocityText;
    private double ballVelocity;

    private boolean gameOver;

    private final List<Light> lights = new ArrayList<>();
    private final WaterRenderer waterRenderer;

    private final PuttingSimulator gameSimulator;
    private final PuttingCourse gameCourse;

    private PlayerCamera playerCamera;
    private int currentCameraPlayerFocus;
    private final List<GamePlayer> players;
    private GUIText numTurnsText;

    private GameMap terrainMap;

    public GameScreen(PuttingSimulator gameSimulator, String gamemode, Loader loader, MasterRenderer renderer) {
        this.font = new FontType(loader.loadTexture("skins/arial"), new File("./res/skins/arial.fnt"));
        this.velocityText = new GUIText("Velocity: " + ballVelocity + " m/s", 1, font, new Vector2f(0.65f, 0.05f), 0.5f, true);
        this.players = new ArrayList<>();
        this.gameSimulator = gameSimulator;
        this.gameCourse = gameSimulator.getCourse();
        createMap(loader);
        createFlag();
        setupPlayers(gamemode, loader);
        String turnText = "Player " + (currentCameraPlayerFocus+1) + ". Turn" + (players.get(currentCameraPlayerFocus).getNumMovesMade()+1);
        this.numTurnsText = new GUIText(turnText, 1, font, new Vector2f(0.65f, 0.10f), 0.5f, true);
        //Create a sun
        Light sun = new Light(new Vector3f(Integer.MAX_VALUE,Integer.MAX_VALUE, Integer.MAX_VALUE), new Vector3f(1, 1, 1));
        lights.add(sun);
        this.waterRenderer = new WaterRenderer(loader, terrainMap.getWaterShader(), renderer.getProjectionMatrix(), terrainMap.getFbos());
    }

    private void takeShot() {
        GamePlayer currentShooter = players.get(currentCameraPlayerFocus);
        Vector3d oldPosition = currentShooter.getPosition();
        Vector3d shot;
        if (currentShooter.getType().equals("user")) {
            //Calculate the direction (and magnitude) of the shot
            Vector3d curBallPos = currentShooter.getPosition();
            Vector3d curCamPos = new Vector3d(playerCamera.getPosition().x, playerCamera.getPosition().z);
            Vector3d normDirection = curBallPos.minus(curCamPos).getNormalized();
            normDirection.scale(ballVelocity);
            shot = normDirection;
        } else {
            //Let the bot shoot
            shot = new Vector3d(0,0);
        }
        //Take the shot

        currentShooter.takeShot(shot);
        //System.out.println(currentShooter.getPosition());
        if (StatusMessage.isInWater()) {
            //Handle ball landing in water
            System.out.println("Ball landed in water!");
            currentShooter.setPosition(oldPosition);
        } else if (StatusMessage.isFinished()) {
            //Handle game is over since target is reached
            System.out.println("TARGET REACHED!");
            this.gameOver = true;
        }
        try {
            Thread.sleep(2000);
            nextPlayer();
            isShooting = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isShooting = false;
    public void render(MasterRenderer renderer) {

        if (!isShooting) {
            TextMaster.removeText(this.numTurnsText);
            String turnText = "Player " + (currentCameraPlayerFocus+1) + ". Turn " + (players.get(currentCameraPlayerFocus).getNumMovesMade()+1);
            this.numTurnsText = new GUIText(turnText, 1, font, new Vector2f(0.65f, 0.10f), 0.5f, true);
        }

        playerCamera.move();

        terrainMap.renderMap(renderer, waterRenderer, playerCamera, lights);

        if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS && !isShooting) {
            isShooting = true;
            new Thread(this::takeShot).start();
        } else if (GLFW.glfwGetKey(Window.getWindow(), GLFW. GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            gameOver = true;
        } else if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            double factor = 0.02 * (gameCourse.get_maximum_velocity()/3);
            if (ballVelocity + factor <= gameCourse.get_maximum_velocity()) {
                ballVelocity += factor;
            } else {
                ballVelocity = gameCourse.get_maximum_velocity();
            }
            TextMaster.removeText(velocityText);
            velocityText = new GUIText(String.format("Velocity: %.2f m/s", ballVelocity), 1, font, new Vector2f(0.65f, 0.05f), 0.5f, true);
        } else if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            double factor = 0.02 * (gameCourse.get_maximum_velocity()/3);
            if (ballVelocity - factor >= 0) {
                ballVelocity -= factor;
            } else {
                ballVelocity = 0;
            }
            TextMaster.removeText(velocityText);
            velocityText = new GUIText(String.format("Velocity: %.2f m/s", ballVelocity), 1, font, new Vector2f(0.65f, 0.05f), 0.5f, true);
        }

    }

    private void nextPlayer() {
        if (++currentCameraPlayerFocus >= players.size()) currentCameraPlayerFocus = 0;
        playerCamera.switchPlayer(players.get(currentCameraPlayerFocus).getUiPlayer());
    }

    private void createMap(Loader loader) {
        Vector3d ballPos = gameCourse.get_start_position();
        Vector3d flagPos = gameCourse.get_flag_position();
        int minX = (int) Math.round(Math.min(ballPos.get_x(), flagPos.get_x()));
        int maxX = (int) Math.round(Math.max(ballPos.get_x(), flagPos.get_x()));
        int minZ = (int) Math.round(Math.min(ballPos.get_z(), flagPos.get_z()));
        int maxZ = (int) Math.round(Math.max(ballPos.get_z(), flagPos.get_z()));
        this.terrainMap = new GameMap(minX - 20, minZ - 20, maxX + 20, maxZ + 20, gameCourse.get_height(), loader);
    }

    private void createFlag() {
        Vector3d flagPos = gameCourse.get_flag_position();
        float targetLightHeight = (float)(gameCourse.get_height().evaluate(flagPos) + 1);
        terrainMap.generateEntityAtPosition("game/entities/flag/flag", "game/entities/bunny/white", (float)flagPos.get_x() + 1.8f, (float)flagPos.get_z(), 0, 1, 3f, 1);
        lights.add(new Light(new Vector3f((float) flagPos.get_x(), targetLightHeight, (float) flagPos.get_z()), new Vector3f(0, 0, 5), new Vector3f(.1f, .1f, .1f)));
    }

    private void setupPlayers(String gamemode, Loader loader) {
        Vector3d startPos2d = gameCourse.get_start_position();
        float startPosHeight = (float)(gameCourse.get_height().evaluate(startPos2d));
        Vector3f startPosition = new Vector3f((float) startPos2d.get_x(), startPosHeight, (float) startPos2d.get_z());
        switch (gamemode) {
            case "single_player":
                GamePlayer p = new GamePlayer("user", loader, startPosition, terrainMap, gameSimulator.copy());
                players.add(p);
                break;
            case "multi_player":
                GamePlayer p1 = new GamePlayer("user", loader, startPosition, terrainMap, gameSimulator.copy());
                GamePlayer p2 = new GamePlayer("user", loader, new Vector3f(startPosition), terrainMap, gameSimulator.copy());
                players.add(p1);
                players.add(p2);
                break;
            case "load_file_mode":
                GamePlayer p3 = new GamePlayer("file", loader, startPosition, terrainMap, gameSimulator.copy());
                players.add(p3);
                break;
            case "bot_mode":
                GamePlayer p4 = new GamePlayer("user", loader, startPosition, terrainMap, gameSimulator.copy());
                GamePlayer p5 = new GamePlayer("bot", loader, new Vector3f(startPosition), terrainMap, gameSimulator.copy());
                players.add(p4);
                players.add(p5);
                break;

        }
        this.playerCamera = new PlayerCamera(players.get(0).getUiPlayer(), Window.getWindow());
        this.currentCameraPlayerFocus = 0;
    }

    public void cleanUp() {
        terrainMap.cleanUp();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameMap getTerrainMap() {
        return terrainMap;
    }

    //For the map editor
    public void renderMap(MasterRenderer renderer, Camera camera) {
        terrainMap.renderMap(renderer, waterRenderer, camera, lights);
    }

    public void setCallback() {
        playerCamera.setCallback();
    }

}
