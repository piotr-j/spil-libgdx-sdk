package com.spilgames.spilgdxsdk.android;

import android.content.Context;
import com.badlogic.gdx.utils.Array;
import com.spilgames.spilgdxsdk.SpilCurrency;
import com.spilgames.spilgdxsdk.SpilItem;
import com.spilgames.spilgdxsdk.Track;
import com.spilgames.spilsdk.events.DefaultEvents;

/**
 * Created by PiotrJ on 12/09/16.
 */
public class AndroidTrack extends Track {
	DefaultEvents events;

	public AndroidTrack (Context context) {
		events = new DefaultEvents(context);
	}

	@Override public void IAPPurchasedEvent (String skuId, String transactionId, String purchaseDate) {
		events.TrackIAPPurchasedEvent(skuId, transactionId, purchaseDate);
	}

	@Override public void IAPRestoredEvent (String skuId, String originalTransactionId, String originalPurchaseDate) {
		events.TrackIAPRestoredEvent(skuId, originalTransactionId, originalPurchaseDate);
	}

	@Override public void IAPFailedEvent (String skuId, String error) {
		events.TrackIAPFailedEvent(skuId, error);
	}

	@Override public void walletInventoryEvent (Array<SpilCurrency> currencies, Array<SpilItem> items, String reason, String location) {
		events.TrackWalletInventoryEvent(currenciesJson(currencies), itemsJson(items), reason, location);
	}


	@Override public void milestoneEvent (String name) {
		events.TrackMilestoneEvent(name);
	}

	@Override public void levelStartEvent (String level) {
		events.TrackLevelStartEvent(level);
	}

	@Override public void levelCompleteEvent (String level, String score, String stars, String turns) {
		events.TrackLevelCompleteEvent(level, score, stars, turns);
	}

	@Override public void levelFailed (String level, String score, String turns) {
		events.TrackLevelFailed(level, score, turns);
	}

	@Override public void tutorialCompleteEvent () {
		events.TrackTutorialCompleteEvent();
	}

	@Override public void tutorialSkippedEvent () {
		events.TrackTutorialSkippedEvent();
	}

	@Override public void registerEvent (String platform) {
		events.TrackRegisterEvent(platform);
	}

	@Override public void shareEvent (String platform) {
		events.TrackShareEvent(platform);
	}

	@Override public void inviteEvent (String platform) {
		events.TrackInviteEvent(platform);
	}
}
