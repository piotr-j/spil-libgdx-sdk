package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;

/**
 * Created by PiotrJ on 19/07/16.
 */
public class SpilDelegateAdapter extends NSObject implements SpilDelegate {

	@Override public void adAvailable (String type) {

	}

	@Override public void adNotAvailable (String type) {

	}

	@Override public void adStart (String type) {

	}

	@Override public void adFinished (String type, String reason, String reward, String network) {

	}

	@Override public void notificationReward (NSDictionary<NSString, NSString> reward) {

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
