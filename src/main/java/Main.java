import gameelements.PuttingSimulator;
import lwjgui.LWJGUI;
import org.lwjgl.opengl.GL;
import ui.*;
import ui.fontRendering.TextMaster;
import ui.renderEngine.Loader;
import ui.renderEngine.MasterRenderer;
import ui.renderEngine.Window;

public class Main {

    public static void main(String[] args) {

        //Create the window
        Window.create();
        GL.createCapabilities();

        //Create the loader that will load all textures/models
        Loader loader = new Loader();

        //Create a renderer for the graphical text elements
        TextMaster.init(loader);

        //Create the renderers that will render the models
        MasterRenderer renderer = new MasterRenderer(loader);

//        text.setColour(0, 0, 0);

//        GuiTexture refraction = new GuiTexture(fbos.getRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//        GuiTexture reflection = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//
//        List<GuiTexture> ui.guis = new ArrayList<>();
//        ui.guis.add(reflection);
//        ui.guis.add(refraction);


        lwjgui.scene.Window guiWindow = LWJGUI.initialize(Window.getWindow());

        LoadingScreen loadingScreen = new LoadingScreen(loader);
        CourseDesignerScreen designerScreen = new CourseDesignerScreen();
        GameModeScreen gameModeScreen = new GameModeScreen();
        MapEditorScreen mapEditorScreen = null;
        GameScreen gameScreen = null;

        PuttingSimulator puttingSimulator = null;
        String gameMode;
        String solverType = "RK4";

        //This is the loop that runs the entire game
        while (!Window.closed()) {

            //While loop for the loading screen
            while (!loadingScreen.isLoadingFinished() && !Window.closed()) {
                loadingScreen.update();
                Window.update();
                Window.swapBuffers();
                if (loadingScreen.isLoadingFinished()) {
                    designerScreen.create(guiWindow);
                    loadingScreen.finish();
                }
            }

            //While loop for the course designer screen
            while (!designerScreen.isDoneSelecting() && !Window.closed()) {
                designerScreen.update();
                if (designerScreen.isDoneSelecting()) {
                    puttingSimulator = designerScreen.createSimulator(solverType);
                    designerScreen.finish();
                    gameModeScreen.create(guiWindow);
                }
            }

            //While loop for the game mode selector
            while (!gameModeScreen.isDoneSelecting() && !Window.closed()) {
                gameModeScreen.update();
                if (gameModeScreen.isDoneSelecting()) {
                    gameMode = gameModeScreen.getSelectedGameMode();
                    gameModeScreen.finish();
                    designerScreen.create(guiWindow);
                    gameScreen = new GameScreen(puttingSimulator, gameMode, loader, renderer);
                    mapEditorScreen = new MapEditorScreen(gameScreen, renderer);
                }
            }

            //While loop for the terrain editor (sand banks, etc...)
            while(!mapEditorScreen.isDoneEditing() && !Window.closed()) {
                mapEditorScreen.render();
            }


            //TODO: Now, set up the game!
            while (!((gameScreen != null && gameScreen.isGameOver()) || Window.closed())) {
                gameScreen.render(renderer);
                TextMaster.render();

                Window.update();
                Window.swapBuffers();
            }

            designerScreen.setDoneSelecting(false);
            gameModeScreen.setDoneSelecting(false);

            loadingScreen.cleanUp();
            loader.cleanUp();
            gameScreen.cleanUp();
            renderer.cleanUp();
            TextMaster.cleanUp();

            mapEditorScreen = null;
            puttingSimulator = null;
            gameScreen = null;

            renderer = new MasterRenderer(loader);

        }

        guiWindow.dispose();
        loadingScreen.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        TextMaster.cleanUp();

    }

}
