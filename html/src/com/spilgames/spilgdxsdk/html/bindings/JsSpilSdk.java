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

	public static native void init (String gameId, String gameVersion, String env, Callback<Void, Void> callback) /*-{
		SpilSDK(gameId, gameVersion, function(){
			// looks like it accepts only Object for generic type
			callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)();
		}, env);
	}-*/;

	public static native void sendEvent (String eventName, String data, SpilEventActionListener listener) /*-{
		// this is super janky, we convert from JsonValue to string to object and to string again in the api
		var callback = null;
		if (listener) {
			callback = function(responseData) {
			   console.log("JsSpilSdk response for " + eventName + ": " + JSON.stringify(responseData));
			   // looks like we are getting something like this back at this point
			   // {"status":"204","name":"eventName"}
				var response = @com.spilgames.spilgdxsdk.SpilResponseEvent::new()()
				response.@com.spilgames.spilgdxsdk.SpilResponseEvent::setName(Ljava/lang/String;)(responseData["name"]);
				listener.@com.spilgames.spilgdxsdk.SpilEventActionListener::onResponse(Lcom/spilgames/spilgdxsdk/SpilResponseEvent;)(response);
			}
		}
		if (data) {
			SpilSDK.sendEvent(eventName, JSON.parse(data), callback);
		} else {
			SpilSDK.sendEvent(eventName, null, callback);
		}
	}-*/;

	public static void sendCustomEvent(String eventName, String data, SpilEventActionListener listener) {
		// NOTE sendCustomEvent in js just delegates to sendEvent, so we will do the same
		sendEvent(eventName, data, listener);
	}

	public static native void requestPlayerData () /*-{
		SpilSDK.requestPlayerData();
	}-*/;

	public static native void setPlayerDataCallbacks (SpilPlayerDataListener listener) /*-{
		SpilSDK.setPlayerDataCallbacks({
			playerDataError:function(error){
				console.log(error);
				var spilError = @com.spilgames.spilgdxsdk.SpilErrorCode::fromId(I)(error["id"]);
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataError(Lcom/spilgames/spilgdxsdk/SpilErrorCode;)(spilError);
			},
			playerDataUpdated:function(reason, updatedData){
				console.log('playerDataUpdated triggered ' + reason + ' ' + updatedData);
				// reason String, updateData js object, we can make it into a string with JSON and do out toJsonValue stuff?
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataUpdated(Ljava/lang/String;Lcom/badlogic/gdx/utils/JsonValue;)(reason, null);
			},
			playerDataAvailable: function() {
				console.log('playerDataAvailable triggered');
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataAvailable()();
			}
		});
	}-*/;

	public static native void requestGameData () /*-{
		SpilSDK.requestGameData();
	}-*/;

	public static native void setGameDataCallbacks (SpilGameDataListener listener) /*-{
		SpilSDK.setGameDataCallbacks({
			gameDataError: function (error) {
				console.log(error)
				var spilError = @com.spilgames.spilgdxsdk.SpilErrorCode::fromId(I)(error["id"]);
				listener.@com.spilgames.spilgdxsdk.SpilGameDataListener::gameDataError(Lcom/spilgames/spilgdxsdk/SpilErrorCode;)(spilError);
			},
			gameDataAvailable: function() {
				console.log('gameDataAvailable triggered');
				listener.@com.spilgames.spilgdxsdk.SpilGameDataListener::gameDataAvailable()();
			}
		});
	}-*/;

	public static native void setAdCallbacks (SpilAdListener listener) /*-{
		SpilSDK.setAdCallbacks({
			AdAvailable: function(adType){
				console.log('AdAvailable triggered ' + adType);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adAvailable(Ljava/lang/String;)(adType);
			},
			AdNotAvailable: function(adType){
				console.log('AdNotAvailable triggered ' + adType);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adNotAvailable(Ljava/lang/String;)(adType);
			},
			AdStart: function(adType){
				console.log('AdStart triggered ' + adType);
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adStart()();
			},
			AdFinished: function(network, adType, reason){
				console.log('AdFinished triggered ' + network + ' ' + adType + ' ' + reason);
				// gotta stuff params into JsonValue
				listener.@com.spilgames.spilgdxsdk.SpilAdListener::adFinished(Lcom/badlogic/gdx/utils/JsonValue;)(null);
			}
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
		SpilSDK.setConfigDataCallbacks({
			// setting the listener function directly does not seem to work
			configDataUpdated: function(){
				listener.@com.spilgames.spilgdxsdk.SpilConfigDataListener::configDataUpdated()();
			}
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

	public static native void consumeBundle(int bundleId, String reason, boolean fromShop) /*-{
		SpilSDK.consumeBundle(bundleId, reason, fromShop);
	}-*/;

	public static native String getWallet () /*-{
		// is there a cleaner way to do this then js object -> string -> JsonValue? Probably not
		return JSON.stringify(SpilSDK.getWallet());
	}-*/;

	public static native void addCurrencyToWallet (int currencyId, int amount, String reason) /*-{
		SpilSDK.addCurrencyToWallet(currencyId, amount, reason);
	}-*/;

	public static native void subtractCurrencyFromWallet (int currencyId, int amount, String reason) /*-{
		SpilSDK.addCurrencyToWallet(currencyId, amount, reason);
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
