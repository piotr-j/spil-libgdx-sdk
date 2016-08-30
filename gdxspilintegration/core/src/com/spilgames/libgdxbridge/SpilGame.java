package com.spilgames.libgdxbridge;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;
import com.spilgames.libgdxbridge.screens.MainScreen;
import com.spilgames.spilgdxsdk.SpilConfigDataListener;
import com.spilgames.spilgdxsdk.SpilErrorCode;
import com.spilgames.spilgdxsdk.SpilGameDataListener;
import com.spilgames.spilgdxsdk.SpilSdk;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class SpilGame extends Game {
	private final static String TAG = SpilGame.class.getSimpleName();

	public SpriteBatch batch;
	public SpilSdk spilSdk;
	public PlatformBridge bridge;

	public SpilGame () {
		this(null);
	}

	public SpilGame (PlatformBridge bridge) {
		this.bridge = bridge;
	}

	public SpilGame (SpilSdk spilSdk, PlatformBridge bridge) {
		this.spilSdk = spilSdk;
		this.bridge = bridge;
	}

	@Override public void create () {
		// required so we get logging on HTML
		Gdx.app.setLogLevel(Application.LOG_INFO);
		if (bridge != null) bridge.onCreate();
		spilSdk.setDebug(true);

		batch = new SpriteBatch();
		// TODO handle scaling
		if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
			VisUI.load(VisUI.SkinScale.X2);
		} else {
			VisUI.load(VisUI.SkinScale.X1);
		}
		setScreen(new MainScreen(this));

		String spilUserId = spilSdk.getSpilUserID();
		Gdx.app.log(TAG, "Spil User ID = " + spilUserId);
		String userId = spilSdk.getUserID();
		Gdx.app.log(TAG, "User ID = " + userId);
		spilSdk.setSpilConfigLDataListener(new SpilConfigDataListener() {
			@Override public void configDataUpdated () {
				Gdx.app.log(TAG, "configDataUpdated");
			}
		});
	}

	@Override public void dispose () {
		super.dispose();
		VisUI.dispose();
		batch.dispose();
	}
}
