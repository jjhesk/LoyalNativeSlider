package com.hkm.slider.Indicators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by hesk on 6/28/2015.
 */
public class NumContainer<customText> implements IContainer {
    @Override
    public int getResLayout() {
        return 0;
    }

    @Override
    public int getTextField() {
        return 0;
    }

    @Override
    public int getAlignment() {
        return 0;
    }

    protected customText tv;
    protected int totalSlides, now;

    public NumContainer(Context c) {
        View v = LayoutInflater.from(c).inflate(getResLayout(), null, false);
        tv = (customText) v.findViewById(getTextField());
    }

    public void setTotal(final int k) {
        totalSlides = k;
    }

    public void setAtSlide(final int e) {
        if (e > totalSlides) {
            now = totalSlides;
        } else {
            now = e;
        }
    }
}
