package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.spilgames.spilgdxsdk.SpilAdListener;
import com.spilgames.spilgdxsdk.SpilSdk;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class AdScreen extends BackScreen {
	private final static String TAG = AdScreen.class.getSimpleName();

	public AdScreen (final SpilGame game) {
		super(game);
		final Preferences prefs = Gdx.app.getPreferences(SpilGame.class.getSimpleName());
		content.defaults().pad(5);
		// DFP
		{
			content.add(new VisLabel("DFP")).pad(20).row();
			VisTextButton dfpInterstitial = new VisTextButton("Interstitial");
			content.add(dfpInterstitial).row();
			dfpInterstitial.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (spilSdk.isAdProviderInitialized(SpilSdk.PROVIDER_DFP)) {
						spilSdk.devRequestAd(SpilSdk.PROVIDER_DFP, SpilSdk.AD_INTERSTITIAL, false);
					} else {
						dialog("Provider " + SpilSdk.PROVIDER_DFP + " not initialized");
					}
				}
			});
			VisTextButton dfpRewardVideo = new VisTextButton("Reward Video");
			content.add(dfpRewardVideo).row();
			dfpRewardVideo.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (spilSdk.isAdProviderInitialized(SpilSdk.PROVIDER_DFP)) {
						spilSdk.devRequestAd(SpilSdk.PROVIDER_DFP, SpilSdk.AD_REWARD_VIDEO, false);
					} else {
						dialog("Provider " + SpilSdk.PROVIDER_DFP + " not initialized");
					}
				}
			});
		}
		// FYBER
		{
			content.add(new VisLabel("FYBER")).pad(20).row();
			VisTextButton fybRewardVideo = new VisTextButton("Reward Video");
			content.add(fybRewardVideo).row();
			fybRewardVideo.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (spilSdk.isAdProviderInitialized(SpilSdk.PROVIDER_FYBER)) {
						spilSdk.devRequestAd(SpilSdk.PROVIDER_FYBER, SpilSdk.AD_REWARD_VIDEO, false);
					} else {
						dialog("Provider " + SpilSdk.PROVIDER_FYBER + " not initialized");
					}
				}
			});
		}

		// CHARBOOST
		{
			content.add(new VisLabel("CHARTBOOST")).pad(20).row();
			VisTextButton cbInterstitial = new VisTextButton("Interstitial");
			content.add(cbInterstitial).row();
			VisTextButton cbRewardVideo = new VisTextButton("Reward Video");
			content.add(cbRewardVideo).row();
			VisTextButton cbMoreApps = new VisTextButton("More Apps");
			content.add(cbMoreApps).row();
			cbInterstitial.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (spilSdk.isAdProviderInitialized(SpilSdk.PROVIDER_CHARTBOOST)) {
						spilSdk.devRequestAd(SpilSdk.PROVIDER_CHARTBOOST, SpilSdk.AD_INTERSTITIAL, false);
					} else {
						dialog("Provider " + SpilSdk.PROVIDER_CHARTBOOST + " not initialized");
					}
				}
			});
			cbRewardVideo.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (spilSdk.isAdProviderInitialized(SpilSdk.PROVIDER_CHARTBOOST)) {
						spilSdk.devRequestAd(SpilSdk.PROVIDER_CHARTBOOST, SpilSdk.AD_REWARD_VIDEO, false);
					} else {
						dialog("Provider " + SpilSdk.PROVIDER_CHARTBOOST + " not initialized");
					}
				}
			});
			cbMoreApps.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (spilSdk.isAdProviderInitialized(SpilSdk.PROVIDER_CHARTBOOST)) {
						spilSdk.devRequestAd(SpilSdk.PROVIDER_CHARTBOOST, SpilSdk.AD_MORE_APPS, false);
					} else {
						dialog("Provider " + SpilSdk.PROVIDER_CHARTBOOST + " not initialized");
					}
				}
			});
		}

		game.spilSdk.setSpilAdListener(new SpilAdListener() {
			@Override public void adAvailable (final String type) {
				Gdx.app.log(TAG, "adAvailable " +type);
				if (SpilSdk.AD_REWARD_VIDEO.equals(type)) {
					spilSdk.showRewardVideo();
				} else if (SpilSdk.AD_MORE_APPS.equals(type)) {
					spilSdk.showMoreApps();
				} else {
					Gdx.app.log(TAG, "Unknown ad type " + type);
				}
			}

			@Override public void adNotAvailable (String type) {
				Gdx.app.log(TAG, "adNotAvailable " +type);
				dialog("Ad " + type + " not available");
			}

			@Override public void adStart () {
				Gdx.app.log(TAG, "adStart");
			}

			@Override public void adFinished (JsonValue response) {
				Gdx.app.log(TAG, "adFinished " + response.prettyPrint(JsonWriter.OutputType.minimal, 0));
			}
		});
		content.add().fill().expand();
	}
}
