package com.hkm.slider.SliderTypes;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hkm.slider.Layouts.MaterialRippleLayout;
import com.hkm.slider.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hesk on 24/11/15.
 */
public class CompactFrameSliderView extends CompactSliderView {
    @LayoutRes
    protected int getCompactFrameLayout() {
        return DEFAULT_SLIDE;
    }

    public class FrameImage {
        private ImageView mImage;
        private ProgressBar mProgress;
        public TextView mTextView;
        private LinearLayout ly;
        private MaterialRippleLayout touch;
        private View full_view;

        public FrameImage(View frame) {
            mImage = (ImageView) frame.findViewById(R.id.ns_slider_image);
            mProgress = (ProgressBar) frame.findViewById(R.id.ns_loading_progress);
            mTextView = (TextView) frame.findViewById(R.id.ns_slider_desc);
            ly = (LinearLayout) frame.findViewById(R.id.ns_desc_frame);
            if (frame.findViewById(R.id.ns_touch_frame) != null) {
                touch = (MaterialRippleLayout) frame.findViewById(R.id.ns_touch_frame);
            }
            full_view = frame;
        }

        public View getView() {
            return full_view;
        }

        public View getTouch() {
            if (touch == null) return mImage;
            return touch;
        }

        public void setClickListener(final View.OnClickListener listner) {
            if (touch == null) {
                mImage.setOnClickListener(listner);
            } else {
                touch.setOnClickListener(listner);
            }
        }

        public ImageView getImageTarget() {
            return mImage;
        }

        public ProgressBar getLoadingBar() {
            return mProgress;
        }

        public void applyDescription(String word) {
            if (mTextView == null) return;
            mTextView.setText(word);
        }
    }

    public static final int DEFAULT_SLIDE = R.layout.hb_feature_slide;
    public static final int RIPPLE_SLIDE = R.layout.hb_feature_slide_ripple;
    private int setCustomLayoutSlide = 0;
    private List<String> descriptions = new ArrayList<>();

    @LayoutRes
    protected int getLayoutConfig() {
        switch (number_of_pieces) {
            case 2:
                return R.layout.compact_frame_2;
            case 3:
                return R.layout.compact_frame_3;
            case 4:
                return R.layout.compact_frame_4;
            default:
                return R.layout.compact_frame_2;
        }
    }

    public CompactFrameSliderView setSlideLayoutCustom(@LayoutRes int layoutId) {
        setCustomLayoutSlide = layoutId;
        return this;
    }

    public CompactFrameSliderView setDescriptions(final String[] descL) throws Exception {
        if (descL.length < number_of_pieces) {
            throw new Exception("not enough descriptions. There in total we will need to have " + number_of_pieces + " of them");
        }
        this.descriptions = Arrays.asList(descL);
        return this;
    }

    public CompactFrameSliderView setDescriptions(final ArrayList<String> descriptions) throws Exception {
        if (descriptions.size() < number_of_pieces) {
            throw new Exception("not enough descriptions. There in total we will need to have " + number_of_pieces + " of them");
        }

        this.descriptions = descriptions;
        return this;
    }

    /**
     * the description of a slider image. Do not use this
     *
     * @param description String
     * @return BaseSliderView
     */
    @Deprecated
    @Override
    public CompactFrameSliderView description(String description) {
        return this;
    }

    public CompactFrameSliderView(Context context, int pieces) throws Exception {
        super(context, pieces);
    }

    protected FrameLayout f1, f2, f3, f4;

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return View
     */
    @Override
    public int bindviews(View layout) {
        if (number_of_pieces >= 1) {
            f1 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s1);
        }
        if (number_of_pieces >= 2) {
            f2 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s2);
        }
        if (number_of_pieces >= 3) {
            f3 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s3);
        }
        if (number_of_pieces >= 4) {
            f4 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s4);
        }
        return number_of_pieces;
    }

    @Override
    protected void filter_apply_event_to_view(int f) {
        if (f == 0) {
            apply_event_to_frame(f1, urls.get(f), f);
        } else if (f == 1) {
            apply_event_to_frame(f2, urls.get(f), f);
        } else if (f == 2) {
            apply_event_to_frame(f3, urls.get(f), f);
        } else if (f == 3) {
            apply_event_to_frame(f4, urls.get(f), f);
        }
    }


    private void apply_event_to_frame(
            @Nullable FrameLayout mframeLayout,
            @NonNull final String image_url,
            final int n
    ) {
        if (mframeLayout == null) return;
        final int layout_id = setCustomLayoutSlide == 0 ? getCompactFrameLayout() : setCustomLayoutSlide;
        final View layoutview = LayoutInflater.from(mContext).inflate(layout_id, null);
        final FrameImage frame = new FrameImage(layoutview);
        mframeLayout.addView(frame.getView());
        if (descriptions.size() > 0) {
            String desc = descriptions.get(n);
            frame.applyDescription(desc);
        }
        bindCompatPicasso(image_url, frame);

        final CompactFrameSliderView me = this;
        frame.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null && uris.size() > 0) {
                    link_on_click_current = uris.get(n);
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });
    }

    protected void bindCompatPicasso(String mURI, final FrameImage Fr) {

        final Picasso p = Picasso.with(mContext);
        final RequestCreator mreq = p.load(mURI);
        if (getEmpty() != 0) {
            mreq.placeholder(getEmpty());
        }
        if (getError() != 0) {
            mreq.error(getError());
        }
        if (mImageLocalStorageEnable) {
            mreq.memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE);
        }
        switch (mScaleType) {
            case Fit:
                mreq.fit();
                break;
            case CenterCrop:
                mreq.fit().centerCrop();
                break;
            case CenterInside:
                mreq.fit().centerInside();
                break;
        }


        mreq.into(Fr.getImageTarget(), new Callback() {
            @Override
            public void onSuccess() {
                hideoutView(Fr.getLoadingBar());
                if (mLongClickSaveImage && fmg != null) {
                    Fr.getTouch().setOnLongClickListener(new View.OnLongClickListener() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public boolean onLongClick(View v) {
                            prepare_request_save_image = mreq;
                            final saveImageDialog saveImageDial = new saveImageDialog();
                            saveImageDial.show(fmg, "DESC_SAVE_IM");
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onError() {
                //if (mLoadListener != null) {
                //     mLoadListener.onEnd(false, me);
                // }
            }
        });
    }

    @Override
    public CompactFrameSliderView build() {
        return this;
    }
}
