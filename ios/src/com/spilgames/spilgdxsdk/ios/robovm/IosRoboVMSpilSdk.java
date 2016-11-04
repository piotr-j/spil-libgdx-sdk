package com.spilgames.spilgdxsdk.ios.robovm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.utils.Array;
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
	private IosRoboVMTrack tracking;
	private SpilLifecycleListener lifecycleListener;

	public IosRoboVMSpilSdk (IOSApplication.Delegate delegate){
		this(delegate, null);
	}

	public IosRoboVMSpilSdk (IOSApplication.Delegate delegate, SpilLifecycleListener listener){
		tracking = new IosRoboVMTrack();
		setSpilLifecycleListener(listener);
	}

	@Override public void setSpilLifecycleListener (SpilLifecycleListener listener) {
		lifecycleListener = listener;
		if (lifecycleListener != null) {
			lifecycleListener.initialized(this);
		}
	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_ROBOVM;
	}

	@Override public void setDebug (boolean debug) {
		Spil.setAdvancedLoggingEnabled(debug);
	}

	@Override public String getSpilUserID () {
		return Spil.getSpilUserId();
	}

	@Override public void requestOtherUsersGameState(String provider, Array<String> userIds) {
		if (provider == null) {
			Gdx.app.error(TAG, "Provider name cannot be null!");
			return;
		}
		if (userIds == null) {
			Gdx.app.error(TAG, "userIds cannot be null!");
			return;
		}
		if (userIds.size == 0) return;

		NSMutableArray<NSString> list = new NSMutableArray<>();
		for (String userId : userIds) {
			list.add(new NSString(userId));
		}

		Spil.getOtherUsersGameState(provider, list);
	}

	@Override public String getUserID () {
		return Spil.getUserId();
	}

	@Override public String getUserProvider () {
		return Spil.getUserProvider();
	}

	@Override public void setUserID (String providerId, String userId) {
		Spil.setUserID(userId, providerId);
	}

	@Override public String getPublicGameState () {
		return Spil.getPublicGameState();
	}

	@Override public void setPublicGameState (String publicGameState) {
		Spil.setPublicGameState(publicGameState);
	}

	@Override public String getPrivateGameState () {
		return Spil.getPrivateGameState();
	}

	@Override public void setPrivateGameState (String privateGameState) {
		Spil.setPrivateGameState(privateGameState);
	}

	@Override public void setSpilGameStateListener (final SpilGameStateListener gameStateListener) {
		delegate().gameStateListener = gameStateListener;
	}

	@Override public void setSpilAutomatedEventsListener (SpilAutomatedEventsListener automatedEventsListener) {
		delegate().automatedEventsListener = automatedEventsListener;
	}

	@Override public IosRoboVMTrack track () {
		return tracking;
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

	@Override public void requestConfig () {
		// TODO need to update ios sdk for this
	}

	@Override public JsonValue getConfig () {
		return toJson(Spil.getConfig());
	}

	@Override public void setSpilConfigLDataListener (SpilConfigDataListener listener) {
		delegate().configDataListener = listener;
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

	@Override public void setSpilNotificationDataListener (SpilNotificationDataListener rewardListener) {
		delegate().notificationDataListener = rewardListener;
	}

	@Override public void setSpilAdListener (SpilAdListener adCallbacks) {
		delegate().adListener = adCallbacks;
	}

	private SpilDelegate delegate () {
		if (delegate == null) {
			delegate = new SpilDelegate();
			Spil instance = Spil.getInstance();
			instance.setDelegate(delegate);
		}
		return delegate;
	}

	@Override public boolean isAdProviderInitialized (String provider) {
		return Spil.isAdProviderInitialized(provider);
	}

	@Override public void showToastOnVideoReward (boolean enabled) {
		Spil.showToastOnVideoReward(enabled);
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

	public void registerPushNotifications() {
		Spil.registerPushNotifications();
	}

	public void didRegisterForRemoteNotifications(NSData deviceToken) {
		Spil.didRegisterForRemoteNotificationsWithDeviceToken(deviceToken);
	}

	@Override public void requestGameData () {
		// its there in .m but not in header
		// it also is just an 'requestGameData' event, so we could do that?
		Spil.requestGameData();
	}

	@Override public void requestPlayerData () {
		Spil.requestPlayerData();
	}

	@Override public void updatePlayerData () {
		Spil.updatePlayerData();
	}

	@Override public void setSpilPlayerDataListener (SpilPlayerDataListener playerDataListener) {
		delegate().playerDataListener = playerDataListener;
	}

	@Override public void setSpilGameDataListener (SpilGameDataListener gameDataListener) {
		delegate().gameDataListener = gameDataListener;
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


	// customer support
	@Override public void showZendeskHelpCenter () {
		Spil.showHelpCenter();
	}

	@Override public void showZendeskWebViewHelpCenter () {
		Spil.showHelpCenterWebview();
	}

	@Override public void showZendeskContactCenter () {
		Spil.showContactCenter();
	}

	// web
	@Override public void requestDailyBonus () {
		Spil.requestDailyBonus();
	}

	@Override public void setSpilDailyBonusListener (SpilDailyBonusListener listener) {
		delegate().dailyBonusListener = listener;
	}

	@Override public void requestSplashScreen () {
		Spil.requestSplashScreen();
	}

	@Override public void setSpilSplashScreenListener (SpilSplashScreenListener listener) {
		delegate().splashScreenListener = listener;
	}

	// ios only
	public void setCustomBundleId (String bundleIdentifier) {
		Spil.setCustomBundleId(bundleIdentifier);
	}

	private class SpilDelegate extends SpilDelegateAdapter {
		SpilAdListener adListener;
		SpilNotificationDataListener notificationDataListener;
		SpilGameDataListener gameDataListener;
		SpilPlayerDataListener playerDataListener;
		SpilConfigDataListener configDataListener;
		SpilGameStateListener gameStateListener;
		SpilAutomatedEventsListener automatedEventsListener;
		SpilDailyBonusListener dailyBonusListener;
		SpilSplashScreenListener splashScreenListener;

		@Override public void adAvailable (String type) {
			if (adListener != null) adListener.adAvailable(type);
		}

		@Override public void adNotAvailable (String type) {
			if (adListener != null) adListener.adNotAvailable(type);
		}

		@Override public void adStart () {
			if (adListener != null) adListener.adStart();
		}

		@Override public void adFinished (String type, String reason, String reward, String network) {
			if (adListener != null) {
				JsonValue root = new JsonValue(JsonValue.ValueType.object);
				JsonValue typeValue = new JsonValue(type);
				typeValue.name = "type";
				JsonValue reasonValue = new JsonValue(reason);
				reasonValue.name = "reason";
				JsonValue networkValue = new JsonValue(network);
				networkValue.name = "network";
				root.child = typeValue;
				typeValue.next = reasonValue;
				reasonValue.prev = typeValue;
				reasonValue.next = networkValue;
				if (reward != null) {
					JsonValue rewardValue = new JsonValue(reward);
					rewardValue.name = "reward";
					networkValue.prev = rewardValue;
					networkValue.next = rewardValue;
					rewardValue.prev = networkValue;
				}
				adListener.adFinished(root);
			}
		}

		@Override public void grantReward (NSDictionary<?, ?> reward) {
			if (notificationDataListener != null) notificationDataListener.onRewardReceived(toJson(reward));
		}

		@Override public void packagesLoaded () {
			// NOTE there is no equivalent on android side
		}

		@Override public void spilGameDataAvailable () {
			if (gameDataListener != null) gameDataListener.gameDataAvailable();
		}

		@Override public void spilGameDataError (String message) {
			if (gameDataListener != null) playerDataListener.playerDataError(convertErrorMessage(message));
		}

		@Override public void playerDataAvailable () {
			if (playerDataListener != null) playerDataListener.playerDataAvailable();
		}

		@Override public void playerDataError (String message) {
			if (playerDataListener != null) playerDataListener.playerDataError(convertErrorMessage(message));
		}

		@Override public void playerDataUpdated (String reason, String updatedData) {
			if (playerDataListener != null) playerDataListener.playerDataUpdated(reason, toJson(updatedData));
		}

		@Override public void configUpdated () {
			if (configDataListener != null) configDataListener.configDataUpdated();
		}

		@Override public void gameStateUpdated (String access) {
			if (gameStateListener != null) gameStateListener.gameStateUpdated(access);
		}

		@Override public void otherUsersGameStateLoaded (NSDictionary<?, ?> data, String provider) {
			if (gameStateListener != null) gameStateListener.otherUsersGameStateLoaded(provider, toJson(data));
		}

		@Override public void gameStateError (String message) {
			if (gameStateListener != null) gameStateListener.gameStateError(convertErrorMessage(message));
		}

		@Override public void openGameShop () {
			if (automatedEventsListener != null) automatedEventsListener.openGameShop();
		}

		@Override public void splashScreenOpen () {
			if (splashScreenListener != null) splashScreenListener.splashScreenOpen();
		}

		@Override public void splashScreenNotAvailable () {
			if (splashScreenListener != null) splashScreenListener.splashScreenNotAvailable();
		}

		@Override public void splashScreenClosed () {
			if (splashScreenListener != null) splashScreenListener.splashScreenClosed();
		}

		@Override public void splashScreenOpenShop () {
			if (splashScreenListener != null) splashScreenListener.splashScreenOpenShop();
		}

		@Override public void splashScreenError (String message) {
			if (splashScreenListener != null) splashScreenListener.splashScreenError(convertErrorMessage(message));
		}

		@Override public void dailyBonusOpen () {
			if (dailyBonusListener != null) dailyBonusListener.dailyBonusOpen();
		}

		@Override public void dailyBonusNotAvailable () {
			if (dailyBonusListener != null) dailyBonusListener.dailyBonusNotAvailable();
		}

		@Override public void dailyBonusClosed () {
			if (dailyBonusListener != null) dailyBonusListener.dailyBonusClosed();
		}

		@Override public void dailyBonusReward (NSDictionary<?, ?> data) {
			if (dailyBonusListener != null) dailyBonusListener.dailyBonusReward(toJson(data));
		}

		@Override public void dailyBonusError (String message) {
			if (dailyBonusListener != null) dailyBonusListener.dailyBonusError(convertErrorMessage(message));
		}

		SpilErrorCode convertErrorMessage(String message) {
			JsonValue value = toJson(message);
			if (value != null) {
				int id = value.getInt("id", -1);
				SpilErrorCode.fromId(id);
			}
			return SpilErrorCode.Invalid;
		}
	}
}
