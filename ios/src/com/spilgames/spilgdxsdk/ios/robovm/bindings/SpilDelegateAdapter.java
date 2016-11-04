package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.objc.annotation.NotImplemented;

/**
 * Created by PiotrJ on 19/07/16.
 */
public class SpilDelegateAdapter extends NSObject implements SpilDelegate {

	@NotImplemented("adAvailable:")
	@Override public void adAvailable (String type) {

	}

	@NotImplemented("adNotAvailable:")
	@Override public void adNotAvailable (String type) {

	}

	@NotImplemented("adStart")
	@Override public void adStart () {

	}

	@NotImplemented("adFinished:reason:reward:network:")
	@Override public void adFinished (String type, String reason, String reward, String network) {

	}

	@NotImplemented("grantReward:")
	@Override public void grantReward (NSDictionary<?, ?> reward) {

	}

	@NotImplemented("splashScreenOpen")
	@Override public void splashScreenOpen () {

	}

	@NotImplemented("splashScreenNotAvailable")
	@Override public void splashScreenNotAvailable () {

	}

	@NotImplemented("splashScreenClosed")
	@Override public void splashScreenClosed () {

	}

	@NotImplemented("splashScreenOpenShop")
	@Override public void splashScreenOpenShop () {

	}

	@NotImplemented("splashScreenError:")
	@Override public void splashScreenError (String message) {

	}

	@NotImplemented("dailyBonusOpen")
	@Override public void dailyBonusOpen () {

	}

	@NotImplemented("dailyBonusNotAvailable")
	@Override public void dailyBonusNotAvailable () {

	}

	@NotImplemented("dailyBonusClosed")
	@Override public void dailyBonusClosed () {

	}

	@NotImplemented("dailyBonusReward:")
	@Override public void dailyBonusReward (NSDictionary<?, ?> data) {

	}

	@NotImplemented("dailyBonusError:")
	@Override public void dailyBonusError (String message) {

	}

	@NotImplemented("packagesLoaded")
	@Override public void packagesLoaded () {

	}

	@NotImplemented("spilGameDataAvailable")
	@Override public void spilGameDataAvailable () {

	}

	@NotImplemented("spilGameDataError:")
	@Override public void spilGameDataError (String message) {

	}

	@NotImplemented("playerDataAvailable")
	@Override public void playerDataAvailable () {

	}

	@NotImplemented("playerDataError:")
	@Override public void playerDataError (String message) {

	}

	@NotImplemented("playerDataUpdated:updatedData:")
	@Override public void playerDataUpdated (String reason, String updatedData) {

	}

	@NotImplemented("configDataUpdated")
	@Override public void configUpdated () {

	}

	@NotImplemented("configDataUpdated")
	@Override public void gameStateUpdated (String access) {

	}

	@NotImplemented("configDataUpdated")
	@Override public void otherUsersGameStateLoaded (NSDictionary<?, ?> data, String provider) {

	}

	@NotImplemented("configDataUpdated")
	@Override public void gameStateError (String message) {

	}

	@NotImplemented("configDataUpdated")
	@Override public void openGameShop () {

	}
}
