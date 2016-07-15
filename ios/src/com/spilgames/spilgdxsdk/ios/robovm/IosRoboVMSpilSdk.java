package com.spilgames.spilgdxsdk.ios.robovm;

import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilgdxsdk.SpilSdkType;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class IosRoboVMSpilSdk implements SpilSdk {
	public IosRoboVMSpilSdk (){
	}

	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_ROBOVM;
	}
}
