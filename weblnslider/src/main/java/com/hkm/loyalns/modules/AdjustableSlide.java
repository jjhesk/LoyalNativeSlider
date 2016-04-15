package com.hkm.loyalns.modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hkm.loyalns.R;
import com.hkm.slider.SliderTypes.DefaultSliderView;

/**
 * Created by hesk on 15/4/16.
 */
public class AdjustableSlide extends DefaultSliderView {
    public AdjustableSlide(Context context) {
        super(context);
    }


    @Override
    public View getView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_adjustable_slide, null);
        ImageView target = (ImageView) v.findViewById(R.id.ns_slider_image);
        applyImageWithSmartBoth(v, target);
        return v;
    }
}
