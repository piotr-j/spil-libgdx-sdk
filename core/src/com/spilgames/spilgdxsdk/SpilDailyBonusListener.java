package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 19/07/16.
 */
public interface SpilDailyBonusListener {
	void dailyBonusOpen ();

	void dailyBonusClosed ();

	void dailyBonusNotAvailable ();

	void dailyBonusError (SpilErrorCode errorCode);

	void dailyBonusReward (JsonValue rewardList);

}
