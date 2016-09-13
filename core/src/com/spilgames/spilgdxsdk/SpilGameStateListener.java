package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilGameStateListener {
	void gameStateUpdated(String access);
	void otherUsersGameStateLoaded(String provider, JsonValue data);
	void gameStateError(SpilErrorCode errorCode);
}
