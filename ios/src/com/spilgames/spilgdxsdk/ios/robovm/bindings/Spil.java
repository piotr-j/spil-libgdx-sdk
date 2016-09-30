package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.objc.annotation.Block;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.annotation.NativeClass;
import org.robovm.objc.annotation.Property;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.rt.bro.annotation.Library;

/**
 * RoboVM Bindings for Spil.h
 *
 * Created by PiotrJ on 13/07/16.
 */
@Library(Library.INTERNAL)
@NativeClass
public class Spil extends NSObject {

	@Method(selector = "sharedInstance")
	public static native Spil getInstance ();

	@Property(selector = "delegate")
	public native SpilDelegate getDelegate();

	@Property(selector = "setDelegate:", strongRef = true)
	public native void setDelegate(SpilDelegate delegate);

	@Method(selector = "getSpilUserId")
	public static native String getSpilUserId();

	// User data

	@Method(selector = "getUserId")
	public static native String getUserId();

	/**
	 * Get the custom provider id
	 *
	 * @return user provider or null
	 */
	@Method(selector = "getUserProvider")
	public static native String getUserProvider();

	/**
	 *  Set a custom user id for a specified service.
	 *
	 *  @param userId The social user id to use
	 *  @param providerId The id of the service (e.g. facebook)
	 */
	@Method(selector = "setUserId:forProviderId:")
	public static native void setUserID(String userId, String providerId);

	/**
	 *  Set private game state data.
	 *
	 *  @param privateData The private data to store
	 */
	@Method(selector = "setPrivateGameState:")
	public static native void setPrivateGameState(String privateData);

	/**
	 *  Get private game state data.
	 *
	 *  @return private game state or null
	 */
	@Method(selector = "getPrivateGameState")
	public static native String getPrivateGameState();

	/**
	 *  Set public game state data.
	 *
	 *  @param publicData The public data to store
	 */
	@Method(selector = "setPublicGameState:")
	public static native void setPublicGameState(String publicData);

	/**
	 *  Get public game state data.
	 *
	 *  @return public game state or null
	 */
	@Method(selector = "getPublicGameState")
	public static native String getPublicGameState();

	/**
	 *  Get the public game state data of other users,
	 *  based on the user id of a custom provider.
	 *
	 *  @param provider The provider to request the data from
	 *  @param userIds The user ids
	 */
	@Method(selector = "getOtherUsersGameState:userIds:")
	public static native void getOtherUsersGameState(String provider, NSArray<NSString> userIds);

	/**
	 *  Show advanced logs
	 *
	 *  @param advancedLoggingEnabled Enables or disables the advanced logs printed
	 */
	@Method(selector = "setAdvancedLoggingEnabled:")
	public static native void setAdvancedLoggingEnabled(boolean advancedLoggingEnabled);

	/**
	 *  Initiates the API
	 */
	@Method(selector = "start")
	public static native void start();

	/**
	 * @param skuId             The product identifier of the item that was purchased
	 * @param transactionId     The transaction identifier of the item that was purchased (also called orderId)
	 * @param purchaseDate      The date and time that the item was purchased
	 */
	@Method(selector = "trackIAPPurchasedEvent:transactionId:purchaseDate:")
	public static native void trackIAPPurchasedEvent(String skuId, String transactionId, String purchaseDate);

	/**
	 * @param skuId                 The product identifier of the item that was purchased
	 * @param originalTransactionId For a transaction that restores a previous transaction, the transaction identifier of the original transaction.
	 *                              Otherwise, identical to the transaction identifier
	 * @param originalPurchaseDate  For a transaction that restores a previous transaction, the date of the original transaction
	 */
	@Method(selector = "trackIAPRestoredEvent:originalTransactionId:originalPurchaseDate:")
	public static native void trackIAPRestoredEvent(String skuId, String originalTransactionId, String originalPurchaseDate);

	/**
	 * @param skuId     The product identifier of the item that was purchased
	 * @param error     Error description or error code
	 */
	@Method(selector = "trackIAPFailedEvent:error:")
	public static native void trackIAPFailedEvent(String skuId, String error);

	// TODO lists are supposed to be jsons of TrackingCurrency and TrackingItem instances, though these dont seem to be a thing in ios sdk, present on android
	// TODO lets assume that the general data present in both is the same
	/**
	 * @param currencyList  A list containing the currency objects that have been changed with the event.
	 * @param itemsList     A list containing the item objects that have been changed with the event.
	 * @param reason        The reason for which the wallet or the inventory has been updated
	 *                      A list of default resons can be found here: {@link com.spilgames.spilgdxsdk.SpilDataUpdateReasons}
	 * @param location      The location where the event occurred (ex.: Shop Screen, End of the level Screen)
	 */
	@Method(selector = "trackWalletInventoryEvent:itemsList:reason:location:")
	public static native void trackWalletInventoryEvent(String currencyList, String itemsList, String reason, String location);

	/**
	 * @param name          The name of the milestone
	 */
	@Method(selector = "trackMilestoneEvent:")
	public static native void trackMilestoneEvent(String name);

	/**
	 * @param level         The name of the level
	 */
	@Method(selector = "trackLevelStartEvent:")
	public static native void trackLevelStartEvent(String level);

	/**
	 * @param level         The name of the level
	 * @param score         The final score the player achieves at the end of the level
	 * @param stars         The # of stars (or any other rating system) the player achieves at the end of the level
	 * @param turns         The # of moves/turns taken to complete the level
	 */
	@Method(selector = "trackLevelCompleteEvent:score:stars:turns:")
	public static native void trackLevelCompleteEvent(String level, String score, String stars, String turns);

	/**
	 * @param level         The name of the level
	 * @param score         The final score the player achieves at the end of the level
	 * @param turns         The # of moves/turns taken to complete the level
	 */
	@Method(selector = "trackLevelFailed:score:turns:")
	public static native void trackLevelFailed(String level, String score, String turns);

	/**
	 * Track the completion of a tutorial
	 */
	@Method(selector = "trackTutorialCompleteEvent")
	public static native void trackTutorialCompleteEvent();

	/**
	 * Track the skipping of a tutorial
	 */
	@Method(selector = "trackTutorialSkippedEvent")
	public static native void trackTutorialSkippedEvent();

	/**
	 * @param platform      The platform for which the registration occurred (ex.: Facebook)
	 */
	@Method(selector = "trackRegisterEvent:")
	public static native void trackRegisterEvent(String platform);

	/**
	 * @param platform      The platform for which the share occurred (ex.: Facebook)
	 */
	@Method(selector = "trackShareEvent:")
	public static native void trackShareEvent(String platform);

	/**
	 * @param platform      The platform for which the invite occurred (ex.: Facebook)
	 */
	@Method(selector = "trackInviteEvent:")
	public static native void trackInviteEvent(String platform);

	/**
	 *  Track a basic named event
	 *
	 *  @param name The name of the event. Replace spaces with an underscore
	 */
	@Method(selector = "trackEvent:")
	public static native void trackEvent(String name);

	/**
	 *  Track a named events with a key / value object
	 *
	 *  @param name The name of the event. Replace spaces with an underscore
	 *  @param params A key value dictionary holding the params
	 */
	@Method(selector = "trackEvent:withParameters:")
	public static native void trackEvent(String name, NSDictionary<?, ?> params);

	/**
	 *  Track a basic named event with a response
	 *
	 *  @param name  The name of the event. Replace spaces with an underscore
	 *  @param block A block with response param that will be executed when the server sends a reponse on the tracked event
	 */
	@Method(selector = "trackEvent:onResponse:")
	public static native void trackEvent(String name, @Block VoidBlock1<NSObject> block);

	/**
	 *  Track a named event params and a response
	 *
	 *  @param name   The name of the event. Replace spaces with an underscore
	 *  @param params A key value dictionary holding the params
	 *  @param block  A block with response param that will be executed when the server sends a reponse on the tracked event
	 */
	@Method(selector = "trackEvent:withParameters:onResponse:")
	public static native void trackEvent(String name, NSDictionary<?, ?> params, @Block VoidBlock1<NSObject> block);

	/**
	 *  Forwarding Delegate methods to let the Spil framework know when the app went to the background
	 *
	 *  @param application Delegate application to be passed
	 */
	@Method(selector = "applicationDidEnterBackground:")
	public static native void applicationDidEnterBackground(UIApplication application);

	/**
	 *  Forwarding Delegate methods to let the Spil framework know when the app became active again after running in background
	 *
	 *  @param application Delegate application to be passed
	 */
	@Method(selector = "applicationDidBecomeActive:")
	public static native void applicationDidBecomeActive(UIApplication application);

	@Method(selector = "isAdProviderInitialized:")
	public static native boolean isAdProviderInitialized(String identifier);

	/**
	 *  Show a toast when a reward is unlocked
	 *
	 *  @param enabled if enabled
	 */
	@Method(selector = "showToastOnVideoReward:")
	public static native void showToastOnVideoReward(boolean enabled);

	/**
	 * Get the latest stored game configuration, typically a synchronized json object coming from the server.
	 *
	 * @return NSDictionary object representation from the stored game configuration
	 */
	@Method(selector = "getConfig")
	public static native NSDictionary<?, ?> getConfig();

	/**
	 * Get a specific value from a particular key from the game configuration
	 *
	 * @param key Key of the config value. Type must be NSString.
	 * @return returns the object from a key, only first hierarchy
	 */
	@Method(selector = "getConfigValue:")
	public static native String getConfigValue(String key);

	/**
	 *  Handle remote notification packages
	 *
	 *  @param application     Reference to the UIApplication object
	 *  @param userInfo        Reference to the push notification payload
	 */
	@Method(selector = "application:didReceiveRemoteNotification:")
	public static native void didReceiveRemoteNotification(UIApplication application, NSDictionary userInfo);

	@Method(selector = "didRegisterForRemoteNotificationsWithDeviceToken:")
	public static native void didRegisterForRemoteNotificationsWithDeviceToken(NSData deviceToken);

	@Method(selector = "registerPushNotifications")
	public static native void registerPushNotifications();

	@Method(selector = "devRequestAd:withAdType:withParentalGate:")
	public static native void devRequestAd(String adProvider, String adType, boolean parentalGate);

	@Method(selector = "adProvider:")
	public static native void devShowRewardVideo(String adProvider);

	@Method(selector = "devShowInterstitial:")
	public static native void devShowInterstitial(String adProvider);

	@Method(selector = "devShowMoreApps:")
	public static native void devShowMoreApps(String adProvider);

	/**
	 * Show the more apps screen
	 */
	@Method(selector = "showMoreApps")
	public static native void showMoreApps();

	/**
	 * Show the last requested reward video
	 */
	@Method(selector = "playRewardVideo")
	public static native void playRewardVideo();

	/**
	 * Get the latest stored store packages.
	 *
	 * @return NSArray object representation from the stored store packages
	 */
	@Method(selector = "getAllPackages")
	public static native NSArray<?> getAllPackages();

	/**
	 * Get a specific package from the store
	 *
	 * @param key for the package
	 * @return returns the store package, or nil if not found
	 */
	@Method(selector = "getPackageByID:")
	public static native NSDictionary<?, ?> getPackageByID(String key);

	/**
	 * Get the latest stored store promotions.
	 *
	 * @return NSArray object representation from the stored store promotions
	 */
	@Method(selector = "getAllPromotions")
	public static native NSArray<?> getAllPromotions();

	/**
	 * Get a specific promotion from the store
	 *
	 * @param key for the promotion
	 * @return returns the store promotion, or null if not found
	 */
	@Method(selector = "getPromotionByID:")
	public static native NSDictionary<?, ?> getPromotionByID(String key);

	/**
	 * Refresh the package and promotion data
	 */
	@Method(selector = "requestPackages")
	public static native void requestPackages();

	/*
		Wallet APIs
	 */

	@Method(selector = "requestPlayerData")
	public static native void requestPlayerData();

	// NOTE this is not exposed in header for some reason, internally its 'requestGameData' event
	@Method(selector = "requestGameData")
	public static native void requestGameData();

	@Method(selector = "getUserProfile")
	public static native String getUserProfile();

	@Method(selector = "getWallet")
	public static native String getWallet();

	@Method(selector = "getSpilGameData")
	public static native String getSpilGameData();

	@Method(selector = "getInventory")
	public static native String getInventory();

	@Method(selector = "getShop")
	public static native String getShop();

	@Method(selector = "getShopPromotions")
	public static native String getShopPromotions();

	@Method(selector = "addCurrencyToWallet:withAmount:withReason:")
	public static native void addCurrencyToWallet(int currencyId, int amount, String reason);

	@Method(selector = "subtractCurrencyFromWallet:withAmount:withReason:")
	public static native void subtractCurrencyFromWallet(int currencyId, int amount, String reason);

	@Method(selector = "addItemToInventory:withAmount:withReason:")
	public static native void addItemToInventory(int itemId, int amount, String reason);

	@Method(selector = "subtractItemFromInventory:withAmount:withReason:")
	public static native void subtractItemFromInventory(int itemId, int amount, String reason);

	@Method(selector = "consumeBundle:withReason:")
	public static native void consumeBundle(int bundleId, String reason);

	// customer support
	@Method(selector = "showContactCenter")
	public static native void showContactCenter();

	@Method(selector = "showHelpCenterWebview")
	public static native void showHelpCenterWebview();

	@Method(selector = "showHelpCenter")
	public static native void showHelpCenter();
}
