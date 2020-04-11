package ui;

import org.joml.Vector3f;
import ui.entities.FlyingCamera;
import ui.renderEngine.GameMap;
import ui.renderEngine.MasterRenderer;
import ui.renderEngine.Window;
import ui.toolbox.MousePicker;

public class MapEditorScreen {

    private boolean isDoneEditing = true;
    private final GameScreen gameScreen;
    private final GameMap map;

    private final FlyingCamera camera = new FlyingCamera(Window.getWindow());
    private final MousePicker picker;

    public MapEditorScreen(GameScreen gameScreen, MasterRenderer renderer) {
        this.gameScreen = gameScreen;
        this.map = gameScreen.getTerrainMap();
        this.picker = new MousePicker(camera, renderer.getProjectionMatrix(), gameScreen.getTerrainMap().getMasterTerrain());
    }

    public void render(MasterRenderer renderer) {
        gameScreen.renderMap(renderer, camera);
        camera.move();

//                     ----------- Ray Tracing Update -----------
        picker.update();
        Vector3f terrainPoint = picker.getCurrentTerrainPoint();
        if (terrainPoint != null) {
            map.getAllEntities().get(1).setPosition(terrainPoint);
        }
//                      ----------- Ray Tracing Update -----------

    }

    public boolean isDoneEditing() {
        return isDoneEditing;
    }
}
