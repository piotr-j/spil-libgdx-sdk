package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 28/07/16.
 */
public interface SpilNotificationDataListener {
	void onRewardReceived(JsonValue reward);
}
