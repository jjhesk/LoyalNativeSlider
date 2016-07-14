##Log History
###v1.9.4
- adjustable slide callback with the new height and determination of maximum height

###v1.9.3
- android support library 24.0.0
- update build tool
- improve mini frame slider framework
- [release apk](https://github.com/jjhesk/LoyalNativeSlider/releases/download/1.9.3/testApp-debug.apk)

###v1.9.1
- update get color using contextcompat

###v1.9.0
- android build tool 2.1.0
- android support library 23.4.0

###v1.8.2
- auto adjustment for each image height when scroll
- android build tool 2.0.0
- android support library 23.3.0
- fixed bad image urls from the demo app
- added demo example code for the new feature.

###v1.8.0
- Adding call method to enable final detection of the tallest layout among all the loaded sliders. This method is only available thru using calls from `loadSliderList` and `addSliderList`. Because all slides needed to be rendered until all heights are found from each slide, there will be an slight delay from the first render of the sliderLayout. [issue #45](https://github.com/jjhesk/LoyalNativeSlider/issues/45)

###v1.6.0
- add glide. As mentioned from all other recommendations, this library will be now the first choice of the support module to render images. That say all slider will support Gif files now.
- support list insert feature. Instead of having slide to be added one by one, there is a new way to insert items as a list that will be more efficient.

###v1.5.5
- [fixed all the related bugs](https://github.com/jjhesk/LoyalNativeSlider/releases/tag/1.5.5)

###v1.5.0
- Zoomable view
- news feed view

###v1.4.1
- Add local storage enable for picasso
- adapting multiple images in one slide config 2-4
