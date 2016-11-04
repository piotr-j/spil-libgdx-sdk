package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilDailyBonusListener;
import com.spilgames.spilgdxsdk.SpilErrorCode;
import com.spilgames.spilgdxsdk.SpilSplashScreenListener;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class WebScreen extends BackScreen {
	private final static String TAG = WebScreen.class.getSimpleName();

	public WebScreen (final SpilGame game) {
		super(game);
		spilSdk.setSpilDailyBonusListener(new SpilDailyBonusListener() {
			@Override public void dailyBonusOpen () {
				Gdx.app.log(TAG, "dailyBonusOpen ()");
			}

			@Override public void dailyBonusClosed () {
				Gdx.app.log(TAG, "dailyBonusClosed ()");
			}

			@Override public void dailyBonusNotAvailable () {
				Gdx.app.log(TAG, "dailyBonusNotAvailable ()");
				dialog("dailyBonusNotAvailable");
			}

			@Override public void dailyBonusError (SpilErrorCode errorCode) {
				Gdx.app.log(TAG, "dailyBonusError ("+errorCode+")");
				dialog("dailyBonusError", errorCode.getMessage());
			}

			@Override public void dailyBonusReward (JsonValue rewardList) {
				Gdx.app.log(TAG, "dailyBonusReward ("+rewardList+")");
				dialog("dailyBonusReward", rewardList.toString());
			}
		});
		spilSdk.setSpilSplashScreenListener(new SpilSplashScreenListener() {
			@Override public void splashScreenOpenShop () {
				Gdx.app.log(TAG, "splashScreenOpenShop ()");
			}

			@Override public void splashScreenOpen () {
				Gdx.app.log(TAG, "splashScreenOpen ()");
			}

			@Override public void splashScreenClosed () {
				Gdx.app.log(TAG, "splashScreenClosed ()");
			}

			@Override public void splashScreenNotAvailable () {
				Gdx.app.log(TAG, "splashScreenNotAvailable ()");
				dialog("splashScreenNotAvailable");
			}

			@Override public void splashScreenError (SpilErrorCode errorCode) {
				Gdx.app.log(TAG, "splashScreenError ("+errorCode+")");
				dialog("splashScreenError", errorCode.getMessage());
			}
		});
		content.defaults().pad(5);
		content.add(new VisLabel("Daily Bonus")).row();;
		{
			VisTextButton button = new VisTextButton("Request Daily Bonus");
			content.add(button).row();
			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					spilSdk.requestDailyBonus();
				}
			});
		}
		content.add(new VisLabel("Splash Screen")).row();;
		{
			VisTextButton button = new VisTextButton("Request Splash Screen");
			content.add(button).row();
			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					spilSdk.requestSplashScreen();
				}
			});
		}

		// move content to the top
		content.add().expand().fill();
	}
}
