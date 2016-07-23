package com.spilgames.spilgdxsdk.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.chartboost.sdk.Chartboost;
import com.fyber.Fyber;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilsdk.SpilEnvironment;
import com.spilgames.spilsdk.ads.NativeAdCallbacks;
import com.spilgames.spilsdk.ads.OnAdsListener;
import com.spilgames.spilsdk.events.Event;
import com.spilgames.spilsdk.events.EventActionListener;
import com.spilgames.spilsdk.events.response.ResponseEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidSpilSdk implements SpilSdk {
	private final static String TAG = AndroidSpilSdk.class.getSimpleName();

	private com.spilgames.spilsdk.SpilSdk instance;
	public AndroidSpilSdk () {
		if (Gdx.app instanceof AndroidApplication) {
			AndroidApplication app = (AndroidApplication)Gdx.app;

			Log.d(TAG, "App = " + app);
			instance = com.spilgames.spilsdk.SpilSdk.getInstance(app);
			instance.setEnvironment(SpilEnvironment.STAGING);
			Application application = app.getApplication();
			// since SpilSDK requires sdk >14, this should be fine
			application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
				@Override public void onActivityCreated (Activity activity, Bundle bundle) {
					Log.d(TAG, "onActivityCreated");
					instance.onCreate();
				}

				@Override public void onActivityStarted (Activity activity) {
					Log.d(TAG, "onActivityStarted");
					instance.onStart();
				}

				@Override public void onActivityResumed (Activity activity) {
					Log.d(TAG, "onActivityResumed");
					instance.onResume();
				}

				@Override public void onActivityPaused (Activity activity) {
					Log.d(TAG, "onActivityPaused");
					instance.onPause();
				}

				@Override public void onActivityDestroyed (Activity activity) {
					Log.d(TAG, "onActivityDestroyed");
					instance.onDestroy();
				}

				@Override public void onActivityStopped (Activity activity) {}

				@Override public void onActivitySaveInstanceState (Activity activity, Bundle bundle) {}
			});
		} else {
			throw new AssertionError("Not running in android app?" + Gdx.app);
		}
	}

	@Override public void registerDevice (String projectID) {
		instance.registerDevice(projectID);
	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.ANDROID;
	}

	@Override public void setDebug (boolean debug) {
		// does nothing on android
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
//		ObjectMap<String, String> data = spilEvent.getData();
//		if (data != null) {
//			for (ObjectMap.Entry<String, String> entry : data.entries()) {
//				event.addCustomData(entry.key, entry.value);
//			}
//		}
//
//		ObjectMap<String, String> customData = spilEvent.getCustomData();
//		if (customData != null) {
//			for (ObjectMap.Entry<String, String> entry : customData.entries()) {
//				event.addCustomData(entry.key, entry.value);
//			}
//		}
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

	@Override public ObjectMap<String, String> getConfigAll () {
		String configAll = instance.getConfigAll();
		if (configAll == null) return null;
		ObjectMap<String, String> configMap = null;
		try {
			configMap = new Json(JsonWriter.OutputType.json).fromJson(ObjectMap.class, String.class, configAll);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse config data ", ex);
		}
		return configMap;
	}

	@Override public String getConfigValue (String key) {
		return instance.getConfigValue(key);
	}

	@Override public void onCreate () {
		Log.d(TAG, "onCreate");
		instance.onCreate();
	}

	@Override public void onStart () {
//		instance.onStart();
	}

	@Override public void onResume () {
//		instance.onResume();
	}

	@Override public void onPause () {
//		instance.onPause();
	}

	@Override public void onDestroy () {
//		instance.onDestroy();
	}

	@Override public void setSpilAdCallbacks (final SpilAdCallbacks adCallbacks) {
		if (adCallbacks == null) throw new AssertionError("SpilAdCallbacks cannot be null.");
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

	public void onBackPressed () {
		instance.onBackPressed();
	}

	@Override public void startChartboost (String appId, String appSignature) {
		instance.setupChartBoost(appId, appSignature);
	}

	@Override public void startDFP (String adUnitId) {
		instance.startDFP(adUnitId, null);
	}

	@Override public void startFyber (String appId, String token) {
		instance.startFyber(appId, token, null);
	}

	private void post(Runnable runnable) {
		((AndroidApplication)Gdx.app).getHandler().post(runnable);
	}

	@Override public void devRequestAd (final String provider, final String adType, final boolean parentalGate) {
		// NOTE this touches views and in gdx we are not on a proper thread
		post(new Runnable() {
			@Override public void run () {
				// TODO how do we want to show these?
				if (SpilSdk.PROVIDER_CHARTBOOST.equals(provider)) {
					if (SpilSdk.AD_INTERSTITIAL.equals(adType)) {
						instance.requestAd("ChartBoost", "interstitial", false);
					} else if (SpilSdk.AD_REWARD_VIDEO.equals(adType)) {
						instance.requestAd("ChartBoost", "rewardVideo", false);
					} else if (SpilSdk.AD_MORE_APPS.equals(adType)) {
						instance.requestAd("ChartBoost", "moreApps", false);
					} else {
						Gdx.app.error(TAG, "Unknown ad type " + adType);
					}
				} else if (SpilSdk.PROVIDER_FYBER.equals(provider)) {
					if (SpilSdk.AD_INTERSTITIAL.equals(adType)) {

					} else if (SpilSdk.AD_REWARD_VIDEO.equals(adType)) {
						//			instance.requestAd(provider, SpilSdk.AD_REWARD_VIDEO, false);
						instance.requestAd("Fyber", "rewardVideo", false);
					} else if (SpilSdk.AD_MORE_APPS.equals(adType)) {

					} else {
						Gdx.app.error(TAG, "Unknown ad type " + adType);
					}
				} else if (SpilSdk.PROVIDER_DFP.equals(provider)) {
					if (SpilSdk.AD_INTERSTITIAL.equals(adType)) {
						//			instance.requestAd(provider, SpilSdk.AD_REWARD_VIDEO, false);
						instance.requestAd("DFP", "interstitial", false);
					} else if (SpilSdk.AD_REWARD_VIDEO.equals(adType)) {

					} else if (SpilSdk.AD_MORE_APPS.equals(adType)) {

					} else {
						Gdx.app.error(TAG, "Unknown ad type " + adType);
					}
				} else {
					Gdx.app.error(TAG, "Unknown ad provider " + provider);
				}
			}

		});
	}

	@Override public void devShowRewardVideo (String provider) {
		// NOTE this touches views and in gdx we are not on a proper thread
//		instance.playVideo();
		post(new Runnable() {
			@Override public void run () {
				instance.playVideo();
			}
		});
//		if (SpilSdk.PROVIDER_CHARTBOOST.equals(provider)) {
//
//		} else if (SpilSdk.PROVIDER_FYBER.equals(provider)) {
//
//		} else if (SpilSdk.PROVIDER_DFP.equals(provider)) {
//
//		} else {
//			Gdx.app.error(TAG, "Unknown ad provider " + provider);
//		}
	}

	@Override public void devShowInterstitial (String provider) {
//		instance.playInterstitial();
		// NOTE this touches views and in gdx we are not on a proper thread
		post(new Runnable() {
			@Override public void run () {
				instance.playInterstitial();
			}
		});
//		if (SpilSdk.PROVIDER_CHARTBOOST.equals(provider)) {
//
//		} else if (SpilSdk.PROVIDER_FYBER.equals(provider)) {
//
//		} else if (SpilSdk.PROVIDER_DFP.equals(provider)) {
//
//		} else {
//			Gdx.app.error(TAG, "Unknown ad provider " + provider);
//		}
	}

	@Override public void devShowMoreApps (String provider) {
		// NOTE this touches views and in gdx we are not on a proper thread
//		instance.playMoreApps();
		post(new Runnable() {
			@Override public void run () {
				instance.playMoreApps();
			}
		});
//		if (SpilSdk.PROVIDER_CHARTBOOST.equals(provider)) {
//
//		} else if (SpilSdk.PROVIDER_FYBER.equals(provider)) {
//
//		} else if (SpilSdk.PROVIDER_DFP.equals(provider)) {
//
//		} else {
//			Gdx.app.error(TAG, "Unknown ad provider " + provider);
//		}
	}
}
