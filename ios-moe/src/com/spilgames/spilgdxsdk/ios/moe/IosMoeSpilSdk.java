package com.spilgames.spilgdxsdk.ios.moe;

import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.SpilEvent;
import com.spilgames.spilgdxsdk.SpilEventActionListener;
import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilgdxsdk.SpilSdkType;

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

	}
}
