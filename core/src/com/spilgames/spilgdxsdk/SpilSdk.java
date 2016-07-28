package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Common interface for all backends
 *
 * Created by PiotrJ on 01/07/16.
 */
public interface SpilSdk {
	String PROVIDER_CHARTBOOST = "ChartBoost";
	String PROVIDER_DFP = "DFP";
	String PROVIDER_FYBER = "Fyber";
	String AD_INTERSTITIAL = "interstitial";
	String AD_REWARD_VIDEO = "rewardVideo";
	String AD_MORE_APPS = "moreApps";

	SpilSdkType getBackendType ();

	void setDebug(boolean debug);
	void trackEvent(SpilEvent event);
	void trackEvent(SpilEvent event, SpilEventActionListener listener);

	JsonValue getConfig ();

	// Ads
	// TODO this is kind janky
	void startChartboost (String appId, String appSignature);
	void startFyber (String appId, String token);
	void startDFP (String adUnitId);
	void showRewardVideo ();
	void showMoreApps ();

	void registerDevice(String device);

	void setSpilAdCallbacks(SpilAdCallbacks adCallbacks);

	// packages

	void requestPackages();

	JsonValue getAllPackages();

	JsonValue getPackage(String packageId);

	JsonValue getPromotion(String packageId);

	void setSpilRewardListener(SpilRewardListener rewardListener);

	// wallet

	// dev
	void devRequestAd(String provider, String adType, boolean parentalGate);
	void devShowRewardVideo (String provider);
	void devShowInterstitial(String provider);
	void devShowMoreApps (String provider);
}
