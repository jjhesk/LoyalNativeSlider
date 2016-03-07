package com.hkm.slider.Layouts;

import android.support.annotation.DrawableRes;

/**
 * Created by hesk on 7/3/16.
 */
public class ArrowDecor extends Decor {
    @Override
    public void create() {

    }

    public static class Builder {
        @DrawableRes
        private int right, left;
        private boolean autoHide = false;
        private boolean enableSingleItemHide = true;


        public Builder hideOnSingleItem(boolean enableSingleItemHide) {
            this.enableSingleItemHide = enableSingleItemHide;
            return this;
        }


        public Builder setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }

        public Builder setLeftResId(final @DrawableRes int left) {
            this.left = left;
            return this;
        }

        public Builder setRightResId(final @DrawableRes int right) {
            this.right = right;
            return this;
        }
    }
}
