package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilAdCallbacks {
	void adAvailable (String type);

	void adNotAvailable (String type);

	void adStart();

	void adFinished (JsonValue response);
}
