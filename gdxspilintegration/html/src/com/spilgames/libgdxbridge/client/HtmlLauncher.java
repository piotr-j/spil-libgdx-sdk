package com.spilgames.libgdxbridge.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.html.HtmlSpilSdk;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(800, 600);
	}

	@Override
	public ApplicationListener createApplicationListener () {
		setLogLevel(LOG_DEBUG);
		return new SpilGame(new HtmlSpilSdk(), null);
	}
}
