package project12.ui;

import com.badlogic.gdx.Game;
import project12.ui.screens.ScreenEnum;
import project12.ui.screens.ScreenManager;

// Adding a fade-in of a group logo and then showing a main menu with options:
// START, COURSE DESIGNER, LOAD COURSE, SETTINGS
// would be cool to implement
public class Application extends Game {

	@Override
	public void create() {
		ScreenManager.getInstance().init(this);
		ScreenManager.getInstance().setScreen(ScreenEnum.LOADING);
	}

}
