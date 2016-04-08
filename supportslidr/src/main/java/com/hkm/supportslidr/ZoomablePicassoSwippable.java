package com.hkm.supportslidr;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hesk on 15/12/15.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ZoomablePicassoSwippable extends AppCompatActivity implements SlidrInterface {
    public static final String URLKEY = "image_url_full";
    public static final String TITLE = "title_bar_text";
    public static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
    private static final String LOG_TAG = "image-test";
    public static final String TAG = "ZOOMIMAGE";
    protected PhotoView mImage;
    protected Button mButton1;
    protected Button mButton2;
    protected ImageView close;
    protected CheckBox mCheckBox;
    protected static int displayTypeCount = 0;
    protected PhotoViewAttacher mAttacher;
    protected TextView mCurrMatrixTv;
    protected TextView mCaptv;
    protected SlidrInterface slidrInf;
    protected LinearLayout cover;
    protected boolean animateCloseButton = false;
    protected Picasso picasso;

    public static void startIntent(String image_single, String title, Context resContext) {
        final Bundle b = new Bundle();
        b.putString(URLKEY, image_single);
        b.putString(TITLE, title);
        final Intent i = new Intent(resContext, ZoomablePicassoSwippable.class);
        i.putExtras(b);
        resContext.startActivity(i);
    }

    protected SlidrConfig developSliderConfiguration() {
        int color_p = ContextCompat.getColor(this, R.color.prim_color);
        int color_s = ContextCompat.getColor(this, R.color.second_color);

        final SlidrConfig config_slider = new SlidrConfig.Builder()
                .primaryColor(color_p)
                .secondaryColor(color_s)
                .position(SlidrPosition.TOP)
                .sensitivity(0.4f)
                .build();

        return config_slider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LayoutTransition transitioner;

        setContentView(R.layout.content_zoomable_slide);
        cover = (LinearLayout) findViewById(R.id.ssz_bottom_caption);
        close = (ImageView) findViewById(R.id.ssz_frame_close_window_button);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            transitioner = new LayoutTransition();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                transitioner.enableTransitionType(LayoutTransition.CHANGING);
            }
            cover.setLayoutTransition(transitioner);
        }

        slidrInf = Slidr.attach(this, developSliderConfiguration());
        slidrInf.unlock();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {
            picasso = Picasso.with(this);
            Bundle b = getIntent().getExtras();
            onSetLayout(b.getString(URLKEY), b.getString(TITLE));
        }
    }

    private void cover_off() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            cover.animate().alpha(0f);
            if (animateCloseButton)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    close.animate().alpha(0f).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            close.setEnabled(false);
                        }
                    });
                }
        }

    }

    private void cover_on() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            cover.animate().alpha(1f);
            if (animateCloseButton)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    close.animate().alpha(1f).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            close.setEnabled(true);
                        }
                    });
                }
        }

    }

    protected void setCaptionTextviewAdvance(final TextView caption, final String display_text) {
        int color_i = ContextCompat.getColor(this, R.color.letterwhite);
        caption.setTextColor(color_i);
        caption.setText(display_text);
    }

    protected void onSetLayout(final String image_url, final String cctv) {
        mImage = (PhotoView) findViewById(R.id.ssz_uk_co_senab_photoview);
        mCurrMatrixTv = (TextView) findViewById(R.id.ssz_debug_textview);
        // mCurrMatrixTv.setText(cctv);
        mCaptv = (TextView) findViewById(R.id.ssz_caption_textview);
        setCaptionTextviewAdvance(mCaptv, cctv);


        final ProgressBar circle = (ProgressBar) findViewById(R.id.ns_loading_progress);
        Log.d(LOG_TAG, "load image with url : " + image_url + " title:" + cctv);
        picasso.load(image_url).into(mImage, new Callback() {
            @Override
            public void onSuccess() {
                mAttacher = new PhotoViewAttacher(mImage);
                mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
                mAttacher.setOnPhotoTapListener(new PhotoTapListener());
                circle.setVisibility(View.GONE);
                mImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mAttacher.setScale(2f, mImage.getWidth() / 2, mImage.getHeight() / 2, true);
                    }
                });

                //slidrInf.unlock();
                mAttacher.setScale(1.5f);

            }

            @Override
            public void onError() {
                circle.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void lock() {
        Log.d(TAG, "lock in here");
    }

    @Override
    public void unlock() {
        Log.d(TAG, "unlock in here");
    }

    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;
            //    Tool.trace(zoomimage.this,  String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    private class MatrixChangeListener implements PhotoViewAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
            try {
                if (mAttacher.getScale() > 1.5f) cover_on();
                else cover_off();

                if (mAttacher.getScale() == 1.0f) slidrInf.unlock();
                else slidrInf.lock();

            } catch (Exception e) {
                Log.d(TAG, "onMatrix Changed" + e.getMessage());
            }
        }
    }
}
