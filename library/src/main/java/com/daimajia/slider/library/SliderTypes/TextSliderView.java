package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends AdvancedTextSliderView {
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    protected int renderedLayoutTextBanner() {
        return R.layout.render_type_text;
    }
}
