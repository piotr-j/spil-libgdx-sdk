package com.spilgames.spilgdxsdk.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilgdxsdk.html.bindings.JsSpilSdk;
import com.spilgames.spilgdxsdk.html.bindings.JsUtils;


/**
 * Created by PiotrJ on 01/07/16.
 */
public class HtmlSpilSdk implements SpilSdk {
	public interface SpilSdkLoadedCallback {
		void loaded(SpilSdk spilSdk);
	}
	private final static String TAG = HtmlSpilSdk.class.getSimpleName();
	private boolean log;
	private HtmlTrack track = new HtmlTrack();

	private boolean initialized;

	// TODO we probably dont want this stuff in constructor
	public HtmlSpilSdk(String appId, String version, String env, SpilSdkLoadedCallback callback) {
		this(appId, version, env, callback, true);
	}

	public HtmlSpilSdk(final String appId, final String version, final String env, final SpilSdkLoadedCallback callback, boolean loggingEnabled) {
		setLogging(loggingEnabled);
		// TODO how do we get this? hopefully will be available somewhere eventually
		// TODO yay callbacks, we probably want to add a callback in core, something like 'sdk loaded' or whatever
		ScriptInjector.fromUrl("spil-sdk.js").setCallback(new Callback<Void, Exception>() {
			@Override public void onSuccess (Void aVoid) {
				JsSpilSdk.init(appId, version, env, new Callback<Void, Void>() {
					@Override public void onSuccess (Void aVoid) {System.out.println("Loaded spil! ");
						initialized = true;
						if (callback != null) {
							callback.loaded(HtmlSpilSdk.this);
						}
					}

					@Override public void onFailure (Void result) {
						// NOTE: this will never get called
					}
				});
			}

			@Override public void onFailure (Exception e) {
				log(TAG, "FAILED to load Spil SDK");
			}
		}).inject();
	}

	public void setLogging (boolean enabled) {
		this.log = enabled;
		track.log = enabled;
	}

	public boolean isLogging () {
		return log;
	}

	private void log (String tag, String message) {
		if (log){
			// NOTE Gdx.app will be null before gdx is fully initialized, which is after createApplicationListener entry point returns
			if (Gdx.app != null) {
				Gdx.app.log(tag, message);
			} else {
				JsUtils.log(TAG, message);
			}
		}
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
		// is this uuid? ot spil user id?
		return JsSpilSdk.getUuid();
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
		JsonValue customData = event.getCustomData();
		if (customData != null) {
			JsSpilSdk.sendEvent(event.getName(), customData.toJson(JsonWriter.OutputType.json), null);
		} else {
			JsSpilSdk.sendEvent(event.getName(), null, null);
		}
	}

	@Override public void trackEvent (SpilEvent event, SpilEventActionListener listener) {
		log(TAG, "trackEvent ("+event+", "+listener+")");
		JsonValue customData = event.getCustomData();
		if (customData != null) {
			JsSpilSdk.sendEvent(event.getName(), customData.toJson(JsonWriter.OutputType.json), listener);
		} else {
			JsSpilSdk.sendEvent(event.getName(), null, listener);
		}
	}

	@Override public void setSpilNotificationDataListener (SpilNotificationDataListener rewardListener) {
		log(TAG, "SpilRewardListener ("+rewardListener+")");
	}

	@Override public void requestConfig () {
		JsSpilSdk.refreshConfig();
	}

	@Override public JsonValue getConfig () {
		return toJson(JsSpilSdk.getConfigAll());
	}

	@Override public void setSpilConfigLDataListener (SpilConfigDataListener listener) {
		// NOTE perhaps we will need simpler listeners as we need to translate some stuff into out classes, might be simpler in java
		JsSpilSdk.setConfigDataCallbacks(listener);
	}

	@Override public void requestPackages () {
		log(TAG, "requestPackages");
		JsSpilSdk.requestPackages();
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

		// NOTE perhaps we will need simpler listeners as we need to translate some stuff into out classes, might be simpler in java
		JsSpilSdk.setAdCallbacks(adCallbacks);
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

		// NOTE perhaps we will need simpler listeners as we need to translate some stuff into out classes, might be simpler in java
		JsSpilSdk.setPlayerDataCallbacks(playerDataListener);
	}

	@Override public void setSpilGameDataListener (SpilGameDataListener gameDataListener) {
		log(TAG, "setSpilGameDataListener ("+gameDataListener+")");

		// NOTE perhaps we will need simpler listeners as we need to translate some stuff into out classes, might be simpler in java
		JsSpilSdk.setGameDataCallbacks(gameDataListener);
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

	private JsonReader jsonReader = new JsonReader();
	private JsonValue toJson (String data) {
		if (data == null) return null;
		try {
			return jsonReader.parse(data);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse json data ", ex);
		}
		return null;
	}
}
