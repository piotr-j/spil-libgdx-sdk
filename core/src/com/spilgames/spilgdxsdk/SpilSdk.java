package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Common interface for all backends
 *
 * Created by PiotrJ on 01/07/16.
 */
public interface SpilSdk {
	SpilSdkType getBackendType ();

	void setDebug(boolean debug);
	void trackEvent(SpilEvent event);
	void trackEvent(SpilEvent event, SpilEventActionListener listener);

	ObjectMap<String, String> getConfigAll();
	String getConfigValue(String key);

	// TODO do we want those?
	void onCreate ();
	void onStart ();
	void onResume ();
	void onPause ();
	void onDestroy ();
	void onBackPressed ();

	// Ads

	void startChartboost (String appId, String appSignature);

	void setSpilAdCallbacks(SpilAdCallbacks adCallbacks);

	// dev
	void devRequestAd(String provider, String adType, boolean parentalGate);
	void devShowRewardVideo (String provider);
	void devShowInterstitial(String provider);
	void devShowMoreApps (String provider);
}
