package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class ZendeskScreen extends BackScreen {
	private final static String TAG = ZendeskScreen.class.getSimpleName();

	public ZendeskScreen (final SpilGame game) {
		super(game);

		content.defaults().pad(5);
		content.add(new VisLabel("Zendesk")).row();;
		{
			VisTextButton button = new VisTextButton("Help Center");
			content.add(button).row();
			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					// Theme.AppCompat required on android
					spilSdk.showZendeskHelpCenter();
				}
			});
		}
		{
			VisTextButton button = new VisTextButton("WebView Help Center");
			content.add(button).row();
			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					spilSdk.showZendeskWebViewHelpCenter();
				}
			});
		}
		{
			VisTextButton button = new VisTextButton("Contact Center");
			content.add(button).row();
			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					// Theme.AppCompat required on android
					spilSdk.showZendeskContactCenter();
				}
			});

			// move content to the top
			content.add().expand().fill();
		}
	}
}
