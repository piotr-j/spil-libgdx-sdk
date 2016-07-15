package com.spilgames.spilgdxsdk.ios.moe;

import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilgdxsdk.SpilSdkType;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class IosMoeSpilSdk implements SpilSdk {
	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.IOS_MOE;
	}
}
