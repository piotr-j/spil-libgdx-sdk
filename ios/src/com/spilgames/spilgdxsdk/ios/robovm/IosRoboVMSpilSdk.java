package com.spilgames.spilgdxsdk.ios.robovm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilgdxsdk.ios.robovm.bindings.Spil;
import com.spilgames.spilgdxsdk.ios.robovm.bindings.SpilDelegateAdapter;
import com.spilgames.spilgdxsdk.ios.robovm.bindings.JsonUtil;
import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIRemoteNotification;
import org.robovm.objc.block.VoidBlock1;


/**
 * Created by PiotrJ on 01/07/16.
 */
public class IosRoboVMSpilSdk implements SpilSdk {
	private final static String TAG = IosRoboVMSpilSdk.class.getSimpleName();

	private SpilDelegate delegate;
	public IosRoboVMSpilSdk (){

	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_ROBOVM;
	}

	@Override public void registerDevice (String projectID) {
		// does nothing on ios
	}

	@Override public void setDebug (boolean debug) {
		Spil.debug(debug);
	}

	@Override public void trackEvent (SpilEvent event) {
		Spil.trackEvent(event.getName(), buildParams(event));
	}

	private final static NSString KEY_NAME = new NSString("name");
	@Override public void trackEvent (SpilEvent event, final SpilEventActionListener listener) {
		Spil.trackEvent(event.getName(), buildParams(event), new VoidBlock1<NSObject>() {
			@Override public void invoke (NSObject nsObject) {
				SpilResponseEvent responseEvent = new SpilResponseEvent();
				responseEvent.setName("responseEvent");
				if (nsObject instanceof NSDictionary) {
					NSDictionary<?, ?> responseDict = (NSDictionary<?, ?>)nsObject;
					if (responseDict.containsKey(KEY_NAME)) {
						responseEvent.setName(responseDict.getString(KEY_NAME));
					}
					// TODO is this data or custom data? lets go with data for now
					for (NSObject key : responseDict.keySet()) {
						responseEvent.addData(key.toString(), responseDict.getString(key));
					}
				} else {
					Gdx.app.error(TAG, "Unexpected response object type " + nsObject.getClass());
				}
				listener.onResponse(responseEvent);
			}
		});
	}

	private NSDictionary<?, ?> buildParams (SpilEvent event) {
		JsonValue data = event.getData();
		JsonValue customData = event.getCustomData();
		if (data == null && customData == null) return null;
		NSMutableDictionary<?, ?> params = new NSMutableDictionary<>();
		if (data != null && data.child != null) {
			for (JsonValue next : data) {
				params.put(next.name, next.asString());
			}
		}
		if (customData != null && customData.child != null) {
			for (JsonValue next : customData) {
				params.put(next.name, next.asString());
			}
		}
		return params;
	}

	@Override public JsonValue getConfig () {
		return toJson(Spil.getConfig());
	}

	@Override public void requestPackages () {
		Spil.requestPackages();
	}

	@Override public JsonValue getPromotion (String packageId) {
		return toJson(Spil.getPromotionByID(packageId));
	}

	@Override public JsonValue getPackage (String packageId) {
		return toJson(Spil.getPackageByID(packageId));
	}

	@Override public JsonValue getAllPackages () {
		return toJson(Spil.getAllPackages());
	}

	// no android equivalent
	public JsonValue getAllPromotions () {
		return toJson(Spil.getAllPromotions());
	}

	private JsonValue toJson (NSObject data) {
		if (data == null) return null;
		NSString jsonString = JsonUtil.convertObjectToJson(data);
		if (jsonString == null) return null;
		try {
			return new JsonReader().parse(jsonString.toString());
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse json data ", ex);
		}
		return null;
	}

	@Override public void setSpilRewardListener (SpilRewardListener rewardListener) {
		initDelegate();
		delegate.rewardListener = rewardListener;
	}

	@Override public void startChartboost (String appId, String appSignature) {
		Gdx.app.log(TAG, "startChartboost ("+appId+", "+appSignature+")");
	}

	@Override public void startFyber (String appId, String token) {
		Gdx.app.log(TAG, "startFyber ("+appId+", "+token+")");
	}

	@Override public void startDFP (String adUnitId) {
		Gdx.app.log(TAG, "startDFP ("+adUnitId+")");
	}

	@Override public void setSpilAdCallbacks (final SpilAdCallbacks adCallbacks) {
		if (adCallbacks == null)
			throw new AssertionError("SpilAdCallbacks cannot be null!");
		initDelegate();
		delegate.adCallbacks = adCallbacks;
	}

	private void initDelegate () {
		if (delegate == null) {
			delegate = new SpilDelegate();
			Spil instance = Spil.getInstance();
			instance.setDelegate(delegate);
		}
	}

	@Override public void showRewardVideo () {
		Spil.playRewardVideo();
	}

	@Override public void showMoreApps () {
		Spil.showMoreApps();
	}

	@Override public void devRequestAd (String provider, String adType, boolean parentalGate) {
		Spil.devRequestAd(provider, adType, parentalGate);
	}

	// TODO does this dev stuff even work?
	@Override public void devShowRewardVideo (String provider) {
		Spil.devShowRewardVideo(provider);
	}

	@Override public void devShowInterstitial (String provider) {
		Spil.devShowInterstitial(provider);
	}

	@Override public void devShowMoreApps (String provider) {
		Spil.devShowMoreApps(provider);
	}

	public void onCreate () {
		Spil.start();
	}

	public void didBecomeActive (UIApplication application) {
		Spil.applicationDidBecomeActive(application);
	}

	public void didEnterBackground (UIApplication application) {
		Spil.applicationDidEnterBackground(application);
	}

	public void didReceiveRemoteNotification(UIApplication application, UIRemoteNotification userInfo) {
		Spil.didReceiveRemoteNotification(application, userInfo.getDictionary());
	}

	private class SpilDelegate extends SpilDelegateAdapter {
		SpilAdCallbacks adCallbacks;
		SpilRewardListener rewardListener;

		@Override public void adAvailable (String type) {
			if (adCallbacks != null) adCallbacks.adAvailable(type);
		}

		@Override public void adNotAvailable (String type) {
			if (adCallbacks != null) adCallbacks.adNotAvailable(type);
		}

		@Override public void adStart () {
			if (adCallbacks != null) adCallbacks.adStart();
		}

		@Override public void adFinished (String type, String reason, String reward, String network) {
			if (adCallbacks != null) adCallbacks.adFinished(type);
		}

		@Override public void notificationReward (NSDictionary<?, ?> reward) {
			if (rewardListener != null) rewardListener.onRewardReceived(toJson(reward));
		}

		@Override public void packagesLoaded () {

		}

		@Override public void spilGameDataAvailable () {

		}

		@Override public void spilGameDataError (String message) {

		}

		@Override public void playerDataAvailable () {

		}

		@Override public void playerDataError (String message) {

		}

		@Override public void playerDataUpdated (String reason) {

		}
	}
}
