package com.spilgames.libgdxbridge.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.html.HtmlSpilSdk;

public class HtmlLauncher extends GwtApplication {
	private static final String TAG = HtmlLauncher.class.getSimpleName();

	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(800, 500);
	}

	@Override
	public ApplicationListener createApplicationListener () {
		setLogLevel(LOG_DEBUG);
		// TODO need to get this stuff from config of some sort?
		return new SpilGame(new HtmlSpilSdk("com.spilgames.slot", "0.0.2", "prd", "spil-sdk.js"), null);
	}
}
