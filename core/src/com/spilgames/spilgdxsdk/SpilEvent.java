package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;


/**
 * Wrapper for Spil Events
 * On Android they are using json, on iOS dictionaries
 * We will use maps
 *
 * Created by PiotrJ on 18/07/16.
 */
public class SpilEvent {
	private static String TAG = "SpilEvent";
	private String name;
	ObjectMap<String, String> data;
	ObjectMap<String, String> customData;
	private boolean queued = false;
	private long timestamp = System.currentTimeMillis();

	public SpilEvent() {
	}

	public static String getTAG() {
		return TAG;
	}

	public static void setTAG(String TAG) {
		SpilEvent.TAG = TAG;
	}

	public void addData(String key, String value) {
		try {
			if (data == null) data = new ObjectMap<>();
			data.put(key, value);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "error adding data " + ex.getMessage());
		}
	}

	public void addCustomData(String key, String value) {
		try {
			if (customData == null) customData = new ObjectMap<>();
			customData.put(key, value);
		} catch (Exception var4) {
			Gdx.app.error(TAG, "error adding data " + var4.getMessage());
		}

	}

	public void addCustomData(String key, int value) {
		try {
			if (customData == null) customData = new ObjectMap<>();
			customData.put(key, Integer.toString(value));
		} catch (Exception var4) {
			Gdx.app.error(TAG, "error adding data " + var4.getMessage());
		}

	}

	public String getData(String key) {
		if (data == null) return null;
		try {
			return data.get(key, null);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "error getting data " + ex.getMessage());
			return null;
		}
	}

	public String getCustomData(String key) {
		if (customData == null) return null;
		try {
			return customData.get(key, null);
		} catch (Exception ex) {
			Gdx.app.error(TAG, "error getting data " + ex.getMessage());
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isQueued() {
		return queued;
	}

	public void setQueued(boolean queued) {
		this.queued = queued;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public ObjectMap<String, String> getData () {
		return data;
	}

	public ObjectMap<String, String> getCustomData () {
		return customData;
	}
}
