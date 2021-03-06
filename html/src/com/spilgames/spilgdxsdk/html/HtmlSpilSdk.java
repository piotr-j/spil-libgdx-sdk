package com.spilgames.spilgdxsdk.html;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.spilgames.spilgdxsdk.*;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class HtmlSpilSdk implements SpilSdk {
	private final static String TAG = HtmlSpilSdk.class.getSimpleName();
	private boolean log;
	private HtmlTrack track = new HtmlTrack();

	public HtmlSpilSdk() {
		this(true);
	}

	public HtmlSpilSdk(boolean loggingEnabled) {
		setLogging(loggingEnabled);
	}

	public void setLogging (boolean enabled) {
		this.log = enabled;
		track.log = enabled;
	}

	public boolean isLogging () {
		return log;
	}

	private void log (String tag, String message) {
		if (log) log(tag, message);
	}
	
	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.HTML;
	}

	@Override public void requestMoreApps () {
		log(TAG, "requestMoreApps");
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

	@Override public void requestOtherUsersGameState(String provider, Array<String> userIdsList) {
		log(TAG, "requestOtherUsersGameState (" + provider + ", " + userIdsList + ")");
	}

	@Override public String getUserID() {
		return "";
	}

	@Override public String getUserProvider () {
		return null;
	}

	@Override public void setUserID(String providerId, String userId) {
		log(TAG, "setUserID ("+providerId+", "+userId+")");
	}

	@Override public String getPublicGameState () {
		return null;
	}

	@Override public void setPublicGameState (String publicGameState) {
		log(TAG, "setPublicGameState ("+publicGameState+")");
	}

	@Override public String getPrivateGameState () {
		return null;
	}

	@Override public void setPrivateGameState (String privateGameState) {
		log(TAG, "setPrivateGameState ("+privateGameState+")");
	}

	@Override public void setSpilGameStateListener (final SpilGameStateListener gameStateListener) {
		log(TAG, "setSpilGameStateListener ("+gameStateListener+")");
	}

	@Override public void setSpilAutomatedEventsListener (SpilAutomatedEventsListener automatedEventsListener) {
		log(TAG, "setSpilAutomatedEventsListener ("+automatedEventsListener+")");
	}

	@Override public Track track () {
		return null;
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

	@Override public void setSpilConfigLDataListener (SpilConfigDataListener listener) {
		log(TAG, "setSpilConfigLDataListener ("+listener+")");
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

	@Override public void showToastOnVideoReward (boolean enabled) {
		log(TAG, "showToastOnVideoReward("+enabled+")");
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

	// customer support
	@Override public void showZendeskHelpCenter () {
		log(TAG, "showZendeskHelpCenter ()");
	}

	@Override public void showZendeskWebViewHelpCenter () {
		log(TAG, "showZendeskWebViewHelpCenter ()");
	}

	@Override public void showZendeskContactCenter () {
		log(TAG, "showZendeskContactCenter ()");
	}
}
