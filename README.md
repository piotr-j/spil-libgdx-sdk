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


## Usage examples

All examples assume that you have an SpilGDXSdk instance available

### Event Tracking

To track an event, use the following code:
```java
SpilEvent spilEvent = new SpilEvent();
event.setName("testEvent");
spilSdk.trackEvent(spilEvent, new SpilEventActionListener() {
    @Override public void onResponse (SpilResponseEvent response) {
        // handle response    
    }
});
```

The context variable represents the current Activity Context.
The eventActionListener is usually null unless you specifically need to listen to a response event. All important response events are handled by the SDK.

You can also add custom data to the event by calling:
```java
event.addCustomData("testKey", "testData");
```

In most cases additional custom events are required to be implemented such as:

IAP

| Event types     | Required Parameters          | Note  | 
| --------------- | ---------------------------- | ---- |
| iapPurchased	  | skuId, transactionId, purchaseDate ||
| iapRestored	  | skuId, originalTransactionId, original PurchaseDate | |
| iapFailed	error | skuId | |
	
	
The parameters are explained below:

- skuId (string) – The product identifier of the item that was purchased
- transactionId (string) – The transaction identifier of the item that was purchased (also called orderId)
- purchaseDate (string) – The date and time that the item was purchased
- originalTransactionId (string) – For a transaction that restores a previous transaction, the transaction identifier of the original transaction. Otherwise, identical to the transaction identifier
- originalPurchaseDate (string) – For a transaction that restores a previous transaction, the date of the original transaction
- error (string) – Error description or error code

**Important note for tracking In-app Purchases on Android**

We simplified the tracking of In-app purchases for you. Once you send an “iapPurchased” we alter the data with the price and currency values.
In order to do this we ask you to define the **“googleIAPKey”** in the Spil **“defaultGameConfig.json”**.
You can see [this page](http://www.spilgames.com/developers/integration/unity/implementing-spil-sdk/spil-sdk-game-config/) to understand more about the Spil’s default config feature.

**USER BEHAVIOUR**

| Event types | Required Parameters | Optional | Note |
| --- | --- | --- | --- |
| walletUpdate | walletValue, itemValue, source, item, category ||| 		
| milestoneAchieved | name |||
| levelStart | level |||
| levelComplete | level | score, stars, turns || 	
| levelFailed | level | score, turns||
| playerDies | level ||| 
		
**The parameters are explained below:**

- walletValue (int) – The new wallet value after subtracting the item value. E.g coins
- itemValue (int) – The value of the item consumed. E.g. coins. (note: This property can also be negative, for example if a user spends coins, the itemValue can be -100)
- source (int) – 0 == premium
- item (string) – item id or sku
- category (int) – 0 = Consumable, 1 = Booster, 2 = Permanent
- name (string) – name of the milestone
- level (string) – name of the level
- score (int) – The final score the player achieves at the end of the level
- stars (int) – The # of stars (or any other rating system) the player achieves at the end of the level
- turns (int) – The # of moves/turns taken to complete the level

### Game Config

### Advertisement

### Push Notifications

### User ID

### Wallet, Shop & Inventory





