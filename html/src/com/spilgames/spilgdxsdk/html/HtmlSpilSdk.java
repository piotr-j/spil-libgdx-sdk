package com.spilgames.spilgdxsdk.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.*;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class HtmlSpilSdk implements SpilSdk {
	private final static String TAG = HtmlSpilSdk.class.getSimpleName();

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.HTML;
	}

	@Override public void registerDevice (String projectID) {
		Gdx.app.log(TAG, "registerDevice ("+projectID+")");
	}

	@Override public void setDebug (boolean debug) {

	}

	@Override public void trackEvent (SpilEvent event) {

	}

	@Override public void trackEvent (SpilEvent event, SpilEventActionListener listener) {

	}

	@Override public JsonValue getConfig () {
		return null;
	}

	@Override public void requestPackages () {
		Gdx.app.log(TAG, "requestPackages");
	}

	@Override public void setSpilRewardListener (SpilRewardListener rewardListener) {
		Gdx.app.log(TAG, "SpilRewardListener ("+rewardListener+")");
	}

	@Override public JsonValue getPromotion (String packageId) {
		Gdx.app.log(TAG, "getPromotion");
		return null;
	}

	@Override public JsonValue getPackage (String packageId) {
		Gdx.app.log(TAG, "getPackage");
		return null;
	}

	@Override public JsonValue getAllPackages () {
		Gdx.app.log(TAG, "getAllPackages");
		return null;
	}

	@Override public void startChartboost (String appId, String appSignature) {

	}

	@Override public void startFyber (String appId, String token) {
		Gdx.app.log(TAG, "startFyber ("+appId+", "+token+")");
	}

	@Override public void startDFP (String adUnitId) {
		Gdx.app.log(TAG, "startDFP ("+adUnitId+")");
	}

	@Override public boolean isAdProviderInitialized (String provider) {
		return false;
	}

	@Override public void showMoreApps () {

	}

	@Override public void showRewardVideo () {

	}

	@Override public void setSpilAdCallbacks (SpilAdCallbacks adCallbacks) {

	}

	@Override public void devRequestAd (String provider, String adType, boolean parentalGate) {

	}

	@Override public void devShowRewardVideo (String provider) {

	}

	@Override public void devShowInterstitial (String provider) {

	}

	@Override public void devShowMoreApps (String provider) {

	}

	@Override public void requestGameData () {
		Gdx.app.log(TAG, "requestGameData");
	}

	@Override public void requestPlayerData () {
		Gdx.app.log(TAG, "requestPlayerData)");
	}

	@Override public void setSpilPlayerDataListener (SpilPlayerDataListener playerDataListener) {
		Gdx.app.log(TAG, "setSpilPlayerDataListener ("+playerDataListener+")");
	}

	@Override public void setSpilGameDataListener (SpilGameDataListener gameDataListener) {
		Gdx.app.log(TAG, "setSpilGameDataListener ("+gameDataListener+")");
	}

	@Override public JsonValue getUserProfile () {
		Gdx.app.log(TAG, "getUserProfile");
		return null;
	}

	@Override public JsonValue getWallet () {
		Gdx.app.log(TAG, "getWallet");
		return null;
	}

	@Override public JsonValue getGameData () {
		Gdx.app.log(TAG, "getGameData");
		return null;
	}

	@Override public JsonValue getInventory () {
		Gdx.app.log(TAG, "getInventory");
		return null;
	}

//	@Override public String getShop () {
//		Gdx.app.log(TAG, "getShop");
//		return null;
//	}
//
//	@Override public String getShopPromotions () {
//		Gdx.app.log(TAG, "getShopPromotions");
//		return null;
//	}

	@Override public void addCurrencyToWallet (int currencyId, int amount, String reason) {
		Gdx.app.log(TAG, "addCurrencyToWallet (" +currencyId+", "+amount+", "+ reason+")");
	}

	@Override public void subtractCurrencyFromWallet (int currencyId, int amount, String reason) {
		Gdx.app.log(TAG, "subtractCurrencyFromWallet (" +currencyId+", "+amount+", "+ reason+")");
	}

	@Override public void addItemToInventory (int itemId, int amount, String reason) {
		Gdx.app.log(TAG, "addItemToInventory (" +itemId+", "+amount+", "+ reason+")");
	}

	@Override public void subtractItemFromInventory (int itemId, int amount, String reason) {
		Gdx.app.log(TAG, "subtractItemFromInventory (" +itemId+", "+amount+", "+ reason+")");
	}

	@Override public void consumeBundle (int bundleId, String reason) {
		Gdx.app.log(TAG, "consumeBundle (" +bundleId+", "+ reason+")");
	}
}
