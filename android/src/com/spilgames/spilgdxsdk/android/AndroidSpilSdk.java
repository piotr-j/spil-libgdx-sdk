package com.spilgames.spilgdxsdk.android;

import android.content.Context;
import android.support.annotation.NonNull;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilsdk.SpilEnvironment;
import com.spilgames.spilsdk.ads.NativeAdCallbacks;
import com.spilgames.spilsdk.ads.OnAdsListener;
import com.spilgames.spilsdk.ads.dfp.DFPUtil;
import com.spilgames.spilsdk.events.Event;
import com.spilgames.spilsdk.events.EventActionListener;
import com.spilgames.spilsdk.events.response.ResponseEvent;
import com.spilgames.spilsdk.gamedata.OnGameDataListener;
import com.spilgames.spilsdk.gamedata.SpilGameDataCallbacks;
import com.spilgames.spilsdk.playerdata.OnPlayerDataListener;
import com.spilgames.spilsdk.playerdata.PlayerDataCallbacks;
import com.spilgames.spilsdk.utils.error.ErrorCodes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidSpilSdk implements SpilSdk {
	private final static String TAG = AndroidSpilSdk.class.getSimpleName();

	private com.spilgames.spilsdk.SpilSdk instance;
	public AndroidSpilSdk (Context context) {
		instance = com.spilgames.spilsdk.SpilSdk.getInstance(context);
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
			instance.setEnvironment(SpilEnvironment.PRODUCTION);
		} else {
			instance.setEnvironment(SpilEnvironment.PRODUCTION);
		}
	}

	private SpilRewardListener rewardListener;
	public void processNotification () {
		String dara = instance.processNotification();
		if (dara != null && rewardListener != null) {
			rewardListener.onRewardReceived(toJson(dara));
		}
	}

	@Override public void setSpilRewardListener (SpilRewardListener rewardListener) {
		this.rewardListener = rewardListener;
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

	@Override public JsonValue getConfig () {
		String data = instance.getConfigAll();
		return toJson(data);
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

	private JsonValue toJson (String data) {
		if (data == null) return null;
		try {
			return new JsonReader().parse(data);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse json data ", ex);
		}
		return null;
	}

	@Override public void setSpilAdCallbacks (final SpilAdCallbacks adCallbacks) {
		if (adCallbacks == null) {
			instance.setNativeAdCallbacks(null);
			return;
		}
		NativeAdCallbacks nativeAdCallbacks = new NativeAdCallbacks(new OnAdsListener() {
			@Override public void AdAvailable (String type) {
				adCallbacks.adAvailable(type);
			}

			@Override public void AdNotAvailable (String type) {
				adCallbacks.adNotAvailable(type);
			}

			@Override public void AdStart () {
				adCallbacks.adStart();
			}

			@Override public void AdFinished (String type) {
				adCallbacks.adFinished(type);
			}
		});
		instance.setNativeAdCallbacks(nativeAdCallbacks);
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
			return DFPUtil.getPublisherInterstitialAd() != null;
		} else if (clean.equals("fyber")) {
			// no easy way to know, but wont explode when request ad is called
			return true;
		}
		return false;
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

	@Override public void setSpilGameDataListener (final SpilGameDataListener gameDataListener) {
		if (gameDataListener == null) {
			instance.setGameDataCallbacks(null);
			return;
		}
		instance.setGameDataCallbacks(new SpilGameDataCallbacks(new OnGameDataListener() {
			@Override public void GameDataAvailable () {
				gameDataListener.gameDataAvailable();
			}

			@Override public void GameDataError (ErrorCodes errorCodes) {
				gameDataListener.gameDataError(SpilErrorCode.fromId(errorCodes.getId()));
			}
		}));
	}

	@Override public void setSpilPlayerDataListener (final SpilPlayerDataListener playerDataListener) {
		if (playerDataListener == null) {
			instance.setPlayerDataCallbacks(null);
			return;
		}
		instance.setPlayerDataCallbacks(new PlayerDataCallbacks(new OnPlayerDataListener() {
			@Override public void PlayerDataAvailable () {
				playerDataListener.playerDataAvailable();
			}

			@Override public void PlayerDataUpdated (String reason) {
				playerDataListener.playerDataUpdated(reason);
			}

			@Override public void PlayerDataError (ErrorCodes errorCodes) {
				playerDataListener.playerDataError(SpilErrorCode.fromId(errorCodes.getId()));
			}
		}));
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

	public void onCreate () {
		instance.onCreate();
	}

	public void onStart () {
		instance.onStart();
	}

	public void onResume () {
		instance.onResume();
		// TODO do we want to call this or make the user do that?
		if (rewardListener != null) {
			processNotification();
		}
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
		((AndroidApplication)Gdx.app).runOnUiThread(runnable);
	}
}
