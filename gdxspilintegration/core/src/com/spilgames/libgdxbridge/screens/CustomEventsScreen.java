package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.spilgdxsdk.SpilEvent;
import com.spilgames.spilgdxsdk.SpilEventActionListener;
import com.spilgames.spilgdxsdk.SpilResponseEvent;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class CustomEventsScreen extends BackScreen {
	private final static String TAG = CustomEventsScreen.class.getSimpleName();

	public CustomEventsScreen (final SpilGame game) {
		super(game);
		final SpilEvent spilEvent = new SpilEvent();
		content.defaults().pad(5);
		final VisTextField nameTF = new VisTextField();
		nameTF.setMessageText("event name");
		final VisTextField keyTF = new VisTextField();
		keyTF.setMessageText("param key");
		final VisTextField valueTF = new VisTextField();
		valueTF.setMessageText("param value");
		content.add(nameTF).expandX().fillX().row();
		VisTable paramPair = new VisTable(true);
		paramPair.add(keyTF).expandX().fillX();
		paramPair.add(valueTF).expandX().fillX();
		content.add(paramPair).expandX().fillX().row();
		VisTextButton addParameter = new VisTextButton("Add parameter");
		content.add(addParameter).row();
		VisTextButton send = new VisTextButton("Send event");
		content.add(send).row();

		final VisLabel parameters = new VisLabel("KEY : VALUE");
		VisScrollPane scrollPane = new VisScrollPane(parameters);
		content.add(scrollPane).expand().row();

		addParameter.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				String key = keyTF.getText().trim();
				if (key.isEmpty()) {
					Dialogs.showErrorDialog(stage, "Parameter key cannot be empty");
					return;
				}
				String value = valueTF.getText().trim();
				if (value.isEmpty()) {
					Dialogs.showErrorDialog(stage, "Parameter value cannot be empty");
					return;
				}
				spilEvent.addData(key, value);
				parameters.setText(parameters.getText()+"\n"+key+" : " + value);
//				keyTF.setText("");
//				valueTF.setText("");
			}
		});

		send.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				String name = nameTF.getText().trim();
				if (name.isEmpty()) {
					Dialogs.showErrorDialog(stage, "Event name cannot be empty");
					return;
				}
				spilEvent.setName(name);
				spilSdk.trackEvent(spilEvent, new SpilEventActionListener() {
					@Override public void onResponse (SpilResponseEvent convert) {
						String response = convert.toString();
						VisDialog dialog = new VisDialog("Event Response");
						VisLabel label = new VisLabel(response);
						label.setWrap(true);
						dialog.getContentTable().add(label).width(stage.getWidth() * 3 / 4).expand().fill();
						dialog.button(ButtonBar.ButtonType.OK.getText()).padBottom(3);
						dialog.pack();
						dialog.centerWindow();
						stage.addActor(dialog.fadeIn());
					}
				});
			}
		});
	}
}
