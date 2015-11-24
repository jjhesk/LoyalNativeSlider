# Loyal Native Slider 
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Loyal%20Native%20Slider-brightgreen.svg?style=flat)][6][ ![Download](https://api.bintray.com/packages/jjhesk/maven/library/images/download.svg) ][7][![Android Gems](http://www.android-gems.com/badge/jjhesk/LoyalNativeSlider.svg?branch=master)][8][![Gitter](https://badges.gitter.im/Join Chat.svg)][9]
 
Master: [![Build Status](https://travis-ci.org/jjhesk/LoyalNativeSlider.svg)](https://travis-ci.org/jjhesk/LoyalNativeSlider)

Dev: [![Build Status](https://travis-ci.org/jjhesk/LoyalNativeSlider.svg?branch=dev)](https://travis-ci.org/jjhesk/LoyalNativeSlider)

This is an amazing image slider for the Android platform. I decided to open source this because there is really not an attractive, convenient slider widget in Android.
 
You can easily load images from an internet URL, drawable, or file. And there are many kinds of amazing animations you can choose. :-D

##Features

###v1.3.3
- [x] Add local storage enable for picasso
- [ ] adapting multiple images in one slide config 2-4

## Demo

 
## Usage

### Step 1

#### Gradle
[ ![Download](https://api.bintray.com/packages/jjhesk/maven/library/images/download.svg) ](https://bintray.com/jjhesk/maven/library/_latestVersion)
```gradle

dependencies {
    compile 'com.hkm.loyalslider:library:1.3.3'
}

```

### Step 2

Add permissions (if necessary) to your `AndroidManifest.xml`

```xml
<!-- if you want to load images from the internet -->
<uses-permission android:name="android.permission.INTERNET" /> 

<!-- if you want to load images from a file OR from the internet -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

**Note:** If you want to load images from the internet, you need both the `INTERNET` and `READ_EXTERNAL_STORAGE` permissions to allow files from the internet to be cached into local storage.

If you want to load images from drawable, then no additional permissions are necessary.

### Step 3

Add the Slider to your layout:
 
```java
<com.hkm.slider.SliderLayout
        android:id="@+id/slider"
        android:layout_width="match_parent"
        android:layout_height="200dp"
/>
```        
 
There are some default indicators. If you want to use a provided indicator:
 
```java
<com.hkm.slider.Indicators.PagerIndicator
        android:id="@+id/custom_indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        />
```

[Code example](https://github.com/jjhesk/LoyalNativeSlider/blob/master/demo/src/main/java/com/daimajia/slider/demo/MainActivity.java)
 
====
## Preview
<img src="scn/device-2015-06-17-142823.png" width="300px"/>
<img src="scn/device-2015-06-17-150718.png" width="300px"/>
<img src="scn/device-2015-06-17-150829.png" width="300px"/>


New Version Demo:
https://www.youtube.com/watch?v=7P6lv662n-8&feature=youtu.be


## Advanced usage
Visit the Transform [Wiki](https://github.com/jjhesk/LoyalNativeSlider/wiki)
Please visit [Wiki](https://github.com/daimajia/AndroidImageSlider/wiki) for the mother library
 
## Thanks for the Reference

- [Picasso][2]
- [NineOldAndroids][3]
- [ViewPagerTransforms][4]
- [Android Image Slider][5]

If there are someone who I do not mention here, please accept my sincere appologies and tell me.



###Donation:
Open Donation - USD: [![OPEN](http://i.imgur.com/wUWK6e1.jpg)][1]

License
--------

    Copyright 2015 JJHesk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


##About me
[1]: https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=ooxfordck@gmail.com&currency_code=USD&amount=&return=&item_name=LoyalSliderDonation
[2]: https://github.com/square/picasso
[3]: https://github.com/JakeWharton/NineOldAndroids
[4]: https://github.com/ToxicBakery/ViewPagerTransforms
[5]: https://github.com/daimajia/AndroidImageSlider
[6]: http://android-arsenal.com/details/1/1998
[7]: https://bintray.com/jjhesk/maven/library/_latestVersion
[8]: http://www.android-gems.com/lib/jjhesk/LoyalNativeSlider
[9]: https://gitter.im/daimajia/AndroidImageSlider?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
