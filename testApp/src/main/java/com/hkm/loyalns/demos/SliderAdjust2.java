package com.hkm.loyalns.demos;

import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.widget.TextView;

import com.hkm.loyalns.R;
import com.hkm.loyalns.mod.BaseApp;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.AdjustableSlide;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.TransformerL;

import java.util.ArrayList;

/**
 * Created by hesk on 21/4/16.
 */
public class SliderAdjust2 extends BaseApp {
    protected void realSetupSlider() {
        mDemoSlider.setPresetTransformer(TransformerL.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(40000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColorRes(R.color.red_pink_26, R.color.red_pink_27);
        mDemoSlider.setDisablePageIndicator();
        mDemoSlider.setAutoAdjustImageByHeight();
        String[] urls = {
                getString(R.string.hl_image_01),
                getString(R.string.hl_image_02),
                getString(R.string.hl_image_03),
                getString(R.string.hl_image_04),
                getString(R.string.hl_image_11),
                getString(R.string.hl_image_12),
                getString(R.string.hl_image_08),
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
//        mFrameMain.setVisibility(View.VISIBLE);
        mDemoSlider.setOnImageLoadWithAdjustableHeightListener(new SliderLayout.OnImageLoadWithAdjustableHeight() {
            @Override
            public void onNotified(int new_height, boolean isMax) {
                StringBuilder sb = new StringBuilder();
                sb.append("confirmed new height: ");
                sb.append(new_height);
                sb.append("\n");
                sb.append("Is max height has reached: ");
                sb.append(isMax);
                mdisplay_num_text.setText(sb.toString());
            }
        });

        mDemoSlider.loadSliderList(list);
    }

    @Override
    protected void setupSlider() {
        realSetupSlider();
    }

    @Override
    protected int getActivityMainLayoutId() {
        return R.layout.test_adjustment_height;
    }

    @Override
    public void onSliderClick(BaseSliderView coreSlider) {

    }

    private TextView mdisplay_num_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdisplay_num_text = (TextView) findViewById(R.id.display_num_text);
    }
}
