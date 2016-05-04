package com.hkm.slider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.hkm.slider.SliderTypes.AdvancedTextSliderView;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.CompactFrameSliderView;
import com.hkm.slider.SliderTypes.CompactSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by hesk on 25/11/15.
 * Loyal Helper for making things cleaner and better
 */
public class LoyalUtil {

    public static Uri getUri(BaseSliderView coreSlider) {
        if (coreSlider instanceof CompactFrameSliderView) {
            CompactFrameSliderView t = (CompactFrameSliderView) coreSlider;
            return t.getCurrentClickUri();
        } else if (coreSlider instanceof CompactSliderView) {
            CompactSliderView d = (CompactSliderView) coreSlider;
            return d.getCurrentClickUri();
        } else if (coreSlider instanceof AdvancedTextSliderView) {
            AdvancedTextSliderView d = (AdvancedTextSliderView) coreSlider;
            return d.getTouchURI();
        } else {
            return null;
        }
    }


    public static void glideImplementation(String i, final ImageView target, Context context) {
        if (i.contains(".gif")) {
            Glide.with(context).load(i).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
        } else {
            Glide.with(context).load(i)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target)
            ;
        }
    }

    public static void glideImplementation(String i, final ImageView target, Context context, final Runnable run) {
        if (i.contains(".gif")) {
            Glide.with(context).load(i).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new ImageViewTarget<GifDrawable>(target) {
                        @Override
                        protected void setResource(GifDrawable resource) {
                            target.setImageDrawable(resource);
                            run.run();
                        }
                    });
        } else {
            Glide.with(context).load(i)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new GlideDrawableImageViewTarget(target) {
                        @Override
                        protected void setResource(GlideDrawable resource) {
                            super.setResource(resource);
                            run.run();
                        }
                    });
            ;
        }
    }


    public static void picassoImplementation(String u, final ImageView target, Context context, final Runnable callback) {
        Picasso.with(context)
                .load(u)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(target, new Callback() {
                    @Override
                    public void onSuccess() {
                        callback.run();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public static void picassoImplementation(String u, ImageView target, Context context) {
        Picasso.with(context)
                .load(u)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(target);
    }


    public static void hybridImplementation(String u, final ImageView target, Context context, final Runnable callback) {
        if (u.contains(".gif")) {
            Glide.with(context).load(u).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new ImageViewTarget<GifDrawable>(target) {
                        @Override
                        protected void setResource(GifDrawable resource) {
                            target.setImageDrawable(resource);
                            callback.run();
                        }
                    });
        } else {
            picassoImplementation(u, target, context, callback);
        }
    }


    public static void hybridImplementation(String u, ImageView target, Context context) {
        if (u.contains(".gif")) {
            Glide.with(context).load(u).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
        } else {
            picassoImplementation(u, target, context);
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
