package com.spilgames.spilgdxsdk.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.*;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class HtmlSpilSdk implements SpilSdk {
	private final static String TAG = HtmlSpilSdk.class.getSimpleName();

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.HTML;
	}

	@Override public void registerDevice (String projectID) {
		Gdx.app.log(TAG, "registerDevice ("+projectID+")");
	}

	@Override public void setDebug (boolean debug) {

	}

	@Override public void trackEvent (SpilEvent event) {

	}

	@Override public void trackEvent (SpilEvent event, SpilEventActionListener listener) {

	}

	@Override public JsonValue getConfig () {
		return null;
	}

	@Override public void requestPackages () {
		Gdx.app.log(TAG, "requestPackages");
	}

	@Override public void setSpilRewardListener (SpilRewardListener rewardListener) {
		Gdx.app.log(TAG, "SpilRewardListener ("+rewardListener+")");
	}

	@Override public JsonValue getPromotion (String packageId) {
		Gdx.app.log(TAG, "getPromotion");
		return null;
	}

	@Override public JsonValue getPackage (String packageId) {
		Gdx.app.log(TAG, "getPackage");
		return null;
	}

	@Override public JsonValue getAllPackages () {
		Gdx.app.log(TAG, "getAllPackages");
		return null;
	}

	@Override public void startChartboost (String appId, String appSignature) {

	}

	@Override public void startFyber (String appId, String token) {
		Gdx.app.log(TAG, "startFyber ("+appId+", "+token+")");
	}

	@Override public void startDFP (String adUnitId) {
		Gdx.app.log(TAG, "startDFP ("+adUnitId+")");
	}

	@Override public void showMoreApps () {

	}

	@Override public void showRewardVideo () {

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
