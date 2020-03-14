package project12.ui.screens;

import com.badlogic.gdx.Screen;
import project12.ui.Application;

public class ScreenManager {

    private static ScreenManager manager;
    private Application application;

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (manager == null) {
            manager = new ScreenManager();
        }
        return manager;
    }

    public void init(Application app) {
        this.application = app;
    }

    public void setScreen(ScreenEnum screenEnum, Object ... params) {

        Screen curScreen = application.getScreen();
        AbstractScreen newScreen = screenEnum.getScreen(params);

        newScreen.buildStage();
        application.setScreen(newScreen);

        if (curScreen != null) curScreen.dispose();

    }

}
