package com.hkm.slider.SliderTypes;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.hkm.slider.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by hesk on 15/12/15.
 */
public class ZoomableView extends BaseSliderView {
    private final Activity activity;
    public final static String LOG_TAG = "ZoomableViewpp";
    private boolean
            animateCloseButton = false,
            bottomFadeDescription = true;
    private float initial_zoom_factor = 1.0f;
    protected Typeface typeface;
    private int corner_button_image;
    private Drawable corner_button_image_d;
    private cornerbuttonOnClick mclick = new cornerbuttonOnClick() {
        @Override
        public void click(int button_view_id) {

        }
    };
    private ZoomCallBack mzoom = new ZoomCallBack() {
        @Override
        public void cover(boolean isOn) {

        }
    };

    /**
     * the work for the button click listener
     */
    public interface cornerbuttonOnClick {
        void click(int button_view_id);
    }

    public interface ZoomCallBack {
        void cover(boolean isOn);
    }

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

    public ZoomableView setZoomCallBack(final ZoomCallBack zcb) {
        mzoom = zcb;
        return this;
    }

    public ZoomableView setCornerButtonImageRes(final @DrawableRes int drawable) {
        corner_button_image = drawable;
        return this;
    }

    public ZoomableView setCornerButtonImageDrawable(final Drawable drawable) {
        corner_button_image_d = drawable;
        return this;
    }

    public ZoomableView setCornerButtonClickListener(final cornerbuttonOnClick cb) {
        mclick = cb;
        return this;
    }


    public ZoomableView setButtomDescription(boolean enableDesc) {
        bottomFadeDescription = enableDesc;
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
        final ImageButton cornerbutton = (ImageButton) viewLayout.findViewById(R.id.ssz_frame_close_window_button);
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
            if (bottomFadeDescription) {
                cover.setLayoutTransition(transitioner);
            } else {
                cover.setVisibility(View.GONE);
            }
        }

        if (corner_button_image_d != null) {
            cornerbutton.setImageDrawable(corner_button_image_d);
            cornerbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mclick.click(v.getId());
                }
            });
        } else if (corner_button_image > 0) {
            cornerbutton.setImageResource(corner_button_image);
            cornerbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mclick.click(v.getId());
                }
            });
        } else {
            cornerbutton.setVisibility(View.GONE);
        }

        Log.d(LOG_TAG, "load image with url : " + getUrl() + " title:" + getDescription());
        Picasso.with(mContext).load(getUrl()).into(mImage, new Callback() {
            @Override
            public void onSuccess() {

                mAttacher.setOnMatrixChangeListener(new MatrixChangeListener(mAttacher, cover, cornerbutton));

                mAttacher.setOnPhotoTapListener(new PhotoTapListener());
                circle.setVisibility(View.GONE);
                mImage.post(new Runnable() {
                    @Override
                    public void run() {
                        mAttacher.setScale(
                                initial_zoom_factor,
                                true);
                    }
                });
                //slidrInf.unlock();
                //mImage.getImmImage.getWidth()
                //mAttacher.setScale(initial_zoom_factor);
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


    public class PhotoTapListener implements OnPhotoTapListener {
        @Override
        public void onPhotoTap(ImageView imageView, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;
            //    Tool.trace(zoomimage.this,  String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    public class MatrixChangeListener implements OnMatrixChangedListener {
        private final PhotoViewAttacher mAttacher;
        private final LinearLayout cover;
        private final ImageButton button;


        public MatrixChangeListener(PhotoViewAttacher mAttacher, LinearLayout cover, final ImageButton button_close) {
            this.mAttacher = mAttacher;
            button = button_close;
            this.cover = cover;
        }


        @Override
        public void onMatrixChanged(RectF rect) {
            try {
                if (mAttacher.getScale() > 1.5f) {
                    cover_on(cover, button);
                    mzoom.cover(true);
                } else {
                    cover_off(cover, button);
                    mzoom.cover(false);
                }


            } catch (Exception e) {
                Log.d(LOG_TAG, "onMatrix Changed" + e.getMessage());
            }
        }
    }


    private void cover_off(LinearLayout cover, final ImageButton cornerButton) {
        if (bottomFadeDescription && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            cover.animate().alpha(0f);

        if (animateCloseButton && cornerButton.getVisibility() != View.GONE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            cornerButton.animate().alpha(0f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    cornerButton.setEnabled(false);
                }
            });
    }

    private void cover_on(LinearLayout cover, final ImageButton cornerButton) {
        if (bottomFadeDescription && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            cover.animate().alpha(1f);

        if (animateCloseButton && cornerButton.getVisibility() != View.GONE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            cornerButton.animate().alpha(1f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    cornerButton.setEnabled(true);
                }
            });

    }

}
