package com.spilgames.spilgdxsdk.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.*;

/**
 * Dummy implementation for desktop
 *
 * Created by PiotrJ on 01/07/16.
 */
public class DesktopSpilSdk implements SpilSdk {
	private final static String TAG = DesktopSpilSdk.class.getSimpleName();

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.DESKTOP;
	}

	@Override public void setDebug (boolean debug) {
		Gdx.app.log(TAG, "setDebug ("+debug+")");
	}

	@Override public void trackEvent (SpilEvent event) {
		Gdx.app.log(TAG, "trackEvent ("+event+")");
	}

	@Override public void trackEvent (SpilEvent event, SpilEventActionListener listener) {
		Gdx.app.log(TAG, "trackEvent ("+event+", "+listener+")");
	}

	@Override public void onCreate () {
		Gdx.app.log(TAG, "onCreate");
	}

	@Override public void onStart () {
		Gdx.app.log(TAG, "onStart");
	}

	@Override public void onResume () {
		Gdx.app.log(TAG, "onResume");
	}

	@Override public void onPause () {
		Gdx.app.log(TAG, "onPause");
	}

	@Override public void onDestroy () {
		Gdx.app.log(TAG, "onDestroy");
	}

	@Override public void onBackPressed () {
		Gdx.app.log(TAG, "onBackPressed");
	}

	@Override public ObjectMap<String, String> getConfigAll () {
		return new ObjectMap<>();
	}

	@Override public String getConfigValue (String key) {
		return null;
	}

	@Override public void startChartboost (String appId, String appSignature) {
		Gdx.app.log(TAG, "startChartboost ("+appId+", "+appSignature+")");
	}

	@Override public void setSpilAdCallbacks (SpilAdCallbacks adCallbacks) {
		Gdx.app.log(TAG, "setSpilAdCallbacks ("+adCallbacks+")");
	}

	@Override public void devRequestAd (String provider, String adType, boolean parentalGate) {
		Gdx.app.log(TAG, "devRequestAd ("+provider+", "+adType+", "+parentalGate+")");
	}

	@Override public void devShowRewardVideo (String provider) {
		Gdx.app.log(TAG, "devShowRewardVideo ("+provider+")");
	}

	@Override public void devShowInterstitial (String provider) {
		Gdx.app.log(TAG, "devShowInterstitial ("+provider+")");
	}

	@Override public void devShowMoreApps (String provider) {
		Gdx.app.log(TAG, "devShowMoreApps ("+provider+")");
	}
}
