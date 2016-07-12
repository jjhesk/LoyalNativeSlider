package com.hkm.loyalns.demos;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.hkm.loyalns.R;
import com.hkm.loyalns.mod.BaseApp;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.AdjustableSlide;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.TransformerL;

import java.util.ArrayList;

/**
 * Created by hesk on 15/4/16.
 */
public class SliderAdjust1 extends BaseApp {
    @Override
    protected void setupSlider() {
        mDemoSlider.setPresetTransformer(TransformerL.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(20000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColorRes(R.color.red_pink_26, R.color.red_pink_27);
        mDemoSlider.setAutoAdjustImageByHeight();
        String[] urls = {
                getString(R.string.hl_image_01),
                getString(R.string.hl_image_02),
                getString(R.string.hl_image_03),
                getString(R.string.hl_image_04),
                getString(R.string.hl_image_05),
                getString(R.string.hl_image_07),
                getString(R.string.hl_image_06),
                getString(R.string.hl_image_09),
                getString(R.string.hl_image_10),
                getString(R.string.hl_image_13),

                getString(R.string.star_url_1),
                getString(R.string.star_url_2),
                getString(R.string.star_url_3),
                getString(R.string.star_url_4),
                getString(R.string.star_url_5)
        };

        ArrayList<AdjustableSlide> list = new ArrayList<>();
        for (int h = 0; h < urls.length; h++) {
            AdjustableSlide textSliderView = new AdjustableSlide(this);
            // initialize a SliderLayout
            textSliderView
                    .image(urls[h])
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setOnSliderClickListener(this);
            //add your extra information
            list.add(textSliderView);
        }
        mFrameMain.setVisibility(View.VISIBLE);
        mDemoSlider.loadSliderList(list);

    }

    @Override
    protected int getActivityMainLayoutId() {
        return R.layout.bouncer_slider;
    }

    @Override
    public void onSliderClick(BaseSliderView coreSlider) {

    }
}
