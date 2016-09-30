package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.Array;
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

	// Event
	void trackEvent(SpilEvent event);
	void trackEvent(SpilEvent event, SpilEventActionListener listener);

	Track track();

	JsonValue getConfig ();
	void setSpilConfigLDataListener (SpilConfigDataListener listener);

	// Ads
	boolean isAdProviderInitialized(String provider);

	void requestRewardVideo ();
	void requestMoreApps ();

	void showRewardVideo ();
	void showMoreApps ();

	void setSpilAdListener (SpilAdListener adListener);

	void showToastOnVideoReward(boolean enabled);

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

	void setSpilNotificationDataListener (SpilNotificationDataListener rewardListener);

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

	// user data
	String getSpilUserID();
	String getUserID();
	String getUserProvider();

	void requestOtherUsersGameState(String provider, Array<String> userIdsList);

	void setUserID(String providerId, String userId);

	String getPublicGameState();
	void setPublicGameState(String publicGameState);

	String getPrivateGameState();
	void setPrivateGameState(String privateGameState);

	void setSpilGameStateListener (SpilGameStateListener gameStateListener);

	// customer support
	void showZendeskHelpCenter();
	void showZendeskWebViewHelpCenter();
	void showZendeskContactCenter();

	// Automated events
	void setSpilAutomatedEventsListener (SpilAutomatedEventsListener automatedEventsListener);
}
