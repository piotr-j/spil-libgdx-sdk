package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilErrorCode;
import com.spilgames.spilgdxsdk.SpilGameStateListener;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class GameStateScreen extends BackScreen {
	private final static String TAG = GameStateScreen.class.getSimpleName();
	Array<String> otherUserIds = new Array<String>();

	public GameStateScreen (final SpilGame game) {
		super(game);

		content.defaults().pad(5);
		content.add(new VisLabel("Game State")).row();
		// provider & user id
		{
			VisTable container = new VisTable(true);
			VisLabel providerLabel = new VisLabel("Provider");
			container.add(providerLabel);
			final VisTextField provider = new VisTextField("");
			container.add(provider).expandX().fillX().row();
			provider.setMessageText("provider");
			VisLabel userIdLabel = new VisLabel("Custom User Id");
			container.add(userIdLabel);
			final VisTextField userId = new VisTextField("");
			container.add(userId).expandX().fillX().row();
			userId.setMessageText("user id");
			VisTextButton save = new VisTextButton("Save provider & user id");
			container.add(save).colspan(2);
			content.add(container).expandX().fillX().row();

			save.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					if (!provider.getText().isEmpty() && !userId.getText().isEmpty()) {
						spilSdk.setUserID(provider.getText(), userId.getText());
					}
				}
			});

			if (spilSdk.getUserID() != null) provider.setText(spilSdk.getUserID());
			if (spilSdk.getUserProvider() != null) userId.setText(spilSdk.getUserProvider());
		}
		// private & public game state
		{
			VisTable container = new VisTable(true);
			{
				VisTable innerContainer = new VisTable(true);
				final VisTextArea textArea = new VisTextArea();
				textArea.setMessageText("Private\nGame\nState");
				textArea.setPrefRows(3.1f);
				VisTextButton save = new VisTextButton("Save Private GS");
				innerContainer.add(textArea).expandX().fillX().row();
				innerContainer.add(save).expandX().fillX();
				container.add(innerContainer).expandX().fillX();

				String state = spilSdk.getPrivateGameState();
				if (state != null) {
					textArea.setText(state);
				}
				save.addListener(new ClickListener() {
					@Override public void clicked (InputEvent event, float x, float y) {
						spilSdk.setPrivateGameState(textArea.getText());
					}
				});
			}
			{
				VisTable innerContainer = new VisTable(true);
				final VisTextArea textArea = new VisTextArea();
				textArea.setMessageText("Public\nGame\nState");
				textArea.setPrefRows(3.1f);
				VisTextButton save = new VisTextButton("Save Public GS");
				innerContainer.add(textArea).expandX().fillX().row();
				innerContainer.add(save).expandX().fillX();
				container.add(innerContainer).expandX().fillX();

				String state = spilSdk.getPublicGameState();
				if (state != null) {
					textArea.setText(state);
				}
				save.addListener(new ClickListener() {
					@Override public void clicked (InputEvent event, float x, float y) {
						spilSdk.setPublicGameState(textArea.getText());
					}
				});
			}
			content.add(container).expandX().fillX().row();
		}
		// other users game state
		{
			VisTable container = new VisTable(true);
			VisTable otherIdContainer = new VisTable(true);
			VisLabel otherUserIdLabel = new VisLabel("Other User Id");
			otherIdContainer.add(otherUserIdLabel);
			final VisTextField otherUserId = new VisTextField("");
			otherUserId.setMessageText("Other User Id");
			otherIdContainer.add(otherUserId).expandX().fillX().row();
			container.add(otherIdContainer).expandX().fillX().row();
			final VisTextButton saveOtherUserId = new VisTextButton("Add Other User Id");
			container.add(saveOtherUserId).expandX().row();

			final VisTextArea textArea = new VisTextArea();
			textArea.setMessageText("Users");
			textArea.setPrefRows(2.1f);
			container.add(textArea).expandX().fillX().row();

			VisTextButton getOtherUserIds = new VisTextButton("Get Other User Game State");
			container.add(getOtherUserIds).expandX().row();

			content.add(container).expandX().fillX().row();

			saveOtherUserId.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					String text = otherUserId.getText();
					if (text.isEmpty()) {
						toasts.show("Other UID cannot be empty!", 2f);
					} else {
						otherUserIds.add(text);
						textArea.clear();
						StringBuilder sb = new StringBuilder();
						for (String userId : otherUserIds) {
							sb.append(userId).append(", ");
						}
						sb.delete(sb.length -2, sb.length -1);
						textArea.setText(sb.toString());

					}
				}
			});

			getOtherUserIds.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					if (otherUserIds.size == 0) {
						toasts.show("Add other user ids first!", 2f);
					} else {
						spilSdk.requestOtherUsersGameState(spilSdk.getUserProvider(), otherUserIds);
					}
				}
			});
		}
		content.add().fill().expand();


		spilSdk.setSpilGameStateListener(new SpilGameStateListener() {
			@Override public void gameStateUpdated (String access) {
				toasts.show(access + " GS Updated", 2f);
				Gdx.app.log(TAG, "gameStateUpdated " + access);
			}

			@Override public void otherUsersGameStateLoaded (String provider, JsonValue data) {
				toasts.show("OUGS Updated " + provider, 2f);
				Gdx.app.log(TAG, "otherUsersGameStateLoaded " + provider + " " + data);
			}

			@Override public void gameStateError (SpilErrorCode errorCode) {
				toasts.show("GS Error " + errorCode, 2f);
				Gdx.app.log(TAG, "gameStateError " + errorCode);
			}
		});
	}
}
