package com.spilgames.spilgdxsdk;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilPlayerDataListener {
	void playerDataAvailable();

	void playerDataUpdated(String reason);

	void playerDataError(SpilErrorCode errorCode);
}
