package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
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

	/**
	 *  Show debug logs
	 *
	 *  @param debugEnabled Enables or disables the debug logs printed
	 */
	@Method(selector = "debug:")
	public static native void debug(boolean debugEnabled);

	/**
	 *  Initiates the API
	 */
	@Method(selector = "start")
	public static native void start();

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
	 *  of the key. Type must be NSString.
	 * @return returns the object from a key, only first hiergy
	 */
	@Method(selector = "getConfigValue:")
	public static native NSString getConfigValue(String key);

	/**
	 *  Handle remote notification packages
	 *
	 *  @param application     Reference to the UIApplication object
	 *  @param userInfo        Reference to the push notification payload
	 */
	@Method(selector = "application:didReceiveRemoteNotification:")
	public static native void didReceiveRemoteNotification(UIApplication application, NSDictionary userInfo);

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

	@Method(selector = "addCurrencyToWallet:")
	public static native void addCurrencyToWallet(int currencyId, int amount, String reason);//	+(void)addCurrencyToWallet:(int)currencyId withAmount:(int)amount withReason:(NSString*)reason;

	@Method(selector = "subtractCurrencyFromWallet:")
	public static native void subtractCurrencyFromWallet(int currencyId, int amount, String reason);

	@Method(selector = "addItemToInventory:withAmount:withReason:")
	public static native void addItemToInventory(int itemId, int amount, String reason);

	@Method(selector = "subtractItemFromInventory:withAmount:withReason:")
	public static native void subtractItemFromInventory(int itemId, int amount, String reason);

	@Method(selector = "consumeBundle:withReason:")
	public static native void consumeBundle(int bundleId, String reason);
}
