package com.spilgames.libgdxbridge.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilEvent;
import com.spilgames.spilgdxsdk.SpilEventActionListener;
import com.spilgames.spilgdxsdk.SpilResponseEvent;
import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilgdxsdk.html.HtmlSpilSdk;
import com.spilgames.spilgdxsdk.html.bindings.JsUtils;

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
		final HtmlSpilSdk sdk = new HtmlSpilSdk("com.spilgames.slot", "0.0.2", "prd", new HtmlSpilSdk.SpilSdkLoadedCallback() {
			@Override public void loaded (SpilSdk sdk) {
				// pretty sure the gdx might not be initialized at this point :/
				// we are running local, so its instant, but on the web it might be after we init the game itself
				if (Gdx.app != null) {
					Gdx.app.log(TAG, "GDX Spil sdk loaded!");
				} else {
					JsUtils.log(TAG, "JS Spil sdk loaded!");
					sdk.trackEvent(new SpilEvent("welp1"));
					sdk.trackEvent(new SpilEvent("welp2"), new SpilEventActionListener() {
						@Override public void onResponse (SpilResponseEvent response) {
							JsUtils.log(TAG, "Tracked event! " + response);
						}
					});
				}
			}
		});
		return new SpilGame(sdk, null);
	}
}
