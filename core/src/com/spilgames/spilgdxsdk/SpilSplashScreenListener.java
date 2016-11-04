package com.spilgames.spilgdxsdk;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilSplashScreenListener {
	void splashScreenOpenShop ();

	void splashScreenOpen ();

	void splashScreenClosed ();

	void splashScreenNotAvailable ();

	void splashScreenError (SpilErrorCode errorCode);
}
