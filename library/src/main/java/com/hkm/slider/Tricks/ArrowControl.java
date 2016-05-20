package com.hkm.slider.Tricks;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by hesk on 15/4/16.
 * This is the smart arrow controller
 */
public class ArrowControl {
    private ImageView left, right;


    private int mLWidthB, mRWidthB, count, current_position;
    boolean mLopen, mRopen, circular;

    public final void setCurrentPosition(final int p) {
        current_position = p;
    }

    public final void setTotal(final int total) {
        count = total;
    }

    public final void noSlideButtons() {
        left.setVisibility(View.GONE);
        right.setVisibility(View.GONE);
        mLopen = false;
        mRopen = false;
    }

    public final void IsCirularView(boolean b) {
        circular = b;
    }

    public final void notifyOnPageChanged() {
        if (count <= 1) {
            if (mLopen) {
                left.animate().translationX(-mLWidthB);
                mLopen = false;
            }
            if (mRopen) {
                right.animate().translationX(mRWidthB);
                mRopen = false;
            }
        } else {
            if (current_position == 0) {
                if (mLopen) {
                    left.animate().translationX(-mLWidthB);
                    mLopen = false;
                }
            } else if (current_position == count - 1) {
                if (mRopen) {
                    right.animate().translationX(mRWidthB);
                    mRopen = false;
                }
            } else {
                if (!mRopen) {
                    right.animate().translationX(0);
                    mRopen = true;
                }
                if (!mLopen) {
                    left.animate().translationX(0);
                    mLopen = true;
                }
            }
        }
    }


    public void setListeners(View.OnClickListener lefttrigger, View.OnClickListener righttrigger) {
        left.setOnClickListener(lefttrigger);
        right.setOnClickListener(righttrigger);

        mLopen = true;
        mRopen = true;
    }

    public ArrowControl(ImageView left_arrow, ImageView right_arrow) {
        left = left_arrow;
        right = right_arrow;

        mLopen = true;
        mRopen = true;
        mLWidthB = left.getDrawable().getIntrinsicWidth();
        mRWidthB = right.getDrawable().getIntrinsicWidth();

    }
}
