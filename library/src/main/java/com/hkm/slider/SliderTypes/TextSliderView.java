package com.hkm.slider.SliderTypes;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.slider.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends AdvancedTextSliderView<TextView, ImageView> {
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    protected int renderedLayoutTextBanner() {
        return R.layout.render_type_text;
    }
}
