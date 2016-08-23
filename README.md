# Spil GDX SDK Bridge

Based on Spil SDK 2.1.0

##Supported Platforms
Android, iOS-RoboVM

##Requirements
libGDX 1.9.3+, Android API 15+, ios 7.1+

## Building

To build the sdk, open project in Intellij IDEA and import it as gradle project. Uncheck 'make module per project'

To install it to mavenLocal, execute this gradle task `gradle uploadArchives -PLOCAL=true`

## Installation prerequisites

Before SpilGDX SDK can be added, Google Analytics and Adjust SDK must be integrated

**Google Analytics**

For Android backend, follow these instructions:
https://developers.google.com/analytics/devguides/collection/android/v4/

For iOS RoboVM, follow these instructions:
https://github.com/MobiDevelop/robovm-robopods/tree/master/google-analytics/ios

**Adjust SDK**

For Android backend, follow these instructions:
https://github.com/adjust/android_sdk#basic-integration

For iOS RoboVM, follow these instructions:
Download `AdjustSdkStaticNoBitcode.framework.zip` from https://github.com/adjust/ios_sdk
Unpack it and put `AdjustSdk.framewoek` into `ios/libs` directory.
Adjust `robovm.xml` file to include added framework
```xml
<frameworks>
    // ...
    <framework>AdjustSdk</framework>
</frameworks>
```

Minimal java bindings are included in sample project


## Installation 

Once prerequisites are added and working, Spil sdk can be added.

**Core**

Add this to your `build.gradle` core dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-core:2.1.0"
```

**Android**

Add following libraries to `android/libs` directory found in same directory in this project
 - mm-ad-sdk.aar  
 - unity-ads.aar    

Add this to your build.gradle android dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-android:2.1.0@aar"

compile fileTree(dir: 'libs', include: '*.jar')
```

Due to number of methods, MultiDex is required on Android.
See https://developer.android.com/studio/build/multidex.html for instructions

**iOS**

Follow the instructions [on libGDX wiki](https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29#setting-up-robovm-for-ios-development) to setup RoboVM


Add this to your build.gradle ios dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-ios-robovm:2.1.0"
```

Copy Spil.framework from `ios/libs` directory into `ios/libs` in your project 

Add this to your robovm.xml
```xml
<config>
  <frameworkPaths>
    ...
    <path>libs</path>
    <path>libs/Spil.framework/Frameworks</path>
  </frameworkPaths>
  <frameworks>
    ...
    <framework>Spil</framework>
  </frameworks>
</config>
```

Add this to your info.plist.xml
```xml
<dict>
    ...
    <key>MinimumOSVersion</key>
    <string>7.1</string>
    <key>NSAppTransportSecurity</key>
    <dict>
        <key>NSAllowsArbitraryLoads</key>
        <true/>
    </dict>
</dict>
```


**Desktop**

Add this to your build.gradle desktop dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-desktop:2.1.0"
```

## Usage

General usage is based on Android Spil SDK, see: http://www.spilgames.com/developers/integration/android/implementing-spil-sdk/
Main thing that differs are various lifecycle methods that must be called on different platforms.

**Core**

Your main class implementing ApplicationListener should take SpilSdk interface as one of its parameters. 

**Android**

Android specific calls should be made in android sub project. AndroidLauncher is a decent place to put them.
AndroidSpilSdk contains some Android only methods not available in generic API, see source for details.
AndroidLauncher could look something like this:
 
```java
public class AndroidLauncher extends AndroidApplication {
	// ...
    AndroidSpilSdk spilSdk;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // ...
		spilSdk = new AndroidSpilSdk(this);
		spilSdk.onCreate();
		// any extra android specific calls
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GdxGame(spilSdk), config);
	}

	@Override public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
		@NonNull int[] grantResults) {
		spilSdk.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override protected void onResume () {
		super.onResume();
		spilSdk.onResume();
	}

	@Override protected void onPause () {
		spilSdk.onPause();
		super.onPause();
	}

	@Override public void onStart() {
		super.onStart();
		spilSdk.onStart();
	}

	@Override public void onDestroy() {
		super.onDestroy();
		spilSdk.onDestroy();
	}

	@Override public void onBackPressed () {
		spilSdk.onBackPressed();
	}
}
```
Other sdks omitted for clarity

**iOS**

IOSLauncher could look like this:

```java
public class IOSLauncher extends IOSApplication.Delegate {

	IosRoboVMSpilSdk spilSdk;
	@Override protected IOSApplication createApplication () {
	  
		final IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		spilSdk = new IosRoboVMSpilSdk(this);
		spilSdk.onCreate();
		return new IOSApplication(new GdxGame(spilSdk), config);
	}

	@Override public void didBecomeActive (UIApplication application) {
		super.didBecomeActive(application);
		spilSdk.didBecomeActive(application);
	}

	@Override public void didEnterBackground (UIApplication application) {
		super.didEnterBackground(application);
		spilSdk.didEnterBackground(application);
	}

	@Override public void didReceiveRemoteNotification (UIApplication application, UIRemoteNotification userInfo) {
		super.didReceiveRemoteNotification(application, userInfo);	
        spilSdk.didReceiveRemoteNotification(application, userInfo);
	}
}
```

Other sdks omitted for clarity

**Desktop**

Desktop backend is a dummy implementation facilitating easy testing on desktop platform.
DesktopLauncher could look like this:
```java
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GdxGame(new DesktopSpilSdk()), config);
	}
}
```

See sample gdx application for details
