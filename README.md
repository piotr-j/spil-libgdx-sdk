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
If your game implements the game configuration option, use the following lines of code:
```java
// Listener that notifies the app when the game data has been updated
SpilGameDataListener spilGameDataListener = new SpilGameDataListener() {
    @Override public void gameDataAvailable () {

    }

    @Override public void gameDataError (SpilErrorCode error) {

    }
};
spilSdk.setSpilGameDataListener(spilGameDataListener);

//Returns the whole configuration file as JsonValue
JsonValue config = spilSdk.getConfig();
```

If there is no network connection, then the SDK will use a default config file.
In exceptional cases people will open the app for the first time and don’t have an internet connection. For those cases we need to add a default configuration in the assets folder of the project:
Add a file named **‘defaultGameConfig.json‘** in the assets folder of your project.
Here is an example of a simple config file for the game Pixel Wizard:

```json
{ "enemyhealth":{ "Bat":10, "Beardwoman":400, "Clown":35 }, "enemycoindrop":{ "Bat":1, "Beardwoman":7, "Clown":2 }, "WizardDamage":{ "Wizard1":25, "Wizard2":35 } }
```

Please get in touch if you need help designing your config file

### Advertisement

Interstitial ads are shown based on incoming events. Discuss with the Spil Games Representative to determine which events should trigger interstitial ads.

Important note for Android:
Chartboost is one of the underlying SDK’s we use and in special cases we need want to load the Chartboost App id from the Spil “defaultGameConfig.json”.
You can see this page to understand more about the Spil’s default config feature.
```json
{
    "chartBoostAppId":"ValueHere",
    "chartBoostAppSignature":"ValueHere"
}
```

The first step in implementing the Advertisement is to create a “SpilAdCallbacks” object and set that in the SpilSDK, with the following code:
```java
spilSdk.setSpilAdListener(new SpilAdListener() {
    @Override public void adAvailable (String type) {
        Gdx.app.log("Ad Callbacks", "Ad " + type + " is available!");

        //Based on the type you can either play a video or show more apps
        //showRewardVideo() and showMoreApps() can be called from anywhere in your app if the AdAvailable is called
        if(type.equals("rewardVideo")){
            spilSdk.showRewardVideo();
        } if(type.equals("moreApps")){
            spilSdk.showMoreApps();
        }
    }

    @Override public void adNotAvailable (String type) {
        //Ad type can either be: interstitial, rewardVideo, moreApps
        Gdx.app.log("Ad Callbacks", "Ad " + type + " is not available!");
    }

    @Override public void adStart () {
        Gdx.app.log("Ad Callbacks", "Ad started");
    }

    @Override public void adFinished (JsonValue response) {
        //The response is a JsonValue with the following data:
        //      type = interstitial/rewardVideo/moreApps
        //      reason = close/dismiss
        //      reward = reward(JSON object) /empty (no field)
        //      network = DFP/Fyber/Chartboost

        Gdx.app.log("Ad Callbacks", "Ad finished! Response = " + response);
    }
});
```

Next a request needs to be made:
```java
//Request a reward video
spilSdk.requestRewardVideo();

//Request more apps
spilSdk.requestMoreApps();

//For interstitials they are automatically recieved and handled (showing) from the Spil Games backend so no call is required
```

Normally the SDK handles the full process of initialisation and showing logic of Ads automatically. 
For testing purposes the following methods are provided:

- Initiation on **Android** backend only:
```java
//DFP
spilSdk.startDFP("your Ad Unit Id");

//Fyber
spilSdk.startFyber("your App Id", "your Token");

//Chartboost
spilSdk.setupChartBoost("your App Id", "your App Signature);
```

- Showing Ads:
```java
//DFP - for testing purposes
spilSdk.devRequestAd("DFP", "interstitial", false);

//Fyber - for testing purposes
spilSdk.devRequestAd("Fyber", "rewardVideo", false);

//Chartboost - for testing purposes
spilSdk.devRequestAd("ChartBoost", "interstitial", false);
spilSdk.devRequestAd("ChartBoost", "rewardVideo", false);
spilSdk.devRequestAd("ChartBoost", "moreApps", false);
```

### Push Notifications

If your game has the extra notification feature, set following listener:
```java
spilSdk.setSpilRewardListener(new SpilRewardListener() {
    @Override public void onRewardReceived (JsonValue data) {
        // handle reward logic
    }
});
```

The payload value is returned as a JsonValue.

### User ID

When a user contacts Spil customer support he/she may be asked for a Spil user id. Spil currently does not require registration for users and so cannot link an email to a Spil user id. All users are essentially guest users and thus have an anonymous user-id. For Android the developer should show the Spil user id in-game, for instance in a settings screen. For iOS this is not required, the user can find the Spil user id outside of the game.

The Spil user id can be obtained by calling the `instance.getSpilUserID();` method of the SpilSDK. This works for the Android SpilSDK, other backends return empty String.

### Wallet, Shop & Inventory

This section describes how the Spil SDK handles a users wallet an in-game shop and the users inventory.

_Wallet_

The wallet feature is holding a users virtual balance of a particular currency. E.g. the user has 100 coins in his wallet. A wallet can contain multiple currencies, e.g. coins and diamonds.

_In-game shop_

A user can buy items or bundles (a pack of multiple items) with his virtual currency. Let’s say a user can buy a sword with 100 coins.

_Inventory_

When a user bought an item in the in-game shop it will be added to his personal inventory.

#### Background information

Before talking about the actual calls for these features it’s good to know the structure of the objects that the Spil SDK uses to manage these features:

- Spil Game Data
- Spil Player Data

_Spil Game Data_

The Spil Game Data contains the information about the game. Like which currencies, Items, Bundles, Shop and Shop Promotions a game might have. See below the details:

- **Currencies** – this list contains all the currencies present in the game and defined in the Spil SLOT interface; each Currency of this list has an id, a name and a type (0 = premium and 1 = non-premium)
- **Items** – this list contains all the items present in the game and defined in the Spil SLOT interface; each Item of this list has an id, a name and a type (0 = Consumable, 1 = Booster and 2 = Permanent)
- **Bundles** – this list contains all the bundles present in the game and defined in the Spil SLOT interface; each Bundle contains an id, a name, a list of one or more Items that will be gained by the player when he buys the bundle and a list of one or more prices that relate to the Currencies.
- **Shop** – this object contains a configuration of the shop as defined in SLOT interface; the object contains a list of Tabs that the define the shop layout; each Tab contains a name, a position and a list of Entries; each Entry in the list has a label that would be displayed in the shop, a position and a Bundle id which points to the received Bundles
- **Promotions** – this list contains all the promotions that would be associated with the Shop and configured in the SLOT interface; each Promotion contains a Bundle id which would point to the received Bundles, an amount value which would represent how many amounts of the bundle will be in the promotion, an updated prices list, a discount label and a start and end date

_Spil Player Data_
A Player Data contains the information related to what the user owns, the users Wallet and Inventory. See below the details:

- **Wallet** – this object contains the information regarding the user’s balance; the Wallet contains a list of Currencies that match the Currencies defined in the Spil Game Data with the addition of having a current balance and a delta, an offset which informs the Spil server which version of the wallet the game is currently having and a logic field which could either be CLIENT or SERVER (for now CLIENT logic is fully implemented)
- **Inventory** – similar object to the Wallet except it holds information about the current Items that the user have

#### Implementation

_Step 1) Default values_

If there is no network connection, then the SDK will use the latest known defaults.
In exceptional cases, people will open the app for the first time and don’t have an Internet connection. For this specific case we need to add two defaults JSON files in the assets folder of the project:

Add a file named ‘defaultGameData.json‘ in the assets/streaming **assets** folder of your project.
Here is an example of a simple example file:
```json
{
  "items": [
      {
          "id": 1,
          "name": "Sword",
          "type": 0
      }, {
          "id": 2,
          "name": "Shield",
          "type": 0
      }, {
          "id": 3,
          "name": "Axe",
          "type": 0
      }, {
          "id": 4,
          "name": "Spear",
          "type": 0
      }
  ],
  "bundles": [
      {
          "id": 1,
          "name": "Sword",
          "prices": [
              {
                "currencyId": 1, 
                "value": 2
              }
          ],
          "items": [
            {
              "id": 1, 
              "amount": 1
            }
          ]
      }, 
      {
          "id": 2,
          "name": "Shield",
          "prices": [
              {
                "currencyId": 1, 
                "value": 4
              }
          ],
          "items": [
            {
              "id": 2, 
              "amount": 1
            }
          ]
      }, 
      {
          "id": 3,
          "name": "Weapons",
          "prices": [
              {
                "currencyId": 2, 
                "value": 50
              }
          ],
          "items": [
            {
              "id": 1, 
              "amount": 1
            }, 
            {
              "id": 3, 
              "amount": 1
            }, 
            {
              "id": 4, 
              "amount": 1
            }
          ]
      }, 
      {
          "id": 4,
          "name": "Warior",
          "prices": [
              {
                "currencyId": 1, 
                "value": 20
              }
          ],
          "items": [
            {
              "id": 4, 
              "amount": 1
            }
          ]
      }
  ],
  "currencies": [
      {
          "id": 1,
          "name": "Coins",
          "type": 0
      }, {
          "id": 2,
          "name": "Diamonds",
          "type": 1
      }
  ],
  "shop": [
      {
          "name": "tab1",
          "entries": [
              {
                  "bundleId": 1,
                  "label": "someLabel",
                  "position": 1
              }, {
                  "bundleId": 2,
                  "label": "someLabel",
                  "position": 2
              }
          ]
      }, {
          "name": "tab2",
          "entries": [
              {
                  "bundleId": 3,
                  "label": "someLabel",
                  "position": 1
              }, {
                  "bundleId": 4,
                  "label": "someLabel",
                  "position": 2
              }
          ]
      }
  ],
  "promotions": [
      {
          "bundleId": 1,
          "amount": 5,
          "prices": [{
              "currencyId": 1,
              "value": 100
          }],
          "discount": "20",
          "startDate": "2016-01-01 19:00:00",
          "endDate": "2016-01-01 20:00:00"
      }, {
          "bundleId": 3,
          "amount": 1,
          "prices": [{
              "currencyId": 1,
              "value": 100
          }],
          "discount": "50%",
          "startDate": "2016-01-01 19:00:00",
          "endDate": "2016-01-01 20:00:00"
      }
  ]
}
```

Add a file named ‘defaultPlayerData.json‘ in the assets/streaming assets folder of your project.
Here is an example of a simple example file:
```json
{
  "wallet":
    {
      "currencies":[
        {
          "id": 22,
          "currentBalance": 0,
          "delta": 0
        },
        {
          "id": 13,
          "currentBalance": 0,
          "delta": 0
        }
      ],
      "offset": 0,
      "logic": "CLIENT"
    },
  "inventory":
    {
      "items":[
      ],
      "offset":0,
      "logic": ""
    }
}
```

_Step 2) Register Callbacks_

The second step that needs to be done is to register the two callbacks using the following code:

For the Spil Game Data:
```java
spilSdk.setSpilGameDataListener(new SpilGameDataListener() {
    @Override public void gameDataAvailable () {
        
    }

    @Override public void gameDataError (SpilErrorCode error) {

    }
});
```

For the Player Data:
```java
spilSdk.setSpilPlayerDataListener(new SpilPlayerDataListener() {
    @Override public void playerDataAvailable () {
        
    }

    @Override public void playerDataUpdated (String reason) {

    }

    @Override public void playerDataError (SpilErrorCode errorCode) {

    }
});
```

_Step 3) Retrieve the data_

After everything has been setup you can use the following method to retrieve the full Spil Game Data (contains information regarding Currencies, Items, Bundles, Shop and Promotions):
```java
JsonValue gameData = spilSdk.getGameData();
```

In order to retrieve the Wallet data use the following code:
```java
JsonValue wallet = spilSdk.getWallet();
```

In order to retrieve the Inventory data use the following code:
```java
JsonValue inventory = spilSdk.getInventory();
```

_Step 4) Update the Wallet / Inventory_

The last step is to use one of the following operations to update your Users Wallet or Inventory:
```java
//Wallet

  //Adding Currency to the Wallet
  //currencyId - the id of the currency
  //delta - the amount of currency
  //reason - the reason for which the wallet was updated; can use standardised reasons(See #1) or custom ones
  spilSdk.addCurrencyToWallet(currencyId, delta, reason);
  
  //Subtracting Currency from the Wallet
  //currencyId - the id of the currency
  //delta - the amount of currency
  //reason - the reason for which the wallet was updated; can use standardised reasons(See #1) or custom ones
  spilSdk.subtractCurrencyFromWallet(currencyId, delta, reason);
  
//Inventory

  //Adding Item to the Inventory
  //itemId - the id of the item
  //amount - the amount of items
  //reason - the reason for which the inventory was updated; can use standardised reasons(See #1) or custom ones
  spilSdk.addItemToInventory(itemId, amount, reason);
  
  //Subtracting Item to the Inventory
  //itemId - the id of the item
  //amount - the amount of items
  //reason - the reason for which the inventory was updated; can use standardised reasons(See #1) or custom ones
  spilSdk.subtractItemFromInventory(itemId, amount, reason);
  
//Bundle
  
  //Consuming Bundle to the Inventory
  //bundleId - the id of the bundle
  //reason - the reason for which the inventory was updated; can use standardised reasons(See #1) or custom ones
  spilSdk.consumeBundle(bundleId, reason);
  
//#1 Standardised reasons
    PlayerDataUpdateReasons.RewardAds = "Reward Ads";
    PlayerDataUpdateReasons.ItemBought = "Item Bought";
    PlayerDataUpdateReasons.ItemSold = "Item Sold";
    PlayerDataUpdateReasons.EventReward = "Event Reward";
    PlayerDataUpdateReasons.LoginReward = "Login Reward";
    PlayerDataUpdateReasons.IAP = "IAP";
    PlayerDataUpdateReasons.PlayerLevelUp = "Player Level Up";
    PlayerDataUpdateReasons.LevelComplete = "Level Complete";
    PlayerDataUpdateReasons.ItemUpgrade = "Item Upgrade";
    PlayerDataUpdateReasons.BonusFeatures = "Bonus Features";
    PlayerDataUpdateReasons.Trade = "Trade";
    PlayerDataUpdateReasons.ClientServerMismatch = "Client-Server Mismatch";
    PlayerDataUpdateReasons.ItemPickedUp = "Item Picked Up";
```



