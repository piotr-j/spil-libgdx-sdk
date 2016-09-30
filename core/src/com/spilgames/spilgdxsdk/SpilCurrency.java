package com.spilgames.spilgdxsdk;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by PiotrJ on 13/09/16.
 */
public class SpilCurrency {
	String name;
	int currentBalance;
	int delta;
	int type;

	public SpilCurrency (String name, int currentBalance, int delta, int type) {
		this.name = name;
		this.currentBalance = currentBalance;
		this.delta = delta;
		this.type = type;
	}

	@Override public String toString () {
		return "SpilCurrency{" + "name='" + name + '\'' + ", currentBalance=" + currentBalance + ", delta=" + delta + ", type="
			+ type + '}';
	}

	public static class JsonSerializer implements Json.Serializer<SpilCurrency> {
		@Override public void write (Json json, SpilCurrency object, Class knownType) {
			json.writeObjectStart();
			json.writeValue("name", object.name);
			json.writeValue("currentBalance", object.currentBalance);
			json.writeValue("delta", object.delta);
			json.writeValue("type", object.type);
			json.writeObjectEnd();
		}

		@Override public SpilCurrency read (Json json, JsonValue jsonData, Class type) {
			return new SpilCurrency(
				jsonData.getString("name", null),
				jsonData.getInt("currentBalance", 0),
				jsonData.getInt("delta", 0),
				jsonData.getInt("type", 0)
			);
		}
	}
}
