package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by PiotrJ on 18/07/16.
 */
public class SpilInputProcessor implements InputProcessor {
	private SpilSdk instance;

	public SpilInputProcessor () {
		this(null);
	}

	public SpilInputProcessor (SpilSdk instance) {
		this.instance = instance;
	}

	public SpilInputProcessor setSpilSdk (SpilSdk instance) {
		this.instance = instance;
		return this;
	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == Input.Keys.BACK && instance != null) {
			instance.onBackPressed();
		}
		return false;
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
