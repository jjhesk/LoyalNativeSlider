package com.hkm.sliderdemo.modules;

import android.content.Context;
import android.widget.TextView;

import com.hkm.slider.Indicators.NumContainer;
import com.hkm.sliderdemo.R;

/**
 * Created by hesk on 7/7/15.
 */
public class NumZero extends NumContainer<TextView> {
    public NumZero(Context c) {
        super(c, R.layout.numfield);
    }
}
