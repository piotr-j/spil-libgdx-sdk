package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.TimeUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilCurrency;
import com.spilgames.spilgdxsdk.SpilDataUpdateReasons;
import com.spilgames.spilgdxsdk.SpilItem;
import com.spilgames.spilgdxsdk.Track;

import java.util.Date;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class TrackingScreen extends BackScreen {
	private final static String TAG = TrackingScreen.class.getSimpleName();
	Array<String> otherUserIds = new Array<String>();

	public TrackingScreen (final SpilGame game) {
		super(game);

		content.defaults().pad(5);
		content.add(new VisLabel("Track shortcuts")).row();
		Track track = spilSdk.track();
		// TODO do we want controls for values? quite a lot of those...
		// TODO no activity for this in android test app
		// what sort of format this date is supposed to be in?
		String pre = new Date(TimeUtils.millis() - 1000).toString();
		String now = new Date(TimeUtils.millis()).toString();
		track.IAPPurchasedEvent("skuId1", "transactionId", pre);
		track.IAPRestoredEvent("skuId1", pre, now);
		track.IAPFailedEvent("skuId2", "error");
		track.milestoneEvent("milestone1");
		track.levelStartEvent("level1");
		track.levelCompleteEvent("level1", "10", "5", "1");
		// this does not like malformed json
//		track.walletInventoryEvent(null, null, SpilDataUpdateReasons.LevelComplete, "end screen1");
		Array<SpilCurrency> currencies = new Array<SpilCurrency>();
		currencies.add(new SpilCurrency("currency1", 5, 5, 1));
		Array<SpilItem> items = new Array<SpilItem>();
		items.add(new SpilItem("item1", 1, 3, 0));

		Gdx.app.log(TAG, track.currenciesJson(currencies));
		Gdx.app.log(TAG, track.itemsJson(items));

		track.walletInventoryEvent(currencies, items, SpilDataUpdateReasons.LevelComplete, "end screen1");

		track.levelStartEvent("level2");
		track.levelFailed("level2", "0", "0");
		track.tutorialCompleteEvent();
		track.tutorialSkippedEvent();
		track.registerEvent("platform1");
		track.shareEvent("platform1");
		track.inviteEvent("platform1");

	}
}
