package com.spilgames.spilgdxsdk;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilGameDataListener {
	void gameDataAvailable();
	void gameDataError(SpilErrorCode error);
}
