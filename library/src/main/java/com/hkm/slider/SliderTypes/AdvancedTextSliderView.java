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
        final Image target = (Image) v.findViewById(R.id.ns_slider_image);
        final TV description = (TV) v.findViewById(R.id.ns_slider_desc);
        description.setText(getDescription());
        bindEventAndShowPicasso(v, target);
        return v;
    }

    protected abstract int renderedLayoutTextBanner();
}
