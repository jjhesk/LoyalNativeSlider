package com.hkm.slider.SliderTypes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.slider.R;

/**
 * Created by hesk on 11/6/15.
 */
public abstract class AdvancedTextSliderView<TV extends TextView, Image extends ImageView> extends BaseSliderView {
    public AdvancedTextSliderView(Context context) {
        super(context);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(renderedLayoutTextBanner(), null);
        Image target = (Image) v.findViewById(R.id.daimajia_slider_image);
        final TV description = (TV) v.findViewById(R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }

    protected abstract int renderedLayoutTextBanner();
}
