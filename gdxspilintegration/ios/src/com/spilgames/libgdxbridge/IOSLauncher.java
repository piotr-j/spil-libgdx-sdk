package com.spilgames.libgdxbridge;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.spilgames.spilgdxsdk.ios.robovm.IosRoboVMSpilSdk;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIRemoteNotification;
import org.robovm.pods.google.analytics.GAI;
import org.robovm.pods.google.analytics.GAIDictionaryBuilder;
import org.robovm.pods.google.analytics.GAITracker;

public class IOSLauncher extends IOSApplication.Delegate {
	private final static String TAG = IOSLauncher.class.getSimpleName();

	private static final String GA_TRACKER = "UA-48906028-3";

	IosRoboVMSpilSdk spilSdk;
	@Override protected IOSApplication createApplication () {

		final IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		com.spilgames.libgdxbridge.PlatformBridge bridge = new com.spilgames.libgdxbridge.PlatformBridge() {
			@Override public void onCreate () {
				/* TODO fix this, lib that implements this is hard to come by
					looks like we need libGGLCore.a from somewhere */
//                try {
//                    GGLContext.getSharedInstance().configure();
//                } catch (NSErrorException e) {
//                    System.err.println("Error configuring the Google context: " + e.getError());
//                }

				GAI gai = GAI.getSharedInstance();
//					gai.getLogger().setLogLevel(GAILogLevel.Verbose);
				gai.setTracksUncaughtExceptions(true);

				NSDictionary<?, ?> event = GAIDictionaryBuilder.createEvent("app_lifecycle", "app_start", "app started", null)
					.build();

				GAITracker tracker = GAI.getSharedInstance().getTracker(GA_TRACKER);
				tracker.send(event);
				tracker.setAllowsIDFACollection(true);

			}
		};

		spilSdk = new IosRoboVMSpilSdk(this);
		spilSdk.onCreate();
		spilSdk.registerPushNotifications();

		return new IOSApplication(new SpilGame(spilSdk, bridge), config);
	}

	@Override public void didBecomeActive (UIApplication application) {
		super.didBecomeActive(application);
		spilSdk.didBecomeActive(application);
	}

	@Override public void didEnterBackground (UIApplication application) {
		super.didEnterBackground(application);
		// pause in gdx is called from willResignActive, but stuff we want is probably better handled in this callback
		spilSdk.didEnterBackground(application);
	}

	@Override public void didReceiveRemoteNotification (UIApplication application, UIRemoteNotification userInfo) {
		super.didReceiveRemoteNotification(application, userInfo);
		if (spilSdk != null) {
			spilSdk.didReceiveRemoteNotification(application, userInfo);
		}
	}

	@Override public void didRegisterForRemoteNotifications (UIApplication application, NSData deviceToken) {
		spilSdk.didRegisterForRemoteNotifications(deviceToken);
	}

	public static void main (String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}
}
