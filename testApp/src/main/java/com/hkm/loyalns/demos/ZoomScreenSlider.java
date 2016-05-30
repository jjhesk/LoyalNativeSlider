package com.hkm.loyalns.demos;

import android.annotation.TargetApi;
import android.os.Build;

import com.hkm.loyalns.demos.BigScreenDemo;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.ZoomableView;

import java.util.HashMap;

/**
 * Created by hesk on 15/12/15.
 */
public class ZoomScreenSlider extends BigScreenDemo {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final SliderLayout slide, final HashMap<String, String> maps) {
        for (String name : maps.keySet()) {
            ZoomableView zoomer = new ZoomableView(true, this);
            // initialize a SliderLayout
            zoomer
                    //ZoomableView configurations
                    .setInitalZoomFactor(1.f)
                            //BaseSlider configurations
                    .description(name)
                    .image(maps.get(name))
                    .enableImageLocalStorage()
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);
            //add your extra information
            zoomer.getBundle().putString("extra", name);
            slide.addSlider(zoomer);
        }
        slide.stopAutoCycle();
        slide.setCurrentPosition(1);
    }
}
