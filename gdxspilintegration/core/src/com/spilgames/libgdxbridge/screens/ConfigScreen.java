package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class ConfigScreen extends BackScreen {
	private final static String TAG = ConfigScreen.class.getSimpleName();

	JsonValue config;
	public ConfigScreen (final SpilGame game) {
		super(game);
		final VisTextButton getConfig = new VisTextButton("Get all config data");
		content.defaults().pad(5);
		content.add(getConfig).row();
		final VisTable valueTable = new VisTable(true);
		final VisTextField configValueKey = new VisTextField();
		configValueKey.setMessageText("key");
		VisTextButton getConfigValue = new VisTextButton("Get config");
		valueTable.add(configValueKey).expandX().fillX();
		valueTable.add(getConfigValue);
		content.add(valueTable).expandX().fillX().row();
		final VisLabel resultLabel = new VisLabel();
		VisScrollPane scrollPane = new VisScrollPane(resultLabel);
		content.add(scrollPane).expand();

		updateConfig(resultLabel);

		getConfig.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				updateConfig(resultLabel);
			}
		});
		getConfigValue.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				if (config != null) {
					JsonValue value = config.get(configValueKey.getText());
					if (value != null) {
						String valueJson = value.prettyPrint(JsonWriter.OutputType.minimal, 0);
						// labels dont do tabs
						valueJson = valueJson.replace("\t", "  ");
						resultLabel.setText(valueJson);
					} else {
						resultLabel.setText("<not found>");
					}
				} else {
					resultLabel.setText("<not found>");
				}
			}
		});
	}

	private void updateConfig (VisLabel resultLabel) {
		config = spilSdk.getConfig();
		if (config != null) {
			try {
				String valueJson = config.prettyPrint(JsonWriter.OutputType.minimal, 0);
				// labels dont do tabs
				valueJson = valueJson.replace("\t", "  ");
				resultLabel.setText(valueJson);
			} catch (Exception ex) {
				Gdx.app.error(TAG, "Failed to parse config data! ", ex);
			}
		}
	}
}
