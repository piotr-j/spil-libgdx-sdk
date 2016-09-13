package com.spilgames.spilgdxsdk.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.spilgames.spilgdxsdk.SpilCurrency;
import com.spilgames.spilgdxsdk.SpilItem;
import com.spilgames.spilgdxsdk.Track;

/**
 * Created by PiotrJ on 12/09/16.
 */
public class DesktopTrack extends Track {
	private final static String TAG = DesktopTrack.class.getSimpleName();
	boolean log;

	private void log (String message) {
		if (log) Gdx.app.log(TAG, message);
	}

	@Override public void IAPPurchasedEvent (String skuId, String transactionId, String purchaseDate) {
		log("IAPPurchasedEvent (" + skuId + ", " + transactionId + ", " + purchaseDate + " )");
	}

	@Override public void IAPRestoredEvent (String skuId, String originalTransactionId, String originalPurchaseDate) {
		log("IAPRestoredEvent (" + skuId + ", " + originalPurchaseDate + ", " + originalPurchaseDate + " )");
	}

	@Override public void IAPFailedEvent (String skuId, String error) {
		log("IAPFailedEvent (" + skuId + ", " + error + " )");
	}

	@Override public void walletInventoryEvent (Array<SpilCurrency> currencies, Array<SpilItem> items, String reason, String location) {
		log("walletInventoryEvent (" + currencies + ", " + items + ", " + reason + ", " + location + " )");
	}

	@Override public void milestoneEvent (String name) {
		log("milestoneEvent (" + name + " )");
	}

	@Override public void levelStartEvent (String level) {
		log("levelStartEvent ( " + level + " )");
	}

	@Override public void levelCompleteEvent (String level, String score, String stars, String turns) {
		log("levelCompleteEvent ( " + level + ", " + score + ", " + stars + ", " + turns + " )");
	}

	@Override public void levelFailed (String level, String score, String turns) {
		log("levelFailed ( " + level + ", " + score + ", " + turns + " )");
	}

	@Override public void tutorialCompleteEvent () {
		log("tutorialCompleteEvent ()");
	}

	@Override public void tutorialSkippedEvent () {
		log("tutorialSkippedEvent ()");
	}

	@Override public void registerEvent (String platform) {
		log("registerEvent ( " + platform + " )");
	}

	@Override public void shareEvent (String platform) {
		log("shareEvent ( " + platform + " )");
	}

	@Override public void inviteEvent (String platform) {
		log("inviteEvent ( " + platform + " )");
	}
}
