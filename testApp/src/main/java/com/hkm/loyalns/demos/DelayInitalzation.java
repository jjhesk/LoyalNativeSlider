package com.hkm.loyalns.demos;

import android.os.Handler;

import com.hkm.slider.SliderLayout;

/**
 * Created by hesk on 30/5/16.
 */
public class DelayInitalzation extends SliderAdjust2 {
    private Handler posthandler = new Handler();
    private Runnable t = new Runnable() {
        @Override
        public void run() {
            realSetupSlider();
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        }
    };

    @Override
    protected void setupSlider() {
        posthandler.postDelayed(t, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        posthandler.removeCallbacks(t);
    }
}
