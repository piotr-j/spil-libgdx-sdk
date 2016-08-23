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
public class ADJEvent extends NSObject {

	//+ (ADJEvent *)eventWithEventToken:(NSString *)eventToken;

	@Method(selector = "eventWithEventToken:")
	public static native ADJEvent eventWithEventToken (String eventToken);


}
