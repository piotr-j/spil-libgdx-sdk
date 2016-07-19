package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObjectProtocol;
import org.robovm.apple.foundation.NSString;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.annotation.NativeClass;
import org.robovm.rt.bro.annotation.Library;

/**
 * Created by PiotrJ on 19/07/16.
 */

@Library(Library.INTERNAL)
@NativeClass
public interface SpilDelegate extends NSObjectProtocol{

	// Ad events
	// >>> type = interstitial/rewardVideo
	// >>> reason = close/dismiss/error etc
	// >>> reward = reward/empty
	// >>> network = DFP/Fyber/Chartboost
//	-(void)adAvailable:(NSString*)type; // tells developer if an ad is available or not (type defines if it is either an interstitial or reward video)
	@Method(selector = "adAvailable:")
	void adAvailable(String type);
//	-(void)adNotAvailable:(NSString*)type; // tells the developer an ad has failed (either to load or no ads available) (type defines if it is either an interstitial or reward video)
	@Method(selector = "adNotAvailable:")
	void adNotAvailable(String type);
//	-(void)adStart; // tells the developer an ad has started showing
	@Method(selector = "adStart")
	void adStart(String type);
//	-(void)adFinished:(NSString*)type reason:(NSString*)reason reward:(NSString*)reward network:(NSString*)network; //- tells the developer an ad finished showing ( can either be dismissed and no reward will be given, or the video was watched completely and a reward will be presented)
	@Method(selector = "adFinished:reason:reward:network:")
	void adFinished(String type, String reason, String reward, String network);

	// Notification events
//	-(void)notificationReward:(NSDictionary*)reward;
	// TODO proper types?
	@Method(selector = "notificationReward:")
	void notificationReward(NSDictionary<NSString, NSString> reward);

	// Package events
//	-(void)packagesLoaded;
	@Method(selector = "packagesLoaded")
	void packagesLoaded();

	// Game data events
//	-(void)spilGameDataAvailable;
	@Method(selector = "spilGameDataAvailable")
	void spilGameDataAvailable();
//	-(void)spilGameDataError:(NSString*)message;
	@Method(selector = "spilGameDataError:")
	void spilGameDataError(String message);

	// Player data events
//	-(void)playerDataAvailable;
	@Method(selector = "playerDataAvailable")
	void playerDataAvailable();
//	-(void)playerDataError:(NSString*)message;
	@Method(selector = "playerDataError:")
	void playerDataError(String message);
//	-(void)playerDataUpdated:(NSString*)reason;
	@Method(selector = "playerDataUpdated:")
	void playerDataUpdated(String reason);

}
