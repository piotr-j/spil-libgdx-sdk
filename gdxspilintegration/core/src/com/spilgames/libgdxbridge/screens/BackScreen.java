package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class BackScreen extends BaseScreen {
	protected Table content;
	public BackScreen (SpilGame game) {
		super(game);
		content = new Table();
		VisTextButton back = new VisTextButton("BACK");
		back.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				changeScreen(MainScreen.class);
			}
		});
		root.add(back).pad(10);
		root.row();
		root.add(content).expand().fill();
	}
}
