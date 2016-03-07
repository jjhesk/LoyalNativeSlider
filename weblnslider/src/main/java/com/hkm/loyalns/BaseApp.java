package com.hkm.loyalns;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.Tricks.ViewPagerEx;

/**
 * Created by hesk on 24/11/15.
 */
public abstract class BaseApp extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    protected SliderLayout mDemoSlider;

    protected boolean numbered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityMainLayoutId());
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        setupSlider();
    }

    @LayoutRes
    protected int getActivityMainLayoutId() {
        return R.layout.main_slider;
    }

    protected abstract void setupSlider();

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
