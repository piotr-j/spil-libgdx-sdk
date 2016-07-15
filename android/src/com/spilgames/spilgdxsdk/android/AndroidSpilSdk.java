package com.spilgames.spilgdxsdk.android;

import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilgdxsdk.SpilSdkType;

public class AndroidSpilSdk implements SpilSdk {

	public AndroidSpilSdk () {

	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.ANDROID;
	}
}
