package com.adjust.bindings;

import org.robovm.apple.foundation.NSObject;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.annotation.NativeClass;
import org.robovm.rt.bro.annotation.Library;

/**
 * Created by PiotrJ on 13/07/16.
 */
@Library(Library.INTERNAL)
@NativeClass
public class Adjust extends NSObject {
	public static final String ENVIRONMENT_SANDBOX = "sandbox";

//	 + (void)appDidLaunch:(ADJConfig *)adjustConfig;

	@Method(selector = "appDidLaunch:")
	public static native void appDidLaunch (ADJConfig config);


//	+ (void)trackEvent:(ADJEvent *)event;

	@Method(selector = "trackEvent:")
	public static native void trackEvent (ADJEvent event);

}
