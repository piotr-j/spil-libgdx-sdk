package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ToastManager;
import com.kotcrab.vis.ui.widget.ButtonBar;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilSdk;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public abstract class BaseScreen implements Screen, InputProcessor{
	private static final String TAG = BaseScreen.class.getSimpleName();

	protected final SpilGame game;
	protected final OrthographicCamera camera;
	protected final ScreenViewport viewport;
	protected final Color clear = new Color(Color.DARK_GRAY);
	protected final Stage stage;
	protected final Table root;
	protected SpilSdk spilSdk;
	protected ToastManager toasts;

	public BaseScreen (SpilGame game) {
		this.game = game;
		spilSdk = game.spilSdk;
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);

		stage = new Stage(viewport, game.batch);
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));

		toasts = new ToastManager(stage);

		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) {
			VisUI.getSkin().getFont("default-font").getData().setScale(2);
		}
	}

	protected void changeScreen (Class<? extends BaseScreen> aClass) {
		if (aClass == getClass()) return;
		try {
			Constructor constructor = ClassReflection.getConstructor(aClass, SpilGame.class);
			game.setScreen((Screen)constructor.newInstance(game));
		} catch (ReflectionException e) {
			Gdx.app.error(TAG, "Failed to change screen", e);
		}
	}

	protected void dialog(String text) {
		dialog(text, null);
	}

	protected void dialog(String title, String text) {
		VisDialog dialog = new VisDialog(title);
		if (text != null) {
			VisLabel label = new VisLabel(text);
			label.setWrap(true);
			dialog.getContentTable().add(label).width(stage.getWidth() * 3 / 4).expand().fill();
		}
		dialog.button(ButtonBar.ButtonType.OK.getText()).padBottom(3);
		dialog.pack();
		dialog.centerWindow();
		stage.addActor(dialog.fadeIn());
	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
			changeScreen(MainScreen.class);
			return true;
		}
		return false;
	}

	@Override public void show () {

	}

	@Override public void render (float delta) {
		Gdx.gl.glClearColor(clear.r, clear.g, clear.b, clear.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override public void resize (int width, int height) {
		viewport.update(width, height, true);
	}

	@Override public void pause () {

	}

	@Override public void resume () {

	}

	@Override public void hide () {
		dispose();
	}

	@Override public void dispose () {

	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}
}
