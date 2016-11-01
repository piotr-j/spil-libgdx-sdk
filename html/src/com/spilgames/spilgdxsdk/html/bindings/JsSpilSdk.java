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

	// note: we are calling java stuff inside functions now, but it should probably by direct when we know it works
	public static native void setConfigDataCallbacks (SpilConfigDataListener listener) /*-{
		SpilSDK.setConfigDataCallbacks({
			configDataUpdated: function(){
				listener.@com.spilgames.spilgdxsdk.SpilConfigDataListener::configDataUpdated()();
			}
			// should probably be this
			// configDataUpdated: listener.@com.spilgames.spilgdxsdk.SpilConfigDataListener::configDataUpdated()
		});
	}-*/;

	public static native void setPlayerDataCallbacks (SpilPlayerDataListener listener) /*-{
		SpilSDK.setPlayerDataCallbacks({
			playerDataError:function(error){
				// what is this error? gotta translate it into SpilErrorCode somehow
				console.log(error);
				// when we know whats in the error, translate it into SpilErrorCode
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataError(Lcom/spilgames/spilgdxsdk/SpilErrorCode;)(null);
			},
			playerDataUpdated:function(reason, updatedData){
				console.log('playerDataUpdated triggered ' + reason + ' ' + updatedData);
				// what are the args? gotta translate updatedData into JsonValue somehow
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataUpdated(Ljava/lang/String;Lcom/badlogic/gdx/utils/JsonValue;)(reason, null);
			},
			playerDataAvailable: function() {
				console.log('playerDataAvailable triggered');
				listener.@com.spilgames.spilgdxsdk.SpilPlayerDataListener::playerDataAvailable()();
			}
		});
	}-*/;

	public static native void setGameDataCallbacks (SpilGameDataListener listener) /*-{
		SpilSDK.setGameDataCallbacks({
			gameDataError: function (error) {
				console.log(error)
				// gotta translate the error at some point!
				listener.@com.spilgames.spilgdxsdk.SpilGameDataListener::gameDataError(Lcom/spilgames/spilgdxsdk/SpilErrorCode;)(null);
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

	public static native void requestPackages () /*-{
		SpilSDK.requestPackages();
	}-*/;

	public static native void requestRewardVideo () /*-{
		SpilSDK.RequestRewardVideo();
	}-*/;

	public static native void playVideo () /*-{
		SpilSDK.PlayVideo();
	}-*/;

}
