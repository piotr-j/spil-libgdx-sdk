package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.spilgdxsdk.SpilEvent;
import com.spilgames.spilgdxsdk.SpilEventActionListener;
import com.spilgames.spilgdxsdk.SpilResponseEvent;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class IAPEventsScreen extends BackScreen {
	private final static String TAG = IAPEventsScreen.class.getSimpleName();

	public IAPEventsScreen (final SpilGame game) {
		super(game);

		content.defaults().pad(5);
		final VisTable valueTable = new VisTable(true);
		final VisTextField eventSKU = new VisTextField("com.spilgames.pixelwizard.pwcp5000");
		eventSKU.setMessageText("SKU ID");
		VisTextButton sendEvent = new VisTextButton("Send");
		valueTable.add(eventSKU).expandX().fillX();
		valueTable.add(sendEvent);
		content.add(valueTable).expandX().fillX().row();
		final VisLabel resultLabel = new VisLabel();
		content.add(resultLabel).expand().fill();

		sendEvent.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				SpilEvent spilEvent = new SpilEvent("iapPurchased");
				spilEvent.addData("skuId", eventSKU.getText());
				spilEvent.addData("transactionId", "test");
				spilEvent.addData("purchaseDate", "today");
				// NOTE we don't get callback on ios for 'iapPurchased' event
				// TODO get proper event stuff for android
				spilSdk.trackEvent(spilEvent, new SpilEventActionListener() {
					@Override public void onResponse (SpilResponseEvent response) {
						resultLabel.setText(response.toString());
					}
				});
			}
		});
	}
}
