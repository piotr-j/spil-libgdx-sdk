package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * Handles instantiation of appropriate backend based on the current platform
 */
public final class SpilSdkManager {
	private final static String TAG = SpilSdkManager.class.getSimpleName();
	private static SpilSdk spilSdk;
	private static boolean loaded;

	/**
	 * Attempts to install appropriate backend based on the current platform
	 * @return installed backed, or null
	 */
	public static SpilSdk install() {
		if (loaded) return spilSdk;
		loaded = true;
		switch (Gdx.app.getType()) {
		case Android:
			return installAndroid();
		case Desktop:
			return installDesktop();
		case iOS:
			return installIOS();
		case WebGL:
			return installWebGL();
		case Applet:
			Gdx.app.error(TAG, "Applet app backendType is not supported!");
			break;
		case HeadlessDesktop:
			Gdx.app.error(TAG, "Headless desktop app backendType is not supported!");
			break;
		}
		return null;
	}

	/**
	 * @return SpilSdk instance, or null if not installed
	 */
	public static SpilSdk instance() {
		// TODO do we want to install if not loaded in here?
		if (!loaded) {
			Gdx.app.error(TAG, "SpilSdk was no loaded, did you forgot to call SpilSdkManager#install()?");
		}
		return spilSdk;
	}

	public static void uninstall() {
		if (!loaded) return;
		loaded = false;
		// TODO do any cleanup needed
		spilSdk = null;
	}

	private static SpilSdk installDesktop () {
		try {
			Class<?> gdxExtClazz =
				ClassReflection.forName("com.spilgames.spilgdxsdk.desktop.DesktopSpilSdk");
			spilSdk = (SpilSdk)ClassReflection.newInstance(gdxExtClazz);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Desktop backend failed to install!", ex);
		}
		return spilSdk;
	}

	private static SpilSdk installAndroid () {
		try {
			Class<?> gdxExtClazz =
				ClassReflection.forName("com.spilgames.spilgdxsdk.android.AndroidSpilSdk");
			spilSdk = (SpilSdk)ClassReflection.newInstance(gdxExtClazz);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Android backend failed to install!", ex);
		}
		return spilSdk;
	}

	private static SpilSdk installIOS () {
		try {
			Class<?> gdxExtClazz =
				ClassReflection.forName("com.spilgames.spilgdxsdk.ios.robovm.IosRoboVMSpilSdk");
			spilSdk = (SpilSdk)ClassReflection.newInstance(gdxExtClazz);
		} catch (Exception ex) {
			try {
				Class<?> gdxExtClazz =
					ClassReflection.forName("com.spilgames.spilgdxsdk.ios.moe.IosMoeSpilSdk");
				spilSdk = (SpilSdk)ClassReflection.newInstance(gdxExtClazz);
			} catch (Exception ex2) {
				Gdx.app.error(TAG, "iOS RoboVM backend failed to install!", ex);
				Gdx.app.error(TAG, "iOS Moe backend failed to install!", ex2);
			}
		}
		return spilSdk;
	}

	private static SpilSdk installWebGL () {
		try {
			Class<?> gdxExtClazz =
				ClassReflection.forName("com.spilgames.spilgdxsdk.html.HtmlSpilSdk");
			spilSdk = (SpilSdk)ClassReflection.newInstance(gdxExtClazz);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "HTML backend failed to install!", ex);
		}
		return spilSdk;
	}
}
