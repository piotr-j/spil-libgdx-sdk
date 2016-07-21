package com.spilgames.spilgdxsdk;

/**
 * Wrapper for spil response event
 *
 * Created by PiotrJ on 18/07/16.
 */
public class SpilResponseEvent extends SpilEvent {
	private String action;
	private String type;
	private String ts;

	public SpilResponseEvent () {
		super();

	}

	public String getAction () {
		return action;
	}

	public SpilResponseEvent setAction (String action) {
		this.action = action;
		return this;
	}

	public String getType () {
		return type;
	}

	public SpilResponseEvent setType (String type) {
		this.type = type;
		return this;
	}

	public String getTs () {
		return ts;
	}

	public SpilResponseEvent setTs (String ts) {
		this.ts = ts;
		return this;
	}

	@Override public String toString () {
		return "SpilResponseEvent{" + "action='" + action + '\'' + ", type='" + type + '\'' + ", ts='" + ts + '\'' + "name='" + name + '\'' + ", data=" + data + ", customData=" + customData +"}";
	}
}
