package project12.ui.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import project12.ui.Application;

public class DesktopLauncher {

	private static final float screenSizeFactor = 0.8f;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) (1920 * screenSizeFactor);
		config.height = (int) (1080 * screenSizeFactor);
		config.resizable = false;
		new LwjglApplication(new Application(), config);
	}
}
