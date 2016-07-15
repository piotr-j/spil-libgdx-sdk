package com.spilgames.spilgdxsdk.html;

import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.spilgdxsdk.SpilSdkType;

/**
 * Created by PiotrJ on 01/07/16.
 */
public class HtmlSpilSdk implements SpilSdk {
	@Override public SpilSdkType getBackendType () {
		return SpilSdkType.HTML;
	}
}
