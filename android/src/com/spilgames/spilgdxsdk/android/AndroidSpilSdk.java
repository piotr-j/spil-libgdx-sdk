package com.spilgames.spilgdxsdk.android;

import android.content.Context;
import android.support.annotation.NonNull;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilsdk.SpilEnvironment;
import com.spilgames.spilsdk.ads.AdCallbacks;
import com.spilgames.spilsdk.ads.OnAdsListener;
import com.spilgames.spilsdk.ads.dfp.DFPUtil;
import com.spilgames.spilsdk.config.ConfigDataCallbacks;
import com.spilgames.spilsdk.config.OnConfigDataListener;
import com.spilgames.spilsdk.events.Event;
import com.spilgames.spilsdk.events.EventActionListener;
import com.spilgames.spilsdk.events.response.ResponseEvent;
import com.spilgames.spilsdk.gamedata.OnGameDataListener;
import com.spilgames.spilsdk.gamedata.SpilGameDataCallbacks;
import com.spilgames.spilsdk.playerdata.GameStateCallbacks;
import com.spilgames.spilsdk.playerdata.OnGameStateListener;
import com.spilgames.spilsdk.playerdata.OnPlayerDataListener;
import com.spilgames.spilsdk.playerdata.PlayerDataCallbacks;
import com.spilgames.spilsdk.pushnotifications.NotificationDataCallbacks;
import com.spilgames.spilsdk.pushnotifications.OnNotificationListener;
import com.spilgames.spilsdk.utils.error.ErrorCodes;
import com.spilgames.spilsdk.web.dailybonus.DailyBonusCallbacks;
import com.spilgames.spilsdk.web.dailybonus.OnDailyBonusListener;
import com.spilgames.spilsdk.web.splashscreen.OnSplashScreenListener;
import com.spilgames.spilsdk.web.splashscreen.SplashScreenCallbacks;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidSpilSdk implements SpilSdk {
	private final static String TAG = AndroidSpilSdk.class.getSimpleName();
	private MyOnConfigDataListener configListener;
	private MyOnPlayerDataListener playerListener;
	private MySpilGameDataCallbacks gameListener;
	private MyOnAdsListener adListener;
	private MyOnDailyBonusListener dailyListener;
	private MyOnSplashScreenListener splashListener;
	private com.spilgames.spilsdk.SpilSdk instance;
	private AndroidTrack track;
	private SpilLifecycleListener lifecycleListener;

	public AndroidSpilSdk (Context context) {
		this(context, null);
	}

	public AndroidSpilSdk (Context context, SpilLifecycleListener listener) {
		com.spilgames.spilsdk.SpilSdk.resetContext();
		instance = com.spilgames.spilsdk.SpilSdk.getInstance(context);
		instance.setConfigDataCallbacks(new ConfigDataCallbacks(configListener = new MyOnConfigDataListener()));
		instance.setPlayerDataCallbacks(new PlayerDataCallbacks(playerListener = new MyOnPlayerDataListener()));
		instance.setGameDataCallbacks(new SpilGameDataCallbacks(gameListener = new MySpilGameDataCallbacks()));
		instance.setNativeAdCallbacks(new AdCallbacks(adListener = new MyOnAdsListener()));
		instance.setSplashScreenCallbacks(new SplashScreenCallbacks(splashListener = new MyOnSplashScreenListener()));
		instance.setDailyBonusCallbacks(new DailyBonusCallbacks(dailyListener = new MyOnDailyBonusListener()));
		track = new AndroidTrack(context);
		setSpilLifecycleListener(listener);
	}

	@Override public void setSpilLifecycleListener (SpilLifecycleListener listener) {
		lifecycleListener = listener;
		if (lifecycleListener != null) {
			lifecycleListener.initialized(this);
		}
	}

	public void registerDevice (String projectID) {
		instance.registerDevice(projectID);
	}

	public void setPluginInformation (String pluginName, String pluginVersion) {
		instance.setPluginInformation(pluginName, pluginVersion);
	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.ANDROID;
	}

	@Override public void setDebug (boolean debug) {
		if (debug) {
			instance.setEnvironment(SpilEnvironment.STAGING);
		} else {
			instance.setEnvironment(SpilEnvironment.PRODUCTION);
		}
	}

	@Override public void setSpilNotificationDataListener (final SpilNotificationDataListener notificationDataListener) {
		if (notificationDataListener == null) {
			instance.setNotificationDataCallbacks(null);
		} else {
			instance.setNotificationDataCallbacks(new NotificationDataCallbacks(new OnNotificationListener() {
				@Override public void onNotificationReceived (String notification) {
					notificationDataListener.onRewardReceived(toJson(notification));
				}
			}));
		}
	}

	@Override public String getSpilUserID () {
		return instance.getSpilUID(); // TODO change to getSpilUserID()
	}

	@Override public void requestOtherUsersGameState(String provider, Array<String> userIdsList) {
		instance.requestOtherUsersGameState(provider, toJsonString(userIdsList));
	}

	@Override public String getUserID () {
		return instance.getUserId();
	}

	@Override public String getUserProvider () {
		return instance.getUserProvider();
	}

	@Override public void setUserID(String providerId, String userId) {
		instance.setUserId(providerId, userId);
	}

	@Override public String getPublicGameState () {
		return instance.getPublicGameState();
	}

	@Override public void setPublicGameState (String publicGameState) {
		instance.setPublicGameState(publicGameState);
	}

	@Override public String getPrivateGameState () {
		return instance.getPrivateGameState();
	}

	@Override public void setPrivateGameState (String privateGameState) {
		instance.setPrivateGameState(privateGameState);
	}

	@Override public void setSpilGameStateListener (final SpilGameStateListener gameStateListener) {
		instance.setGameStateCallbacks(new GameStateCallbacks(new OnGameStateListener() {
			@Override public void GameStateUpdated (String access) {
				gameStateListener.gameStateUpdated(access);
			}

			@Override public void OtherUsersGameStateLoaded (String provider, JSONObject data) {
				gameStateListener.otherUsersGameStateLoaded(provider, toJson(data.toString()));
			}

			@Override public void GameStateError (ErrorCodes errorCode) {
				gameStateListener.gameStateError(SpilErrorCode.fromId(errorCode.getId()));
			}
		}));
	}

	@Override public void setSpilAutomatedEventsListener (final SpilAutomatedEventsListener automatedEventsListener) {
		// removed in 2.2.4, replaced by daily/splash callbacks

//		if (automatedEventsListener == null) {
//			instance.setWebCallbacks(null);
//			return;
//		}
//		instance.setWebCallbacks(new WebCallbacks(new OnWebListener() {
//			@Override public void OpenGameShop () {
//				automatedEventsListener.openGameShop();
//			}
//
//			@Override public void ReceiveReward (String rewardData) {
//				// TODO what about this? is this the same as notification reward thing? what about ios?
//			}
//		}));
	}

	@Override public AndroidTrack track () {
		return track;
	}

	@Override public void trackEvent (SpilEvent event) {
		trackEvent(event, null);
	}

	@Override public void trackEvent (SpilEvent event, final SpilEventActionListener listener) {
		if (event == null) throw new AssertionError("Event cannot be null!");
		if (listener == null) {
			instance.trackEvent(fillDataInEvent(new Event(), event));
		} else {
			instance.trackEvent(fillDataInEvent(new Event(), event), new EventActionListener() {
				@Override public void onResponse (ResponseEvent responseEvent) {
					listener.onResponse(fillDataInSpilResponseEvent(new SpilResponseEvent(), responseEvent));
				}
			});
		}
	}

	private Event fillDataInEvent (Event event, SpilEvent spilEvent) {
		event.setName(spilEvent.getName());
		event.setTimestamp(spilEvent.getTimestamp());
		JsonValue data = spilEvent.getData();
		if (data != null && data.child != null) {
			for (JsonValue next : data) {
				event.addData(next.name, next.asString());
			}
		}
		JsonValue customData = spilEvent.getCustomData();
		if (customData != null && customData.child != null) {
			for (JsonValue next : customData) {
				addCustomData(event, next);
			}
		}
		return event;
	}

	private void addCustomData (Event event, JsonValue value) {
		switch (value.type()) {
		case object: {
			String json = value.toJson(JsonWriter.OutputType.json);
			try {
				event.addCustomData(value.name, new JSONObject(json));
			} catch (JSONException ex) {
				Gdx.app.error(TAG, "Failed to add custom object data", ex);
			}
		}break;
		case array:
			String json = value.toJson(JsonWriter.OutputType.json);
			try {
				event.addCustomData(value.name, new JSONArray(json));
			} catch (JSONException ex) {
				Gdx.app.error(TAG, "Failed to add custom object data", ex);
			}
			break;
		case stringValue:
		case doubleValue:
		case booleanValue:
			event.addCustomData(value.name, value.asString());
			break;
		case longValue:
			event.addCustomData(value.name, value.asInt());
			break;
		case nullValue:
			break;
		}
	}

	private SpilResponseEvent fillDataInSpilResponseEvent (SpilResponseEvent event, ResponseEvent responseEvent) {
		event.setName(responseEvent.getName());
		event.setTimestamp(responseEvent.getTimestamp());
		event.setQueued(responseEvent.isQueued());
		event.setAction(responseEvent.getAction());
		event.setTs(responseEvent.getTs());
		event.setType(responseEvent.getType());
		return event;
	}

	@Override public void requestConfig () {
		instance.requestConfig();
	}

	@Override public JsonValue getConfig () {
		String data = instance.getConfigAll();
		instance.getSpilUID(); // TODO change to getSpilUserID()
		return toJson(data);
	}

	@Override public void setSpilConfigLDataListener (SpilConfigDataListener listener) {
		configListener.listener = listener;
	}

	@Override public void requestPackages () {
		instance.requestPackages();
	}

	@Override public JsonValue getAllPackages () {
		return toJson(instance.getAllPackages());
	}

	@Override public JsonValue getPackage (String packageId) {
		return toJson(instance.getPackage(packageId));
	}

	@Override public JsonValue getPromotion (String packageId) {
		return toJson(instance.getPromotion(packageId));
	}

	private static JsonValue toJson (String data) {
		if (data == null) return null;
		try {
			return new JsonReader().parse(data);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse json data ", ex);
		}
		return null;
	}

	private static String toJsonString (Array<String> data) {
		if (data == null) return null;
		if (data.size == 0) return "[]";
		StringBuilder sb = new StringBuilder(64);
		sb.append("[\"").append(data.get(0)).append("\"");
		for (int i = 1, size = data.size; i < size; i++) {
			sb.append(", \"").append(data.get(i)).append("\"");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override public void setSpilAdListener (final SpilAdListener spilAdListener) {
		adListener.listener = spilAdListener;
	}

	public void startChartboost (final String appId, final String appSignature) {
		postUI(new Runnable() {
			@Override public void run () {
				instance.setupChartBoost(appId, appSignature);
			}
		});
	}

	public void startDFP (final String adUnitId) {
		postUI(new Runnable() {
			@Override public void run () {
				instance.startDFP(adUnitId, null);
			}
		});
	}

	public void startFyber (final String appId, final String token) {
		postUI(new Runnable() {
			@Override public void run () {
				instance.startFyber(appId, token, null);
			}
		});
	}

	@Override public void requestRewardVideo () {
		instance.requestRewardVideo();
	}

	@Override public void requestMoreApps () {
		instance.requestMoreApps();
	}

	@Override public boolean isAdProviderInitialized (String provider) {
		String clean = provider.trim().toLowerCase();
		if (clean.equals("chartboost")) {
			// no easy way to know, but wont explode when request ad is called
			return true;
		} else if (clean.equals("dfp")) {
			// DFP is initialized when this is not null
			return DFPUtil.getInstance().getPublisherInterstitialAd() != null;
		} else if (clean.equals("fyber")) {
			// no easy way to know, but wont explode when request ad is called
			return true;
		}
		return false;
	}

	@Override public void showToastOnVideoReward (boolean enabled) {
		instance.showToastOnVideoReward(enabled);
	}

	@Override public void devRequestAd (final String provider, final String adType, final boolean parentalGate) {
		// NOTE this touches views and in gdx we are not on a proper thread
		postUI(new Runnable() {
			@Override public void run () {
				instance.requestAd(provider, adType, false);
			}

		});
	}

	// TODO there are no specific dev methods on android, do we want them only on ios?
	@Override public void devShowRewardVideo (String provider) {
		instance.playVideo();
	}

	@Override public void devShowInterstitial (String provider) {
		instance.playInterstitial();
	}

	@Override public void devShowMoreApps (String provider) {
		instance.playMoreApps();
	}

	@Override public void showMoreApps () {
		instance.playMoreApps();
	}

	@Override public void showRewardVideo () {
		instance.playVideo();
	}

	@Override public void requestGameData () {
		instance.requestGameData();
	}

	@Override public void requestPlayerData () {
		instance.requestPlayerData();
	}

	@Override public void setSpilGameDataListener (SpilGameDataListener gameDataListener) {
		gameListener.listener = gameDataListener;
	}

	@Override public void setSpilPlayerDataListener (SpilPlayerDataListener playerDataListener) {
		playerListener.listener = playerDataListener;
	}

	@Override public JsonValue getUserProfile () {
		return toJson(instance.getUserProfile());
	}

	@Override public JsonValue getWallet () {
		return toJson(instance.getWallet());
	}

	@Override public JsonValue getGameData () {
		return toJson(instance.getSpilGameData());
	}

	@Override public JsonValue getInventory () {
		return toJson(instance.getInventory());
	}

	@Override public void addCurrencyToWallet (int currencyId, int amount, String reason) {
		instance.addCurrencyToWallet(currencyId, amount, reason);
	}

	@Override public void subtractCurrencyFromWallet (int currencyId, int amount, String reason) {
		instance.subtractCurrencyFromWallet(currencyId, amount, reason);
	}

	@Override public void addItemToInventory (int itemId, int amount, String reason) {
		instance.addItemToInventory(itemId, amount, reason);
	}

	@Override public void subtractItemFromInventory (int itemId, int amount, String reason) {
		instance.subtractItemFromInventory(itemId, amount, reason);
	}

	@Override public void consumeBundle (int bundleId, String reason) {
		instance.consumeBundle(bundleId, reason);
	}

	// customer support

	@Override public void showZendeskHelpCenter () {
		instance.showZendeskHelpCenter();
	}

	@Override public void showZendeskWebViewHelpCenter () {
		instance.showZendeskWebViewHelpCenter();
	}

	@Override public void showZendeskContactCenter () {
		instance.showContactZendeskCenter();
	}

	public void onCreate () {
		instance.onCreate();
	}

	public void onStart () {
		instance.onStart();
	}

	public void onResume () {
		instance.onResume();
	}

	public void onPause () {
		instance.onPause();
	}

	public void onDestroy () {
		instance.onDestroy();
	}

	public void onBackPressed () {
		instance.onBackPressed();
	}

	public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
		@NonNull int[] grantResults) {
		instance.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void postUI(Runnable runnable) {
		// if app is not initialized, we should be on ui thread anyway
		if (Gdx.app == null) {
			runnable.run();
		} else {
			((AndroidApplication)Gdx.app).runOnUiThread(runnable);
		}
	}

	private static class MyOnConfigDataListener implements OnConfigDataListener {
		SpilConfigDataListener listener;
		@Override public void ConfigDataUpdated () {
			if (listener != null) listener.configDataUpdated();
		}
	}

	private static class MyOnPlayerDataListener implements OnPlayerDataListener {
		SpilPlayerDataListener listener;
		@Override public void PlayerDataAvailable () {
			if (listener != null) listener.playerDataAvailable();
		}

		@Override public void PlayerDataUpdated (String reason, String updatedData) {
			if (listener != null) listener.playerDataUpdated(reason, toJson(updatedData));
		}

		@Override public void PlayerDataError (ErrorCodes error) {
			if (listener != null) listener.playerDataError(SpilErrorCode.fromId(error.getId()));
		}
	}

	private static class MySpilGameDataCallbacks implements OnGameDataListener {
		SpilGameDataListener listener;
		@Override public void GameDataAvailable () {
			if (listener != null) listener.gameDataAvailable();
		}

		@Override public void GameDataError (ErrorCodes error) {
			if (listener != null) listener.gameDataError(SpilErrorCode.fromId(error.getId()));
		}
	}

	private static class MyOnAdsListener implements OnAdsListener {
		SpilAdListener listener;
		@Override public void AdAvailable (String type) {
			if (listener != null) listener.adAvailable(type);
		}

		@Override public void AdNotAvailable (String type) {
			if (listener != null) listener.adNotAvailable(type);
		}

		@Override public void AdStart () {
			if (listener != null) listener.adStart();
		}

		@Override public void AdFinished (String response) {
			if (listener != null) listener.adFinished(toJson(response));
		}
	}

	private static class MyOnDailyBonusListener implements OnDailyBonusListener {
		@Override public void DailyBonusOpen () {

		}

		@Override public void DailyBonusClosed () {

		}

		@Override public void DailyBonusNotAvailable () {

		}

		@Override public void DailyBonusError (ErrorCodes errorCode) {

		}

		@Override public void DailyBonusReward (String rewardList) {

		}
	}

	private static class MyOnSplashScreenListener implements OnSplashScreenListener {
		@Override public void SplashScreenOpenShop () {

		}

		@Override public void SplashScreenOpen () {

		}

		@Override public void SplashScreenClosed () {

		}

		@Override public void SplashScreenNotAvailable () {

		}

		@Override public void SplashScreenError (ErrorCodes errorCode) {

		}
	}
}
