package com.spilgames.libgdxbridge.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.html.HtmlSpilSdk;

public class HtmlLauncher extends GwtApplication {
	private static final String TAG = HtmlLauncher.class.getSimpleName();

	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(800, 600);
	}

	@Override
	public ApplicationListener createApplicationListener () {
		setLogLevel(LOG_DEBUG);
		// TODO need to get this stuff from config of some sort?
		// TODO do we even want to pass this in?
		HtmlSpilSdk sdk = new HtmlSpilSdk("com.spilgames.slot", "0.0.2", "stg", new HtmlSpilSdk.SpilSdkLoadedCallback() {
			@Override public void loaded () {
				Gdx.app.log(TAG, "Spil sdk loaded!");
			}
		});
		return new SpilGame(sdk, null);
	}
}
