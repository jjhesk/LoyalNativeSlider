package com.hkm.slider.SliderTypes;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hkm.slider.R;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hesk on 15/12/15.
 */
public class ZoomableView extends BaseSliderView {
    private final Activity activity;
    public final static String LOG_TAG = "ZoomableViewpp";
    private boolean animateCloseButton = false;
    private float initial_zoom_factor = 1.0f;
    protected Typeface typeface;

    public ZoomableView(Activity context) {
        super(context);
        activity = context;

    }

    public ZoomableView(boolean closeButtonAnimation, Activity context) {
        this(context);
        animateCloseButton = closeButtonAnimation;
    }

    public ZoomableView setInitalZoomFactor(float a) {
        initial_zoom_factor = a;
        return this;
    }

    public ZoomableView setDisplayFontName(String name) {
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + name);
        return this;
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return View
     */
    @Override
    public View getView() {
        View layoutview = LayoutInflater.from(mContext).inflate(getLayoutHolder(), null);
        filter_apply_event_to_view(layoutview);
        return layoutview;
    }

    protected void filter_apply_event_to_view(View viewLayout) {
        final LinearLayout cover = (LinearLayout) viewLayout.findViewById(R.id.ssz_bottom_caption);
        final ImageButton close = (ImageButton) viewLayout.findViewById(R.id.ssz_frame_close_window_button);
        final ProgressBar circle = (ProgressBar) viewLayout.findViewById(R.id.ns_loading_progress);
        final PhotoView mImage = (PhotoView) viewLayout.findViewById(R.id.ssz_uk_co_senab_photoview);
        final TextView mCurrMatrixTv = (TextView) viewLayout.findViewById(R.id.ssz_debug_textview);
        setDebugTextAdvance(mCurrMatrixTv, this);
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(mImage);

        final TextView mCaptv = (TextView) viewLayout.findViewById(R.id.ssz_caption_textview);
        setCaptionTextviewAdvance(mCaptv, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            LayoutTransition transitioner = new LayoutTransition();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                transitioner.enableTransitionType(LayoutTransition.CHANGING);
            }
            cover.setLayoutTransition(transitioner);
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  finish();
            }
        });


        Log.d(LOG_TAG, "load image with url : " + getUrl() + " title:" + getDescription());
        Picasso.with(mContext).load(getUrl()).into(mImage, new Callback() {
            @Override
            public void onSuccess() {
                mAttacher.setOnMatrixChangeListener(new MatrixChangeListener(mAttacher, cover, close));
                mAttacher.setOnPhotoTapListener(new PhotoTapListener());
                circle.setVisibility(View.GONE);
                mImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mAttacher.setScale(2f, mImage.getWidth() / 2, mImage.getHeight() / 2, true);
                    }
                });

                //slidrInf.unlock();
                mAttacher.setScale(initial_zoom_factor);
            }

            @Override
            public void onError() {
                circle.setVisibility(View.GONE);
            }
        });
    }


    @LayoutRes
    protected int getLayoutHolder() {
        return R.layout.content_zoomable_slide;
    }

    protected void setCaptionTextviewAdvance(final TextView caption, final BaseSliderView display_text) {
        int color_i = ContextCompat.getColor(mContext, R.color.letterwhite);
        caption.setTextColor(color_i);
        caption.setText(display_text.getDescription());

        if (typeface != null) {
            caption.setTypeface(typeface);
        }
    }

    protected void setDebugTextAdvance(final TextView debugfield, final BaseSliderView display) {

    }


    public class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;
            //    Tool.trace(zoomimage.this,  String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    public class MatrixChangeListener implements PhotoViewAttacher.OnMatrixChangedListener {
        private final PhotoViewAttacher mAttacher;
        private final LinearLayout cover;
        private final ImageButton button;
        private SlidrInterface mSlidr;

        public MatrixChangeListener(PhotoViewAttacher mAttacher, LinearLayout cover, final ImageButton button_close) {
            this.mAttacher = mAttacher;
            button = button_close;
            this.cover = cover;
        }

        public MatrixChangeListener(SlidrInterface controller, PhotoViewAttacher mAttacher, LinearLayout cover, final ImageButton button_close) {
            this(mAttacher, cover, button_close);
            mSlidr = controller;
        }

        @Override
        public void onMatrixChanged(RectF rect) {
            try {
                if (mAttacher.getScale() > 1.5f) {
                    cover_on(cover, button);
                } else {
                    cover_off(cover, button);
                }

                if (mSlidr != null) {
                    if (mAttacher.getScale() == 1.0f)
                        mSlidr.unlock();
                    else
                        mSlidr.lock();
                }

            } catch (Exception e) {
                Log.d(LOG_TAG, "onMatrix Changed" + e.getMessage());
            }
        }
    }


    private void cover_off(LinearLayout cover, final ImageButton button_close) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            cover.animate().alpha(0f);
            if (animateCloseButton)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    button_close.animate().alpha(0f).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            button_close.setEnabled(false);
                        }
                    });
                }
        }

    }

    private void cover_on(LinearLayout cover, final ImageButton button_close) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            cover.animate().alpha(1f);
            if (animateCloseButton)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    button_close.animate().alpha(1f).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            button_close.setEnabled(true);
                        }
                    });
                }
        }

    }

}
