package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Shortcuts for tracking various kinds of events
 *
 * Created by PiotrJ on 12/09/16.
 */
public abstract class Track {
	protected Json json;
	public Track () {
		json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);
		json.setUsePrototypes(false);
		json.setSerializer(SpilCurrency.class, new SpilCurrency.JsonSerializer());
		json.setSerializer(SpilItem.class, new SpilItem.JsonSerializer());
	}

	/**
	 * @param skuId             The product identifier of the item that was purchased
	 * @param transactionId     The transaction identifier of the item that was purchased (also called orderId)
	 * @param purchaseDate      The date and time that the item was purchased
	 */
	public abstract void IAPPurchasedEvent(String skuId, String transactionId, String purchaseDate);

	/**
	 * @param skuId                 The product identifier of the item that was purchased
	 * @param originalTransactionId For a transaction that restores a previous transaction, the transaction identifier of the original transaction.
	 *                              Otherwise, identical to the transaction identifier
	 * @param originalPurchaseDate  For a transaction that restores a previous transaction, the date of the original transaction
	 */
	public abstract void IAPRestoredEvent(String skuId, String originalTransactionId, String originalPurchaseDate);

	/**
	 * @param skuId     The product identifier of the item that was purchased
	 * @param error     Error description or error code
	 */
	public abstract void IAPFailedEvent(String skuId, String error);

	/**
	 * @param currencies  	A list containing the {@link SpilCurrency}s that have been changed with the event.
	 * @param items     		A list containing the {@link SpilItem}s that have been changed with the event.
	 * @param reason        The reason for which the wallet or the inventory has been updated
	 *                      A list of default resons can be found here: {@link com.spilgames.spilgdxsdk.SpilDataUpdateReasons}
	 * @param location      The location where the event occurred (ex.: Shop Screen, End of the level Screen)
	 */
	public abstract void walletInventoryEvent(Array<SpilCurrency> currencies, Array<SpilItem> items, String reason, String location);

	/**
	 * @param name          The name of the milestone
	 */
	public abstract void milestoneEvent(String name);

	/**
	 * @param level         The name of the level
	 */
	public abstract void levelStartEvent(String level);

	/**
	 * @param level         The name of the level
	 * @param score         The final score the player achieves at the end of the level
	 * @param stars         The # of stars (or any other rating system) the player achieves at the end of the level
	 * @param turns         The # of moves/turns taken to complete the level
	 */
	public abstract void levelCompleteEvent(String level, String score, String stars, String turns);

	/**
	 * @param level         The name of the level
	 * @param score         The final score the player achieves at the end of the level
	 * @param turns         The # of moves/turns taken to complete the level
	 */
	public abstract void levelFailed(String level, String score, String turns);

	/**
	 * Track the completion of a tutorial
	 */
	public abstract void tutorialCompleteEvent();

	/**
	 * Track the skipping of a tutorial
	 */
	public abstract void tutorialSkippedEvent();

	/**
	 * @param platform      The platform for which the registration occurred (ex.: Facebook)
	 */
	public abstract void registerEvent(String platform);

	/**
	 * @param platform      The platform for which the share occurred (ex.: Facebook)
	 */
	public abstract void shareEvent(String platform);

	/**
	 * @param platform      The platform for which the invite occurred (ex.: Facebook)
	 */
	public abstract void inviteEvent(String platform);


	public String currenciesJson (Array<SpilCurrency> currencies) {
		if (json == null) json = new Json();
		if (currencies == null) {
			return "[]";
		}
		return json.toJson(currencies);
	}

	public String itemsJson (Array<SpilItem> items) {
		if (json == null) json = new Json();
		if (items == null) {
			return "[]";
		}
		return json.toJson(items);
	}
}
