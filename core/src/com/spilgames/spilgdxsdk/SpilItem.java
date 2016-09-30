package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 13/09/16.
 */
public class SpilItem {
	String name;
	int amount;
	int delta;
	int type;

	public SpilItem (String name, int amount, int delta, int type) {
		this.name = name;
		this.amount = amount;
		this.delta = delta;
		this.type = type;
	}

	@Override public String toString () {
		return "SpilItem{" + "name='" + name + '\'' + ", amount=" + amount + ", delta=" + delta + ", type=" + type + '}';
	}

	public static class JsonSerializer implements Json.Serializer<SpilItem> {
		@Override public void write (Json json, SpilItem object, Class knownType) {
			json.writeObjectStart();
			json.writeValue("name", object.name);
			json.writeValue("amount", object.amount);
			json.writeValue("delta", object.delta);
			json.writeValue("type", object.type);
			json.writeObjectEnd();
		}

		@Override public SpilItem read (Json json, JsonValue jsonData, Class type) {
			return new SpilItem(
				jsonData.getString("name", null),
				jsonData.getInt("amount", 0),
				jsonData.getInt("delta", 0),
				jsonData.getInt("type", 0)
			);
		}
	}
}
