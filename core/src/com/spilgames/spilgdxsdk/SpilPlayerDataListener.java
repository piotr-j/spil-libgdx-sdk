package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilPlayerDataListener {
	void playerDataAvailable();

	void playerDataUpdated(String reason, JsonValue updatedData);

	void playerDataError(SpilErrorCode errorCode);
}
