package com.adjust.bindings;

import org.robovm.apple.foundation.NSObject;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.annotation.NativeClass;
import org.robovm.rt.bro.annotation.Library;
import org.robovm.rt.bro.annotation.Pointer;

/**
 * Created by PiotrJ on 13/07/16.
 */
@Library(Library.INTERNAL)
@NativeClass
public class ADJConfig extends NSObject {

	public static final String ENVIRONMENT_SANDBOX = "sandbox";

	@Method(selector = "configWithAppToken:environment:")
	public static native ADJConfig create (String appToken, String environment);

	public ADJConfig (String appToken, String environment) {
		super((SkipInit)null);
		initObject(initWithAppTokenEnvironment(appToken, environment));
	}

	@Method(selector = "initWithAppToken:environment:")
	private native @Pointer long initWithAppTokenEnvironment(String appToken, String environment);

}
