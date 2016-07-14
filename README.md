# Loyal Native Slider
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Loyal%20Native%20Slider-brightgreen.svg?style=flat)][6][ ![Download](https://api.bintray.com/packages/jjhesk/maven/library/images/download.svg) ][7][![Android Gems](http://www.android-gems.com/badge/jjhesk/LoyalNativeSlider.svg?branch=master)][8][![Gitter](https://badges.gitter.im/Join Chat.svg)][9]

[![Throughput Graph](https://graphs.waffle.io/jjhesk/LoyalNativeSlider/throughput.svg)][14]

This is an amazing image slider for the Android platform. I decided to open source this because there is really not an attractive, convenient slider widget in Android. You can easily load images from an internet URL, drawable, or file. And there are many kinds of amazing animations you can choose. :-D

##Demo Apk
Please also check the latest release testing apk from the [log history](loghistory.md)

## Usage
### Step 1
#### Gradle
[![Download](https://api.bintray.com/packages/jjhesk/maven/library/images/download.svg) ](https://bintray.com/jjhesk/maven/library/_latestVersion)

JCenter is the place to find and share popular Apache Maven packages for use by Maven, Gradle, Ivy, SBT, etc.
For the most comprehensive collection of artifacts, point your Maven at: `http://jcenter.bintray.com`

```gradle
dependencies {
    compile 'com.hkm.loyalslider:library:1.9.4'
}
```

### Step 2 - setup the xml

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

```xml
<com.hkm.slider.SliderLayout
        android:id="@+id/slider"
        android:layout_width="match_parent"
        android:layout_height="200dp"
/>
```

There are some default indicators. If you want to use a provided indicator you can add this optionally

```xml
<com.hkm.slider.Indicators.PagerIndicator
        android:id="@+id/custom_indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        />
```

### Step 4
Implement the SliderLayout component in the fragment or activity

### Step 5
making some small adjustments

### Step 6
setup the slider layout either by your own custom extension or using the existing provided slides from the folder SlideType

### Step 7
binding data from json or your own data providers

### Step 8
completion of the `arrayList` with your own type `T` , `T` as your data type

### Step 9
call `mSliderLayout.loadSliderList` or `.addSliderList` or `.addSlider` independently. finally, check the result. For more variant and options you may

====

## Preview
![zoom in slider](http://i.giphy.com/tGjjsefxE8Cze.gif)
![multislide](http://i.giphy.com/NtCRzwkgbIupG.gif)


## New Version Demo:
[Mobile Testing](https://www.youtube.com/embed/s0pHZgyLeVo)
[multiple screens support](https://youtu.be/yXuMRDX6Cgk)
[Tablet Testing](https//www.youtube.com/embed/7P6lv662n-8)

=======
## Attribtues

This is the attribute support set for [SliderLayout][11]

| attr | description|
| :---- | :---- |
| indicator_visibility| disable or enable the visible area in the whole component. options: visible, invisable |
| auto_cycle| option to enable or disable auto cycle |
| pager_animation| animation list of choices. page animation |
| pager_animation_span| the page animation time span |
| auto_cycle| option to enable or disable auto cycle |
| slide_dot_limit| slide dot limit |
| lns_use_presentation| choices of presentation on the slider. options: Smart, Dots, Numbers |
| image_button_l| reference of drawable for the left arrow button to enable previous slide |
| image_button_r| reference of drawable for the right arrow button to enable next slide |
| slider_side_buttons| enable or disable the arrow buttons on each right or left side |
| slider_side_buttons_function_flip| unkown |


## Advanced usage
Visit the Transform [Wiki](https://github.com/jjhesk/LoyalNativeSlider/wiki)
Please visit [Wiki](https://github.com/daimajia/AndroidImageSlider/wiki) for the mother library

## Thanks for the Reference
- [Picasso][2]
- [NineOldAndroids][3]
- [ViewPagerTransforms][4]
- [Android Image Slider][5]
- [Glide][15]

If there are someone who I do not mention here, please accept my sincere appologies and tell me.

###Bitcoin Donation Accepted
![wallet](http://s32.postimg.org/sdd1oio1t/qrwallet.jpg)
Open Donation - USD: [![OPEN](http://i.imgur.com/wUWK6e1.jpg)][1]

License
--------

    Copyright 2016 jjHesk

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
[10]: https://github.com/jjhesk/LoyalNativeSlider/blob/master/AppDemo/src/main/java/com/hkm/sliderdemo/MainActivity.java
[11]: https://github.com/jjhesk/LoyalNativeSlider/blob/master/library/src/main/res/values/attrs.xml#L3-L54
[12]: https://github.com/daimajia/AndroidImageSlider/releases/download/v1.0.9/AndroidImageSlider-Eclipse.zip
[14]: https://waffle.io/jjhesk/LoyalNativeSlider/metrics
[15]: https://github.com/bumptech/glide
