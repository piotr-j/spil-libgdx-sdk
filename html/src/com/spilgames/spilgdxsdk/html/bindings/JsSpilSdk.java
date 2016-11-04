package com.spilgames.spilgdxsdk.html.bindings;

import com.google.gwt.core.client.Callback;
import com.spilgames.spilgdxsdk.*;

/**
 * Bindings for main JavaScript Spil Sdk entry point
 *
 * Created by PiotrJ on 27/10/2016.
 */
public class JsSpilSdk {
	private JsSpilSdk () {}

	public static native void init (Callback<Void, Void> callback) /*-{
		// looks like spil cant be initialized in gwt and work on the test site,
		// must be directly on the page, not in iframe this runs in
		// while we can export it, other stuff it depends on wont be
		// but how do we ensure this is initialized?
		// lets try exporting all stuff in this to $wnd? maybe it will help with ads!
		SpilSDK = $wnd.SpilSDK;
		if (typeof SpilSDK !== 'undefined' && $wnd.spilInitialized === true) {
			$entry(function(){
				// looks like it accepts only Object for generic type
				callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)();
			})();
		} else {
			$entry(function(){
				// looks like it accepts only Object for generic type
				callback.@com.google.gwt.core.client.Callback::onFailure(Ljava/lang/Object;)();
			})();
		}
	}-*/;

	public static native void trackEvent (String eventName, String data, SpilEventActionListener listener) /*-{
		// this is super janky, we convert from JsonValue to string to object and to string again in the api
		var callback = null;
		if (listener !== null) {
			callback = $entry(function(responseData) {
			   console.log("JsSpilSdk response for " + eventName + ": " + JSON.stringify(responseData));
				var response = @com.spilgames.spilgdxsdk.SpilResponseEvent::new()()
				response.@com.spilgames.spilgdxsdk.SpilResponseEvent::setName(Ljava/lang/String;)(responseData["name"]);
				listener.@com.spilgames.spilgdxsdk.SpilEventActionListener::onResponse(Lcom/spilgames/spilgdxsdk/SpilResponseEvent;)(response);
			});
		}
		if (data !== null) {
			SpilSDK.trackEvent(eventName, JSON.parse(data), callback);
		} else {
			SpilSDK.trackEvent(eventName, null, callback);
		}
	}-*/;

	public static native void requestPlayerData () /*-{
		SpilSDK.requestPlayerData();
	}-*/;

	public static native void setPlayerDataCallbacks (SpilPlayerDataListener listener) /*-{
		if (listener === null) {
			SpilSDK.setPlayerDataCallbacks(null);
			return;
		}
		SpilSDK.setPlayerDataCallbacks({
			playerDataError: $entry(function(error){
				console.log(error);
				var spilError = @com.spilgames.spilgdxsdk.SpilErrorCode::fromId(I)(error["id"] || -1);
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataError(Lcom/spilgames/spilgdxsdk/SpilErrorCode;)(spilError);
			}),
			playerDataUpdated: $entry(function(reason, updatedData){
				console.log('playerDataUpdated triggered ' + reason + ' ' + updatedData);
				var jsonReader = @com.badlogic.gdx.utils.JsonReader::new()();
				var jsonValue = jsonReader.@com.badlogic.gdx.utils.JsonReader::parse(Ljava/lang/String;)(JSON.stringify(updatedData));
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataUpdated(Ljava/lang/String;Lcom/badlogic/gdx/utils/JsonValue;)(reason, jsonValue);
			}),
			playerDataAvailable: $entry(function() {
				console.log('playerDataAvailable triggered');
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataAvailable()();
			})
		});
	}-*/;

	public static native void requestGameData () /*-{
		SpilSDK.requestGameData();
	}-*/;

	public static native void setGameDataCallbacks (SpilGameDataListener listener) /*-{
		if (listener === null) {
			SpilSDK.setGameDataCallbacks(null);
			return;
		}
		SpilSDK.setGameDataCallbacks({
			gameDataError: $entry(function (error) {
				console.log(error)
				var spilError = @com.spilgames.spilgdxsdk.SpilErrorCode::fromId(I)(error["id"] || -1);
				listener.@com.spilgames.spilgdxsdk.SpilGameDataListener::gameDataError(Lcom/spilgames/spilgdxsdk/SpilErrorCode;)(spilError);
			}),
			gameDataAvailable: $entry(function() {
				console.log('gameDataAvailable triggered');
				listener.@com.spilgames.spilgdxsdk.SpilGameDataListener::gameDataAvailable()();
			})
		});
	}-*/;

	public static native void setAdCallbacks (SpilAdListener listener) /*-{
		if (listener === null) {
			SpilSDK.setAdCallbacks(null);
			return;
		}
		SpilSDK.setAdCallbacks({
			AdAvailable: $entry(function(adType){
				console.log('AdAvailable triggered ' + adType);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adAvailable(Ljava/lang/String;)(adType);
			}),
			AdNotAvailable: $entry(function(adType){
				console.log('AdNotAvailable triggered ' + adType);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adNotAvailable(Ljava/lang/String;)(adType);
			}),
			AdStart: $entry(function(adType){
				console.log('AdStart triggered ' + adType);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adStart()();
			}),
			AdFinished: $entry(function(network, adType, reason){
				console.log('AdFinished triggered ' + network + ' ' + adType + ' ' + reason);
				var rootType = @com.badlogic.gdx.utils.JsonValue.ValueType::object;
				var jsonValue = @com.badlogic.gdx.utils.JsonValue::new(Lcom/badlogic/gdx/utils/JsonValue$ValueType;)(rootType);
				jsonValue.@com.badlogic.gdx.utils.JsonValue::addChild(Ljava/lang/String;Lcom/badlogic/gdx/utils/JsonValue;)(
					"network", @com.badlogic.gdx.utils.JsonValue::new(Ljava/lang/String;)(network)
				);
				jsonValue.@com.badlogic.gdx.utils.JsonValue::addChild(Ljava/lang/String;Lcom/badlogic/gdx/utils/JsonValue;)(
					"type", @com.badlogic.gdx.utils.JsonValue::new(Ljava/lang/String;)(adType)
				);
				jsonValue.@com.badlogic.gdx.utils.JsonValue::addChild(Ljava/lang/String;Lcom/badlogic/gdx/utils/JsonValue;)(
					"reason", @com.badlogic.gdx.utils.JsonValue::new(Ljava/lang/String;)(reason)
				);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adFinished(Lcom/badlogic/gdx/utils/JsonValue;)(jsonValue);
			})
		});
	}-*/;

	public static native void refreshConfig () /*-{
		SpilSDK.refreshConfig();
	}-*/;

	public static native String getConfigAll () /*-{
		// is there a cleaner way to do this then js object -> string -> JsonValue? Probably not
		return JSON.stringify(SpilSDK.getConfigAll());
	}-*/;

	// note: we are calling java stuff inside functions now, but it should probably by direct when we know it works
	public static native void setConfigDataCallbacks (SpilConfigDataListener listener) /*-{
		if (listener === null) {
			SpilSDK.setConfigDataCallbacks(null);
			return;
		}
		SpilSDK.setConfigDataCallbacks({
			// setting the listener function directly does not seem to work
			configDataUpdated: $entry(function(){
				listener.@com.spilgames.spilgdxsdk.SpilConfigDataListener::configDataUpdated()();
			})
		});
	}-*/;

	public static native void requestPackages () /*-{
		SpilSDK.requestPackages();
	}-*/;

	public static native String getAllPackages() /*-{
		return JSON.stringify(SpilSDK.getAllPackages());
	}-*/;

	public static native String getPackage(String packageId) /*-{
		return JSON.stringify(SpilSDK.getPackage(packageId));
	}-*/;

	public static native String getPromotion(String packageId) /*-{
		return JSON.stringify(SpilSDK.getPromotion(packageId));
	}-*/;

	public static native void requestRewardVideo () /*-{
		SpilSDK.RequestRewardVideo();
	}-*/;

	public static native void playVideo () /*-{
		SpilSDK.PlayVideo();
	}-*/;

	public static native String getUuid() /*-{
		return SpilSDK.getUuid();
	}-*/;

	public static native String getUserProfile() /*-{
		return JSON.stringify(SpilSDK.getUserProfile());
	}-*/;

	public static native String getGameData() /*-{
		return JSON.stringify(SpilSDK.getGameData());
	}-*/;

	public static native void consumeBundle(int bundleId, String reason) /*-{
		SpilSDK.consumeBundle(bundleId, reason);
	}-*/;

	public static native String getWallet () /*-{
		// is there a cleaner way to do this then js object -> string -> JsonValue? Probably not
		return JSON.stringify(SpilSDK.getWallet());
	}-*/;

	public static native void addCurrencyToWallet (int currencyId, int amount, String reason) /*-{
		SpilSDK.addCurrencyToWallet(currencyId, amount, reason);
	}-*/;

	public static native void subtractCurrencyFromWallet (int currencyId, int amount, String reason) /*-{
		SpilSDK.subtractCurrencyFromWallet(currencyId, amount, reason);
	}-*/;

	public static native String getInventory () /*-{
		// is there a cleaner way to do this then js object -> string -> JsonValue? Probably not
		return JSON.stringify(SpilSDK.getInventory());
	}-*/;

	public static native void addItemToInventory (int itemId, int amount, String reason) /*-{
		SpilSDK.addItemToInventory(itemId, amount, reason);
	}-*/;

	public static native void subtractItemFromInventory (int itemId, int amount, String reason) /*-{
		SpilSDK.subtractItemFromInventory(itemId, amount, reason);
	}-*/;

}
