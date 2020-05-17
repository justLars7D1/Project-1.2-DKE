package ui;

import lwjgui.LWJGUI;
import lwjgui.scene.control.Button;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.floating.FloatingPane;
import lwjgui.style.BackgroundNVGImage;
import org.lwjgl.opengl.GL30;
import ui.renderEngine.Window;

import java.util.HashMap;

public class GameModeScreen {

    private static final BackgroundImg backgroundImage = new BackgroundImg("./res/gamemodeselection/mainbackground.png");
    private final StackPane pane;

    private String selectedGameMode = "";

    HashMap<String, Button> gameModeBtns;

    private boolean doneSelecting;

    public GameModeScreen() {
        this.gameModeBtns = new HashMap<>();
        this.pane = new StackPane();
    }

    /**
     * Create the screen
     * @param guiWindow The window to create the screen on
     */
    public void create(lwjgui.scene.Window guiWindow) {
        guiWindow.getScene().setRoot(pane);

        FloatingPane floatingPane = new FloatingPane();
        this.pane.getChildren().add(floatingPane);
        floatingPane.setAbsolutePosition(Window.getWidth()/2f - floatingPane.getWidth()/2, 0.245 * Window.getHeight());

        setupButtons(floatingPane);

        this.gameModeBtns.get("single_player").setOnMouseClicked(e -> {
            this.selectedGameMode = "single_player";
            this.doneSelecting = true;
        });

        this.gameModeBtns.get("multi_player").setOnMouseClicked(e -> {
            this.selectedGameMode = "multi_player";
            this.doneSelecting = true;
        });

        this.gameModeBtns.get("load_file_mode").setOnMouseClicked(e -> {
            this.selectedGameMode = "load_file_mode";
            this.doneSelecting = true;
        });

        this.gameModeBtns.get("bot_mode").setOnMouseClicked(e -> {
            this.selectedGameMode = "bot_mode";
            this.doneSelecting = true;
        });

        pane.setBackground(new BackgroundNVGImage(backgroundImage.getImage()));

    }

    private void setupButtons(FloatingPane floatingPane) {
        String[] buttonNames = {"single_player", "multi_player", "load_file_mode", "bot_mode"};
        String[] initialValues = {"Single player", "Multi-player", "Load from file mode", "Player vs bot"};
        for (int i = 0; i < buttonNames.length; i++) {
            FloatingPane t = new FloatingPane();
            if (i != buttonNames.length-1) {
                t.setAbsolutePosition(0.15 * Window.getWidth() + 0.25 * i * Window.getWidth(), 0.35 * Window.getHeight());
            } else {
                t.setAbsolutePosition(0.15 * Window.getWidth() + 0.25 * Window.getWidth(), 0.625 * Window.getHeight());
            }
            floatingPane.getChildren().add(t);
            Button btn = new Button(initialValues[i]);
            gameModeBtns.put(buttonNames[i], btn);
            btn.setPrefSize(300, 150);
            btn.setFontSize(30);
            t.getChildren().add(btn);
        }
    }

    public void update() {
        finish();
        LWJGUI.render();
    }

    public void finish() {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(0, 0, 0, 1);
    }

    public boolean isDoneSelecting() {
        return doneSelecting;
    }

    public String getSelectedGameMode() {
        return selectedGameMode;
    }

    public void setDoneSelecting(boolean doneSelecting) {
        this.doneSelecting = doneSelecting;
    }
}
