package com.spilgames.spilgdxsdk.ios.robovm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
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
	public IosRoboVMSpilSdk (IOSApplication.Delegate delegate){

	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_ROBOVM;
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
		return toJson(jsonString.toString());
	}

	private JsonReader jsonReader = new JsonReader();
	private JsonValue toJson (String data) {
		try {
			return jsonReader.parse(data);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Failed to parse json data ", ex);
		}
		return null;
	}

	@Override public void setSpilRewardListener (SpilRewardListener rewardListener) {
		initDelegate();
		delegate.rewardListener = rewardListener;
	}

	@Override public void setSpilAdCallbacks (SpilAdCallbacks adCallbacks) {
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

	@Override public boolean isAdProviderInitialized (String provider) {
		return Spil.isAdProviderInitialized(provider);
	}

	@Override public void requestMoreApps () {
		// same as android
		Spil.devRequestAd("ChartBoost", "moreApps", false);
	}

	@Override public void requestRewardVideo () {
		Spil.trackEvent("requestRewardVideo");
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

	@Override public void requestGameData () {
		// its there in .m but not in header
		// it also is just an 'requestGameData' event, so we could do that?
		Spil.requestGameData();
	}

	@Override public void requestPlayerData () {
		Spil.requestPlayerData();
	}

	@Override public void setSpilPlayerDataListener (SpilPlayerDataListener playerDataListener) {
		initDelegate();
		delegate.playerDataListener = playerDataListener;
	}

	@Override public void setSpilGameDataListener (SpilGameDataListener gameDataListener) {
		initDelegate();
		delegate.gameDataListener = gameDataListener;
	}

	@Override public JsonValue getUserProfile () {
		return toJson(Spil.getUserProfile());
	}

	@Override public JsonValue getWallet () {
		return toJson(Spil.getWallet());
	}

	@Override public JsonValue getGameData () {
		return toJson(Spil.getSpilGameData());
	}

	@Override public JsonValue getInventory () {
		return toJson(Spil.getInventory());
	}

//	public String getShop () {
//		// this is getGameData.get(shop)
//		return Spil.getShop();
//	}
//
//	public String getShopPromotions () {
//		// this is getGameData.get(promotions)
//		return Spil.getShopPromotions();
//	}

	@Override public void addCurrencyToWallet (int currencyId, int amount, String reason) {
		Spil.addCurrencyToWallet(currencyId, amount, reason);
	}

	@Override public void subtractCurrencyFromWallet (int currencyId, int amount, String reason) {
		Spil.subtractCurrencyFromWallet(currencyId, amount, reason);
	}

	@Override public void addItemToInventory (int itemId, int amount, String reason) {
		Spil.addItemToInventory(itemId, amount, reason);
	}

	@Override public void subtractItemFromInventory (int itemId, int amount, String reason) {
		Spil.subtractItemFromInventory(itemId, amount, reason);
	}

	@Override public void consumeBundle (int bundleId, String reason) {
		Spil.consumeBundle(bundleId, reason);
	}

	private class SpilDelegate extends SpilDelegateAdapter {
		SpilAdCallbacks adCallbacks;
		SpilRewardListener rewardListener;
		SpilGameDataListener gameDataListener;
		SpilPlayerDataListener playerDataListener;

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
			// NOTE there is no equivalent on android side
		}

		@Override public void spilGameDataAvailable () {
			if (gameDataListener != null) gameDataListener.gameDataAvailable();
		}

		@Override public void spilGameDataError (String message) {
			if (gameDataListener != null) {
				JsonValue value = toJson(message);
				if (value == null) {
					playerDataListener.playerDataError(SpilErrorCode.Invalid);
				} else {
					int id = value.getInt("id");
					playerDataListener.playerDataError(SpilErrorCode.fromId(id));
				}
			}
		}

		@Override public void playerDataAvailable () {
			if (playerDataListener != null) playerDataListener.playerDataAvailable();
		}

		@Override public void playerDataError (String message) {
			if (playerDataListener != null) {
				JsonValue value = toJson(message);
				if (value == null) {
					playerDataListener.playerDataError(SpilErrorCode.Invalid);
				} else {
					int id = value.getInt("id");
					playerDataListener.playerDataError(SpilErrorCode.fromId(id));
				}
			}
		}

		@Override public void playerDataUpdated (String reason) {
			if (playerDataListener != null) playerDataListener.playerDataUpdated(reason);
		}
	}
}
