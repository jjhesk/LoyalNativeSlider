package com.hkm.slider.TouchView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapToGlideDrawableTranscoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hkm.slider.R;


public class UrlTouchImageView extends RelativeLayout {
    protected ProgressBar mProgressBar;
    protected TouchImageView mImageView;

    protected Context mContext;
    private int image_view_width, image_view_height;
    private String mImageUrl;
    protected GenericRequestBuilder generator;

    public UrlTouchImageView(Context ctx) {
        super(ctx);
        mContext = ctx;
        init();

    }

    public UrlTouchImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }

    public TouchImageView getImageView() {
        return mImageView;
    }

    @SuppressWarnings("deprecation")
    protected void init() {
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        ViewTreeObserver vto = mImageView.getViewTreeObserver();
        /**
         * Callback method to be invoked when the global layout state or the visibility of views
         * within the view tree changes
         */
        glide3();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                image_view_height = mImageView.getMeasuredHeight();
                image_view_width = mImageView.getMeasuredWidth();
                // mImageView.setText("Height: " + finalHeight + " Width: " + finalWidth);
                initLoadingRequest();
                // mImageView.setVisibility(GONE);
                return false;
            }
        });
        this.addView(mImageView);
        //  mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar = new ProgressBar(mContext, null);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(30, 0, 30, 0);
        mProgressBar.setLayoutParams(params);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(100);
        this.addView(mProgressBar);
    }

    private void initLoadingRequest() {
        generator.load(mImageUrl).into(mImageView);
    }

    private void glide2() {
        //   DrawableTypeRequest<String> t = Glide.with(mContext);
        generator = Glide.with(mContext)
                .using(new GenerateParamsPassthroughModelLoader(), GenerateParams.class)          // custom class
                .from(GenerateParams.class)
                .as(Bitmap.class)
                .transcode(new BitmapToGlideDrawableTranscoder(mContext), GlideDrawable.class)     // builtin
                .decoder(new GenerateParamsBitmapResourceDecoder(mContext))                        // custom class
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 0/*ignored for lossless*/)) // builtin

                .cacheDecoder(new FileToStreamDecoder<Bitmap>(new StreamBitmapDecoder(mContext)))  // builtin
                        //.placeholder(new ColorDrawable(Color.YELLOW)) // you can pre-set placeholder and error
                .error(new ColorDrawable(Color.RED))            // so it's easier when binding
        //.diskCacheStrategy(DiskCacheStrategy.NONE)    // only for debugging to always regenerate
        //.skipMemoryCache(true)                        // only for debugging to always regenerate
        ;
    }


    private void glide3() {
        //   DrawableTypeRequest<String> t = Glide.with(mContext);
        generator = Glide.with(mContext)

                .fromString()
                .asBitmap()

                .cacheDecoder(new FileToStreamDecoder<Bitmap>(new StreamBitmapDecoder(mContext)))  // builtin
                        //.placeholder(new ColorDrawable(Color.YELLOW)) // you can pre-set placeholder and error
                        //  .error(new ColorDrawable(Color.RED))            // so it's easier when binding
                        //.error(R.drawable.shadow_hb_professional)
                .diskCacheStrategy(DiskCacheStrategy.ALL)    // only for debugging to always regenerate

                .skipMemoryCache(true)                        // only for debugging to always regenerate
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        mImageView.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mProgressBar.animate().alpha(0).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(GONE);
                                }
                            });
                        }
                        return false;
                    }
                })
        ;
    }


    public void setUrl(String imageUrl) {
        mImageUrl = imageUrl;
        //new ImageLoadTask().execute(imageUrl);
    }

    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

}
