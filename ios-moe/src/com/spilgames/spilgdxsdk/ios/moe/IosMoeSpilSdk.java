package com.spilgames.spilgdxsdk.ios.moe;

import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.*;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class IosMoeSpilSdk implements SpilSdk {
	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_MOE;
	}

	@Override public void setDebug (boolean debug) {

	}

	@Override public void trackEvent (SpilEvent event) {

	}

	@Override public void trackEvent (SpilEvent event, SpilEventActionListener listener) {

	}

	@Override public void onCreate () {

	}

	@Override public void onStart () {

	}

	@Override public void onResume () {

	}

	@Override public void onPause () {

	}

	@Override public void onDestroy () {

	}

	@Override public void onBackPressed () {

	}

	@Override public ObjectMap<String, String> getConfigAll () {
		return null;
	}

	@Override public String getConfigValue (String key) {
		return null;
	}

	@Override public void startChartboost (String appId, String appSignature) {
		Gdx.app.log(TAG, "startChartboost ("+appId+", "+appId+")");
	}

	@Override public void startFyber (String appId, String token) {
		Gdx.app.log(TAG, "startFyber ("+appId+", "+token+")");
	}

	@Override public void startDFP (String adUnitId) {
		Gdx.app.log(TAG, "startDFP ("+adUnitId+")");
	}

	@Override public void setSpilAdCallbacks (SpilAdCallbacks adCallbacks) {

	}

	@Override public void devRequestAd (String provider, String adType, boolean parentalGate) {

	}

	@Override public void devShowRewardVideo (String provider) {

	}

	@Override public void devShowInterstitial (String provider) {

	}

	@Override public void devShowMoreApps (String provider) {

	}
}
