package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class PackagesScreen extends BackScreen {
	private final static String TAG = PackagesScreen.class.getSimpleName();

	VisLabel resultLabel;
	public PackagesScreen (final SpilGame game) {
		super(game);
		final VisTextButton getAll = new VisTextButton("Get all packages");
		content.defaults().pad(5);
		content.add(getAll).row();
		{
			final VisTable valueTable = new VisTable(true);
			final VisTextField packageKey = new VisTextField();
			packageKey.setMessageText("package id");
			VisTextButton getPackageData = new VisTextButton("Get package");
			valueTable.add(packageKey).expandX().fillX();
			valueTable.add(getPackageData);
			content.add(valueTable).expandX().fillX().row();

			getPackageData.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					JsonValue value = spilSdk.getPackage(packageKey.getText());
					if (value != null) {
						resultLabel.setText(value.prettyPrint(JsonWriter.OutputType.minimal, 0).replace("\t", "  "));
					} else {
						resultLabel.setText("<missing>");
					}
				}
			});
		}

		{
			final VisTable valueTable = new VisTable(true);
			final VisTextField packageKey = new VisTextField();
			packageKey.setMessageText("package id");
			VisTextButton getPackageData = new VisTextButton("Get promotion");
			valueTable.add(packageKey).expandX().fillX();
			valueTable.add(getPackageData);
			content.add(valueTable).expandX().fillX().row();

			getPackageData.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					JsonValue value = spilSdk.getPromotion(packageKey.getText());
					if (value != null) {
						resultLabel.setText(value.prettyPrint(JsonWriter.OutputType.minimal, 0).replace("\t", "  "));
					} else {
						resultLabel.setText("<missing>");
					}
				}
			});
		}

		resultLabel = new VisLabel();
		VisScrollPane scrollPane = new VisScrollPane(resultLabel);
		content.add(scrollPane).expand().fill();

		getAll.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				JsonValue value = spilSdk.getAllPackages();
				if (value != null) {
					resultLabel.setText(value.prettyPrint(JsonWriter.OutputType.minimal, 0).replace("\t", "  "));
				} else {
					resultLabel.setText("<missing>");
					spilSdk.requestPackages();
				}
			}
		});
	}
}
