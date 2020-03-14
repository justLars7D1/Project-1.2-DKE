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

    GAME {
        public AbstractScreen getScreen(Object ... params) {
            return new GameScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object ... params);

}
