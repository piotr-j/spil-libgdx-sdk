package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObjectProtocol;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.annotation.NativeClass;
import org.robovm.rt.bro.annotation.Library;

/**
 * Created by PiotrJ on 19/07/16.
 */

@Library(Library.INTERNAL)
@NativeClass
public interface SpilDelegate extends NSObjectProtocol {

	// Ad events
	// >>> type = interstitial/rewardVideo
	// >>> reason = close/dismiss/error etc
	// >>> reward = reward/empty
	// >>> network = DFP/Fyber/Chartboost
	@Method(selector = "adAvailable:")
	void adAvailable(String type);

	@Method(selector = "adNotAvailable:")
	void adNotAvailable(String type);

	@Method(selector = "adStart")
	void adStart(String type);

	@Method(selector = "adFinished:reason:reward:network:")
	void adFinished(String type, String reason, String reward, String network);

	// Notification events
	@Method(selector = "notificationReward:")
	void notificationReward(NSDictionary<?, ?> reward);

	// Package events
	@Method(selector = "packagesLoaded")
	void packagesLoaded();

	// Game data events
	@Method(selector = "spilGameDataAvailable")
	void spilGameDataAvailable();

	@Method(selector = "spilGameDataError:")
	void spilGameDataError(String message);

	// Player data events
	@Method(selector = "playerDataAvailable")
	void playerDataAvailable();

	@Method(selector = "playerDataError:")
	void playerDataError(String message);

	@Method(selector = "playerDataUpdated:")
	void playerDataUpdated(String reason);
}
