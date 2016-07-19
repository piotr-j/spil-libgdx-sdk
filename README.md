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

See example app for details

**Google Analytics**


**Adjust SDK**


## Installation 

**Core**

Add this to your build.gradle core dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-core:2.1.0"
```

**Android**

Add following libraries to `android/libs` directory found in same directory in this project
 - mm-ad-sdk.aar  
 - unity-ads.aar  
 - spilSDK-2.1.0.jar  

Also add `spil-gdx-sdk-android-2.1.0.aar` from local .m2 repository  

Add this to your build.gradle android dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-android:2.1.0@aar"

compile fileTree(dir: 'libs', include: '*.jar')
```



**iOS**

Follow the instructions [on libGDX wiki](https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29#setting-up-robovm-for-ios-development) to setup RoboVM


Add this to your build.gradle ios dependencies
```gradle
compile "com.spilgames.spilgdxsdk:spil-gdx-sdk-ios-robovm:2.1.0"
```

NOTE currently this is a custom Spil.framework, build without unity thing that requires `-Wl,-U,_UnitySendMessage`
`

Copy Spil.framework from ios/libs directory into ios/libs in your project 

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

Add json configuration files to `android/assets` directory, defaultGameConfig, defaultGameData, defaultPlayerData

see test app!

