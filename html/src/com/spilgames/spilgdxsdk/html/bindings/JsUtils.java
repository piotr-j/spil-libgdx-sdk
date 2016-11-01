package com.spilgames.spilgdxsdk.html.bindings;

/**
 * Created by PiotrJ on 01/11/2016.
 */
public class JsUtils {

	public static native void log(String tag, String message) /*-{
        try {
            console.log(tag + ": "+ message);
        } catch (e) {
        }
    }-*/;
}
