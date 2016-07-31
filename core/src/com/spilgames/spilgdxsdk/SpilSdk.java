package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Common interface for all backends
 *
 * TODO comment the api
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
	boolean isAdProviderInitialized(String provider);

	void showRewardVideo ();
	void showMoreApps ();

	void setSpilAdCallbacks(SpilAdCallbacks adCallbacks);

	// ads dev
	void devRequestAd(String provider, String adType, boolean parentalGate);
	void devShowRewardVideo (String provider);
	void devShowInterstitial(String provider);
	void devShowMoreApps (String provider);

	// packages

	void requestPackages();

	JsonValue getAllPackages();

	JsonValue getPackage(String packageId);

	JsonValue getPromotion(String packageId);

	void setSpilRewardListener(SpilRewardListener rewardListener);

	// wallet
	void requestPlayerData();
	void requestGameData();
	void setSpilGameDataListener(SpilGameDataListener gameDataListener);
	void setSpilPlayerDataListener(SpilPlayerDataListener playerDataListener);

	JsonValue getUserProfile();
	JsonValue getWallet();
	JsonValue getGameData();
	JsonValue getInventory();

	void addCurrencyToWallet(int currencyId, int amount, String reason);
	void subtractCurrencyFromWallet(int currencyId, int amount, String reason);

	void addItemToInventory(int itemId, int amount, String reason);
	void subtractItemFromInventory(int itemId, int amount, String reason);

	void consumeBundle(int bundleId, String reason);
}
