package com.spilgames.spilgdxsdk.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.spilgames.spilgdxsdk.*;
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

			instance = com.spilgames.spilsdk.SpilSdk.getInstance(app);
			// TODO double check if we can get this to work
//			instance.setEnvironment(SpilEnvironment.STAGING);
			// looks like this doesnt work as well as we would have hoped
//			Application application = app.getApplication();
			// since SpilSDK requires sdk >14, this should be fine
//			application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
//				@Override public void onActivityCreated (Activity activity, Bundle bundle) {
//					Log.d(TAG, "onActivityCreated");
//					instance.onCreate();
//				}
//
//				@Override public void onActivityStarted (Activity activity) {
//					Log.d(TAG, "onActivityStarted");
//					instance.onStart();
//				}
//
//				@Override public void onActivityResumed (Activity activity) {
//					Log.d(TAG, "onActivityResumed");
//					instance.onResume();
//				}
//
//				@Override public void onActivityPaused (Activity activity) {
//					Log.d(TAG, "onActivityPaused");
//					instance.onPause();
//				}
//
//				@Override public void onActivityDestroyed (Activity activity) {
//					Log.d(TAG, "onActivityDestroyed");
//					instance.onDestroy();
//				}
//
//				@Override public void onActivityStopped (Activity activity) {}
//
//				@Override public void onActivitySaveInstanceState (Activity activity, Bundle bundle) {}
//			});
		} else {
			throw new AssertionError("Not running in android app?" + Gdx.app);
		}
	}

	// TODO we probably dont need that once stuff works
	public com.spilgames.spilsdk.SpilSdk getInstance () {
		return instance;
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
		return tryParse(data);
	}

	@Override public void requestPackages () {
		instance.requestPackages();
	}

	@Override public JsonValue getAllPackages () {
		return tryParse(instance.getAllPackages());
	}

	@Override public JsonValue getPackage (String packageId) {
		return tryParse(instance.getPackage(packageId));
	}

	@Override public JsonValue getPromotion (String packageId) {
		return tryParse(instance.getPromotion(packageId));
	}

	private JsonValue tryParse (String data) {
		if (data == null) return null;
		try {
			return new JsonReader().parse(data);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse json data ", ex);
		}
		return null;
	}

	@Override public void onCreate () {
		instance.onCreate();
	}

	@Override public void onStart () {
		instance.onStart();
	}

	@Override public void onResume () {
		instance.onResume();
	}

	@Override public void onPause () {
		instance.onPause();
	}

	@Override public void onDestroy () {
		instance.onDestroy();
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

	@Override public void startChartboost (final String appId, final String appSignature) {
		postUI(new Runnable() {
			@Override public void run () {
				instance.setupChartBoost(appId, appSignature);
			}
		});
	}

	@Override public void startDFP (final String adUnitId) {
		postUI(new Runnable() {
			@Override public void run () {
				instance.startDFP(adUnitId, null);
			}
		});
	}

	@Override public void startFyber (final String appId, final String token) {
		postUI(new Runnable() {
			@Override public void run () {
				instance.startFyber(appId, token, null);
			}
		});
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

	private void postUI(Runnable runnable) {
		((AndroidApplication)Gdx.app).runOnUiThread(runnable);
	}
}
