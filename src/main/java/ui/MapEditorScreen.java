package ui;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import physicsengine.Vector3d;
import ui.entities.Camera;
import ui.entities.Light;
import ui.fontMeshCreator.FontType;
import ui.fontMeshCreator.GUIText;
import ui.fontRendering.TextMaster;
import ui.guis.GuiRenderer;
import ui.renderEngine.GameMap;
import ui.renderEngine.Loader;
import ui.renderEngine.MasterRenderer;
import ui.renderEngine.Window;
import ui.status.StatusMessage;
import ui.toolbox.MousePicker;
import ui.water.WaterRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapEditorScreen {

    private boolean isDoneEditing;
    private final GameScreen gameScreen;

    private final FlyingCamera camera = new FlyingCamera();
    private final MousePicker picker;

    public MapEditorScreen(GameScreen gameScreen, MasterRenderer renderer) {
        this.gameScreen = gameScreen;
        this.picker = new MousePicker(camera, renderer.getProjectionMatrix(), gameScreen.getTerrainMap().getMasterTerrain());
    }

    public void render(MasterRenderer renderer) {
        gameScreen.renderMap(renderer);

//                     ----------- Ray Tracing Update -----------
        picker.update();
        Vector3f terrainPoint = picker.getCurrentTerrainPoint();
        if (terrainPoint != null) {
            System.out.println(terrainPoint);
        }
//                      ----------- Ray Tracing Update -----------

    }

    public boolean isDoneEditing() {
        return isDoneEditing;
    }
}
