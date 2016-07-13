package com.hkm.slider.SliderTypes;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by zJJ on 7/13/2016.
 */
public interface MiniSliderFrame {
    View getView();

    View getTouch();

    void setClickListener(final View.OnClickListener listner);

    ImageView getImageTarget();

    ProgressBar getLoadingBar();

    /**
     * all bundled string can be applied in here
     * @param word input word
     */
    void applyDescription(String word);

    /**
     * all bundled string can be applied in here
     * @param wordlist input word
     */
    void applyDescription(String[] wordlist);
}
