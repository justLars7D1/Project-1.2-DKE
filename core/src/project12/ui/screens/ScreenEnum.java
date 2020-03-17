package project12.ui.screens;

public enum ScreenEnum {

    LOADING {
        public AbstractScreen getScreen(Object ... params) {
            return new LoadingScreen();
        }
    },

    MAIN {
        public AbstractScreen getScreen(Object ... params) {
            return new MainMenuScreen();
        }
    },

    DESIGNER {
        public AbstractScreen getScreen(Object ... params) {
            return new CourseDesignerScreen();
        }
    },

    GAMEMODE {
        public AbstractScreen getScreen(Object ... params) {
            return new GameModeSelectionScreen();
        }
    },

    GAME {
        public AbstractScreen getScreen(Object ... params) {
            return new GameScreen(params);
        }
    };

    public abstract AbstractScreen getScreen(Object ... params);

}
