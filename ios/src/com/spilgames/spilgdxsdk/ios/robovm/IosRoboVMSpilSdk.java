package com.spilgames.spilgdxsdk.ios.robovm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.spilgames.spilgdxsdk.*;
import com.spilgames.spilgdxsdk.ios.robovm.bindings.Spil;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSMutableDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.objc.block.VoidBlock1;


/**
 * Created by PiotrJ on 01/07/16.
 */
public class IosRoboVMSpilSdk implements SpilSdk {
	private final static String TAG = IosRoboVMSpilSdk.class.getSimpleName();

	public IosRoboVMSpilSdk (){

	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_ROBOVM;
	}

	@Override public void setDebug (boolean debug) {
		Spil.debug(debug);
	}

	@Override public void trackEvent (SpilEvent event) {
		Spil.trackEvent(event.getName(), buildParams(event));
	}

	private final static NSString KEY_NAME = new NSString("name");
	@Override public void trackEvent (SpilEvent event, final SpilEventActionListener listener) {
		Spil.trackEvent(event.getName(), buildParams(event), new VoidBlock1<NSObject>() {
			@Override public void invoke (NSObject nsObject) {
				SpilResponseEvent responseEvent = new SpilResponseEvent();
				responseEvent.setName("responseEvent");
				if (nsObject instanceof NSDictionary) {
					NSDictionary<NSString, NSString> responseDict = (NSDictionary<NSString, NSString>)nsObject;
					if (responseDict.containsKey(KEY_NAME)) {
						responseEvent.setName(responseDict.getString(KEY_NAME));
					}
					// TODO is this data or custom data? lets go with data for now
					for (NSString key : responseDict.keySet()) {
						responseEvent.addData(key.toString(), responseDict.getString(key));
					}
				} else {
					Gdx.app.error(TAG, "Unexpected response object type " + nsObject.getClass());
				}
				listener.onResponse(responseEvent);
			}
		});
	}

	private NSDictionary<NSString, NSString> buildParams (SpilEvent event) {
		ObjectMap<String, String> data = event.getData();
		ObjectMap<String, String> customData = event.getCustomData();
		if (data == null && customData == null) return null;
		NSMutableDictionary<NSString, NSString> params = new NSMutableDictionary<>();
		if (data != null) {
			for (ObjectMap.Entry<String, String> entry : data.entries()) {
				params.put(entry.key, entry.value);
			}
		}

		if (customData != null) {
			for (ObjectMap.Entry<String, String> entry : customData.entries()) {
				params.put(entry.key, entry.value);
			}
		}
		return params;
	}

	@Override public void onCreate () {
		Spil.start();
	}

	@Override public void onStart () {

	}

	@Override public void onResume () {
		Spil.applicationDidBecomeActive(UIApplication.getSharedApplication());
	}

	@Override public void onPause () {
		Spil.applicationDidEnterBackground(UIApplication.getSharedApplication());
	}

	@Override public void onDestroy () {

	}

	@Override public void onBackPressed () {
		// do nothing on iOS
	}

	@Override public ObjectMap<String, String> getConfigAll () {
		NSDictionary<NSString, NSString> config = Spil.getConfig();
		if (config == null) return null;
		ObjectMap<String, String> configMap = new ObjectMap<>();
		for (NSString key : config.keySet()) {
			configMap.put(key.toString(), config.getString(key));
		}
		return configMap;
	}

	@Override public String getConfigValue (String key) {
		NSString value = Spil.getConfigValue(key);
		if (value == null) return null;
		String stringValue = value.toString();
		// it returns empty string on missing key
		// android returns null when key is missing, so we will as well
		if (stringValue.trim().length() == 0) return null;
		return stringValue;
	}

	@Override public void startChartboost (String appId, String appSignature) {

	}
}
