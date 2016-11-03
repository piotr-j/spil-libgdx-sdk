package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class MainScreen extends BaseScreen {
	private final static String TAG = MainScreen.class.getSimpleName();

	public MainScreen (final SpilGame game) {
		super(game);

		VisTable table = new VisTable(true);
		table.add(new VisLabel("General")).row();
		addScreenButton(table, "Config", ConfigScreen.class);
		addScreenButton(table, "IAPEvents", IAPEventsScreen.class);
		addScreenButton(table, "CustomEvents", CustomEventsScreen.class);
		addScreenButton(table, "Tracking shortcuts", TrackingScreen.class);

		table.add(new VisLabel("Packages & Promotions")).row();
		addScreenButton(table, "Packages & Promotions", PackagesScreen.class);

		table.add(new VisLabel("Ad Networks")).row();
		addScreenButton(table, "Ads", AdScreen.class);

		table.add(new VisLabel("Game & Player Data")).row();
		addScreenButton(table, "Player Data", PlayerDataScreen.class);
		addScreenButton(table, "Game State", GameStateScreen.class);
		addScreenButton(table, "Shop", ShopScreen.class);

		table.add(new VisLabel("Customer Support")).row();
		addScreenButton(table, "Zendesk", ZendeskScreen.class);

		root.add(table).expand().top();
	}

	private void addScreenButton (VisTable table, String label, final Class<? extends BaseScreen> aClass) {
		VisTextButton button = new VisTextButton(label);
		table.add(button).row();
		button.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				changeScreen(aClass);
			}
		});
	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
			Gdx.app.exit();
			return true;
		}
		return false;
	}
}
