package com.spilgames.spilgdxsdk.ios.robovm.bindings;

import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.annotation.NativeClass;
import org.robovm.rt.bro.annotation.Library;

/**
 * RoboVM Bindings for Spil.h
 *
 * Created by PiotrJ on 13/07/16.
 */
@Library(Library.INTERNAL)
@NativeClass
public class JsonUtil extends NSObject {

	@Method(selector = "convertStringToObject:")
	public static native NSObject convertStringToObject(String jsonString);

	@Method(selector = "convertObjectToJson:")
	public static native NSString convertObjectToJson(NSObject json);
}
