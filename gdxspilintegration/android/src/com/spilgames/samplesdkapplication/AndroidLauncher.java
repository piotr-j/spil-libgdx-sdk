package com.spilgames.samplesdkapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.ads.AdSettings;
import com.spilgames.libgdxbridge.BuildConfig;
import com.spilgames.libgdxbridge.PlatformBridge;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.android.AndroidSpilSdk;
import com.spilgames.spilsdk.SpilEnvironment;
import com.spilgames.spilsdk.ads.NativeAdCallbacks;
import com.spilgames.spilsdk.gamedata.OnGameDataListener;
import com.spilgames.spilsdk.gamedata.SpilGameDataCallbacks;
import com.spilgames.spilsdk.utils.error.ErrorCodes;

public class AndroidLauncher extends AndroidApplication {
	private final static String TAG = AndroidLauncher.class.getSimpleName();

//	public final static String SPIL_DEVICE_ID = "988987865109";
	public final static String SPIL_DEVICE_ID = "29590593309";
	//    public final static String SPIL_DEVICE_ID = "29590593309";
//	public final static String DFP_AD_UNIT_ID = "/59392726/test_native_interstitial";
	public final static String DFP_AD_UNIT_ID = "/59392726/NativeMobile/Test-Interstitials";
	public final static String FYBER_APP_ID = "37301";
	public final static String FYBER_TOKEN = "038a9377532f222c33e88bd76e82ecbc";
	//    public final static String CHARBOOST_APP_ID = "56f3bf0c2fdf3406a7450e5a";
	public final static String CHARBOOST_APP_ID = "5776767a04b01608ac8cf156";
	//    public final static String CHARBOOST_APP_SIG = "b655f901fb6b1258dc170a0da94ae1b70c320a3f";
	public final static String CHARBOOST_APP_SIG = "bcbcf4357637263e7d10b147b3e58d139701b9cd";

	AndroidSpilSdk spilSdk;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

			} else {
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
			}
		}

		// adjust, it doesnt like being initialized in callback
		String appToken = "ozq14osmze2o";
		String environment = AdjustConfig.ENVIRONMENT_SANDBOX;
		AdjustConfig adjustConfig = new AdjustConfig(AndroidLauncher.this, appToken, environment);
		Adjust.onCreate(adjustConfig);

		AdSettings.addTestDevice("22134B4472FE5181D0ECDD2D22048DBB");
		AdSettings.addTestDevice("710fdf61be878656071f89a144712143");

		spilSdk = new AndroidSpilSdk(this);

		// dunno what this is used for, but its in android sample project
		spilSdk.setPluginInformation("Native", BuildConfig.VERSION_NAME);

		spilSdk.onCreate();
		spilSdk.registerDevice(SPIL_DEVICE_ID);

//		if(true){
//			instance.startDFP(DFP_AD_UNIT_ID, null);
//			Toast.makeText(this,"DFP Initialised", Toast.LENGTH_SHORT).show();
//
//			instance.startFyber(FYBER_APP_ID, FYBER_TOKEN, null);
//			Toast.makeText(this, "Fyber Initialised", Toast.LENGTH_SHORT).show();

//			instance.setupChartBoost(CHARBOOST_APP_ID, CHARBOOST_APP_SIG);
//			Toast.makeText(this,"ChartBoost Initialised", Toast.LENGTH_SHORT).show();
//		}

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SpilGame(spilSdk, new PlatformBridge() {
			@Override public void onCreate () {
			}
		}), config);
	}

	@Override public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
		@NonNull int[] grantResults) {
		spilSdk.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override protected void onResume () {
		super.onResume();
		Adjust.onResume();
		spilSdk.onResume();
	}

	@Override protected void onPause () {
		spilSdk.onPause();
		Adjust.onPause();
		super.onPause();
	}

	@Override public void onStart() {
		super.onStart();
		spilSdk.onStart();
	}

	@Override public void onDestroy() {
		super.onDestroy();
		spilSdk.onDestroy();
	}

	@Override public void onBackPressed () {
		spilSdk.onBackPressed();
	}
}
