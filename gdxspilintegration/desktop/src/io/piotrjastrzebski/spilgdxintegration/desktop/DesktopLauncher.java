package io.piotrjastrzebski.spilgdxintegration.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.desktop.DesktopSpilSdk;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpilGame(new DesktopSpilSdk(), null), config);
	}
}
