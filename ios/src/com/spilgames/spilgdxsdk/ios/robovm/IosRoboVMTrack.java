package com.spilgames.spilgdxsdk.ios.robovm;

import com.badlogic.gdx.utils.Array;
import com.spilgames.spilgdxsdk.SpilCurrency;
import com.spilgames.spilgdxsdk.SpilItem;
import com.spilgames.spilgdxsdk.Track;
import com.spilgames.spilgdxsdk.ios.robovm.bindings.Spil;

/**
 * Created by PiotrJ on 12/09/16.
 */
public class IosRoboVMTrack extends Track {
	@Override public void IAPPurchasedEvent (String skuId, String transactionId, String purchaseDate) {
		Spil.trackIAPPurchasedEvent(skuId, transactionId, purchaseDate);
	}

	@Override public void IAPRestoredEvent (String skuId, String originalTransactionId, String originalPurchaseDate) {
		Spil.trackIAPRestoredEvent(skuId, originalTransactionId, originalPurchaseDate);
	}

	@Override public void IAPFailedEvent (String skuId, String error) {
		Spil.trackIAPFailedEvent(skuId, error);
	}

	@Override public void walletInventoryEvent (Array<SpilCurrency> currencies, Array<SpilItem> items, String reason, String location) {
		Spil.trackWalletInventoryEvent(currenciesJson(currencies), itemsJson(items), reason, location);
	}

	@Override public void milestoneEvent (String name) {
		Spil.trackMilestoneEvent(name);
	}

	@Override public void levelStartEvent (String level) {
		Spil.trackLevelStartEvent(level);
	}

	@Override public void levelCompleteEvent (String level, String score, String stars, String turns) {
		Spil.trackLevelCompleteEvent(level, score, stars, turns);
	}

	@Override public void levelFailed (String level, String score, String turns) {
		Spil.trackLevelFailed(level, score, turns);
	}

	@Override public void tutorialCompleteEvent () {
		Spil.trackTutorialCompleteEvent();
	}

	@Override public void tutorialSkippedEvent () {
		Spil.trackTutorialSkippedEvent();
	}

	@Override public void registerEvent (String platform) {
		Spil.trackRegisterEvent(platform);
	}

	@Override public void shareEvent (String platform) {
		Spil.trackShareEvent(platform);
	}

	@Override public void inviteEvent (String platform) {
		Spil.trackInviteEvent(platform);
	}
}
