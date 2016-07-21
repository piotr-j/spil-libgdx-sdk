package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Wrapper for Spil Events
 * On Android they are using json, on iOS dictionaries
 * We will use maps
 *
 * Created by PiotrJ on 18/07/16.
 */
public class SpilEvent {
	private static String TAG = "SpilEvent";
	protected String name;
	JsonValue data;
	JsonValue customData;
	protected boolean queued = false;
	protected long timestamp = System.currentTimeMillis();

	public SpilEvent() {
		this(null);
	}

	public SpilEvent(String name) {
		setName(name);
	}

	public static String getTAG() {
		return TAG;
	}

	public static void setTAG(String TAG) {
		SpilEvent.TAG = TAG;
	}

	public void addData(String key, String value) {
		try {
			if (data == null) data = new JsonValue(JsonValue.ValueType.object);
			addChild(data, key, new JsonValue(value));
		} catch (Exception ex) {
			Gdx.app.error(TAG, "error adding data " + ex.getMessage());
		}
	}

	public void addCustomData(String key, String value) {
		try {
			if (customData == null) customData = new JsonValue(JsonValue.ValueType.object);
			addChild(customData, key, new JsonValue(value));
		} catch (Exception var4) {
			Gdx.app.error(TAG, "error adding data " + var4.getMessage());
		}
	}

	private void addChild(JsonValue root, String key, JsonValue value) {
		value.name = key;
		value.parent = root;
		if (root.child == null) {
			root.child = value;
		} else {
			JsonValue ch = root.child;
			while (ch.next != null) {
				ch = ch.next;
			}
			ch.next = value;
			value.prev = ch;
		}
	}

	public void addCustomData(String key, int value) {
		try {
			if (customData == null) customData = new JsonValue(JsonValue.ValueType.object);
			addChild(customData, key, new JsonValue(value));
		} catch (Exception var4) {
			Gdx.app.error(TAG, "error adding data " + var4.getMessage());
		}
	}


	public void addCustomData(String key, JsonValue value) {
		try {
			if (customData == null) customData = new JsonValue(JsonValue.ValueType.object);
			addChild(customData, key, value);
		} catch (Exception var4) {
			Gdx.app.error(TAG, "error adding data " + var4.getMessage());
		}
	}

	public JsonValue getDataValue(String key) {
		if (data == null) return null;
		return data.get(key);
	}

	public String getData(String key) {
		JsonValue dataValue = getDataValue(key);
		if (dataValue == null) return null;
		try {
			return dataValue.toString();
		} catch (Exception ex) {
			Gdx.app.error(TAG, "error getting data " + ex.getMessage());
			return null;
		}
	}

	public JsonValue getCustomDataValue(String key) {
		if (customData == null) return null;
		return customData.get(key);
	}

	public String getCustomData(String key) {
		JsonValue dataValue = getCustomDataValue(key);
		if (dataValue == null) return null;
		try {
			return dataValue.toString();
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

	public JsonValue getData () {
		return data;
	}

	public JsonValue getCustomData () {
		return customData;
	}

	@Override public String toString () {
		return "SpilEvent{" + "name='" + name + '\'' + ", data=" + data + ", customData=" + customData + '}';
	}
}
