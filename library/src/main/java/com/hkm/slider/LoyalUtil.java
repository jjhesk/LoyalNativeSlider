package com.hkm.slider;

import android.net.Uri;

import com.hkm.slider.SliderTypes.AdvancedTextSliderView;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.CompactFrameSliderView;
import com.hkm.slider.SliderTypes.CompactSliderView;

/**
 * Created by hesk on 25/11/15.
 * Loyal Helper for making things cleaner and better
 */
public class LoyalUtil {

    public static Uri getUri(BaseSliderView coreSlider) {
        if (coreSlider instanceof CompactFrameSliderView) {
            CompactFrameSliderView t = (CompactFrameSliderView) coreSlider;
            return t.getCurrentClickUri();
        } else if (coreSlider instanceof CompactSliderView) {
            CompactSliderView d = (CompactSliderView) coreSlider;
            return d.getCurrentClickUri();
        } else if (coreSlider instanceof AdvancedTextSliderView) {
            AdvancedTextSliderView d = (AdvancedTextSliderView) coreSlider;
            return d.getTouchURI();
        } else {
            return null;
        }
    }
}
