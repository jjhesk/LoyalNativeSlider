package com.hkm.loyalns.modules;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.loyalns.R;
import com.hkm.slider.SliderTypes.AdvancedTextSliderView;


/**
 * Created by hesk on 19/8/15.
 */
public class CustomNumberView extends AdvancedTextSliderView<TextView, ImageView> {

    public CustomNumberView(Context context) {
        super(context);
    }

    @Override
    protected int renderedLayoutTextBanner() {
        return R.layout.item_slide_feature_banner;
    }
}