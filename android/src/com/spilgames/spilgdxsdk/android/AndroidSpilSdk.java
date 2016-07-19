package com.spilgames.spilgdxsdk.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilsdk.ads.NativeAdCallbacks;
import com.spilgames.spilsdk.ads.OnAdsListener;
import com.spilgames.spilsdk.events.Event;
import com.spilgames.spilsdk.events.EventActionListener;
import com.spilgames.spilsdk.events.response.ResponseEvent;

public class AndroidSpilSdk implements SpilSdk {
	private final static String TAG = AndroidSpilSdk.class.getSimpleName();

	private com.spilgames.spilsdk.SpilSdk instance;

	public AndroidSpilSdk () {
		if (Gdx.app instanceof AndroidApplication) {
			AndroidApplication app = (AndroidApplication)Gdx.app;
			Log.d(TAG, "App = " + app);
			instance = com.spilgames.spilsdk.SpilSdk.getInstance(app);
			Application application = app.getApplication();
			// since SpilSDK requires sdk >14, this should be fine
			application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
				@Override public void onActivityCreated (Activity activity, Bundle bundle) {
					Log.d(TAG, "onActivityCreated");
					instance.onCreate();
				}

				@Override public void onActivityStarted (Activity activity) {
					instance.onStart();
				}

				@Override public void onActivityResumed (Activity activity) {
					instance.onResume();
				}

				@Override public void onActivityPaused (Activity activity) {
					instance.onPause();
				}

				@Override public void onActivityDestroyed (Activity activity) {
					instance.onDestroy();
				}

				@Override public void onActivityStopped (Activity activity) {}

				@Override public void onActivitySaveInstanceState (Activity activity, Bundle bundle) {}
			});
		} else {
			throw new AssertionError("Not running in android app?" + Gdx.app);
		}
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
		event.setName(spilEvent.getName());
		event.setTimestamp(spilEvent.getTimestamp());

		ObjectMap<String, String> data = spilEvent.getData();
		if (data != null) {
			for (ObjectMap.Entry<String, String> entry : data.entries()) {
				event.addData(entry.key, entry.value);
			}
		}

		ObjectMap<String, String> customData = spilEvent.getCustomData();
		if (customData != null) {
			for (ObjectMap.Entry<String, String> entry : customData.entries()) {
				event.addCustomData(entry.key, entry.value);
			}
		}
		return event;
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

	@Override public void onBackPressed () {
		instance.onBackPressed();
	}


	@Override public void startChartboost (String appId, String appSignature) {
		instance.setupChartBoost(appId, appSignature);
	}


	@Override public void devRequestAd (String provider, String adType, boolean parentalGate) {

	}

	@Override public void devShowRewardVideo (String provider) {

	}

	@Override public void devShowInterstitial (String provider) {

	}

	@Override public void devShowMoreApps (String provider) {

	}
}
