package com.hkm.slider;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hkm.slider.SliderTypes.AdvancedTextSliderView;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.CompactFrameSliderView;
import com.hkm.slider.SliderTypes.CompactSliderView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

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


    public static void glideImplementation(String i, ImageView target, Context context) {
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

    public static void picassoImplementation(String u, ImageView target, Context context) {
        Picasso.with(context)
                .load(u)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(target);
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
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
