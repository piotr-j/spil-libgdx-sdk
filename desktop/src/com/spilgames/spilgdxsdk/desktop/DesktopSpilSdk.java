package com.spilgames.spilgdxsdk.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.spilgames.spilgdxsdk.*;

/**
 * Dummy implementation for desktop
 *
 * Created by PiotrJ on 01/07/16.
 */
public class DesktopSpilSdk implements SpilSdk {
	private final static String TAG = DesktopSpilSdk.class.getSimpleName();
	private boolean log;

	public DesktopSpilSdk() {
		this(true);
	}

	public DesktopSpilSdk(boolean loggingEnabled) {
		setLogging(loggingEnabled);
	}

	public void setLogging (boolean enabled) {
		this.log = enabled;
	}

	public boolean isLogging () {
		return log;
	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.DESKTOP;
	}

	@Override public void requestMoreApps () {
		log(TAG, "requestMoreApps");
	}

	private void log (String tag, String message) {
		if (log) Gdx.app.log(tag, message);
	}

	@Override public void requestRewardVideo () {
		log(TAG, "requestRewardVideo");
	}

	@Override public void setDebug (boolean debug) {
		log(TAG, "setDebug ("+debug+")");
	}

	@Override public String getSpilUserID () {
		return "";
	}

	@Override public String getUserID() {
		return "";
	}

	@Override public void setUserID(String providerId, String userId) {

	}

	@Override public void trackEvent (SpilEvent event) {
		log(TAG, "trackEvent ("+event+")");
	}

	@Override public void trackEvent (SpilEvent event, SpilEventActionListener listener) {
		log(TAG, "trackEvent ("+event+", "+listener+")");
	}

	@Override public void setSpilNotificationDataListener (SpilNotificationDataListener rewardListener) {
		log(TAG, "SpilRewardListener ("+rewardListener+")");
	}

	@Override public JsonValue getConfig () {
		return null;
	}

	@Override public void requestPackages () {
		log(TAG, "requestPackages");
	}

	@Override public JsonValue getPromotion (String packageId) {
		log(TAG, "getPromotion");
		return null;
	}

	@Override public JsonValue getPackage (String packageId) {
		log(TAG, "getPackage");
		return null;
	}

	@Override public JsonValue getAllPackages () {
		log(TAG, "getAllPackages");
		return null;
	}

	@Override public boolean isAdProviderInitialized (String provider) {
		return false;
	}

	@Override public void showMoreApps () {
		log(TAG, "showMoreApps");
	}

	@Override public void showRewardVideo () {
		log(TAG, "showRewardVideo");
	}

	@Override public void setSpilAdListener (SpilAdListener adCallbacks) {
		log(TAG, "setSpilAdListener ("+adCallbacks+")");
	}

	@Override public void devRequestAd (String provider, String adType, boolean parentalGate) {
		log(TAG, "devRequestAd ("+provider+", "+adType+", "+parentalGate+")");
	}

	@Override public void devShowRewardVideo (String provider) {
		log(TAG, "devShowRewardVideo ("+provider+")");
	}

	@Override public void devShowInterstitial (String provider) {
		log(TAG, "devShowInterstitial ("+provider+")");
	}

	@Override public void devShowMoreApps (String provider) {
		log(TAG, "devShowMoreApps ("+provider+")");
	}

	@Override public void requestGameData () {
		log(TAG, "requestGameData");
	}

	@Override public void requestPlayerData () {
		log(TAG, "requestPlayerData)");
	}

	@Override public void setSpilPlayerDataListener (SpilPlayerDataListener playerDataListener) {
		log(TAG, "setSpilPlayerDataListener ("+playerDataListener+")");
	}

	@Override public void setSpilGameDataListener (SpilGameDataListener gameDataListener) {
		log(TAG, "setSpilGameDataListener ("+gameDataListener+")");
	}

	@Override public JsonValue getUserProfile () {
		log(TAG, "getUserProfile");
		return null;
	}

	@Override public JsonValue getWallet () {
		log(TAG, "getWallet");
		return null;
	}

	@Override public JsonValue getGameData () {
		log(TAG, "getGameData");
		return null;
	}

	@Override public JsonValue getInventory () {
		log(TAG, "getInventory");
		return null;
	}

//	@Override public String getShop () {
//		log(TAG, "getShop");
//		return null;
//	}
//
//	@Override public String getShopPromotions () {
//		log(TAG, "getShopPromotions");
//		return null;
//	}

	@Override public void addCurrencyToWallet (int currencyId, int amount, String reason) {
		log(TAG, "addCurrencyToWallet (" +currencyId+", "+amount+", "+ reason+")");
	}

	@Override public void subtractCurrencyFromWallet (int currencyId, int amount, String reason) {
		log(TAG, "subtractCurrencyFromWallet (" +currencyId+", "+amount+", "+ reason+")");
	}

	@Override public void addItemToInventory (int itemId, int amount, String reason) {
		log(TAG, "addItemToInventory (" +itemId+", "+amount+", "+ reason+")");
	}

	@Override public void subtractItemFromInventory (int itemId, int amount, String reason) {
		log(TAG, "subtractItemFromInventory (" +itemId+", "+amount+", "+ reason+")");
	}

	@Override public void consumeBundle (int bundleId, String reason) {
		log(TAG, "consumeBundle (" +bundleId+", "+ reason+")");
	}
}
