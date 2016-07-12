package com.hkm.slider.SliderTypes;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hkm.slider.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the Sample Compact Slider View with Image display only
 * this is the demonstration of a simple way to implementat a compact slider view
 */
public class CompactSliderView extends BaseSliderView {
    protected boolean loadingProgress = false;
    protected int number_of_pieces;
    protected ImageView s1, s2, s3, s4;
    protected List<String> urls = new ArrayList<>();
    protected List<String> uris = new ArrayList<>();

    public CompactSliderView(Context context, final int pieces) throws Exception {
        super(context);
        number_of_pieces = pieces;
        if (number_of_pieces < 2 && number_of_pieces > 4) {
            throw new Exception("Do not support this request");
        }
    }

    @LayoutRes
    protected int getLayoutConfig() {
        switch (number_of_pieces) {
            case 2:
                return R.layout.compact_2_images;
            case 3:
                return R.layout.compact_3_images;
            case 4:
                return R.layout.compact_4_images;
            default:
                return R.layout.compact_2_images;
        }
    }

    protected ImageView getImView(int number_of_pieces) {
        switch (number_of_pieces) {
            case 0:
                return s1;
            case 1:
                return s2;
            case 2:
                return s3;
            case 3:
                return s4;
            default:
                return s4;
        }
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return View
     */
    @Override
    public View getView() {
        View layoutview = LayoutInflater.from(mContext).inflate(getLayoutConfig(), null);
        for (int f = 0; f < bindviews(layoutview); f++) {
            filter_apply_event_to_view(f);
        }
        return layoutview;
    }

    protected void filter_apply_event_to_view(final int nOrder) {
        bindEventAndShow(getImView(nOrder), urls.get(nOrder));
        final CompactSliderView me = this;
        final String h = uris.size() > 0 ? uris.get(nOrder) : null;
        getImView(nOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null && h != null) {
                    link_on_click_current = h;
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });
    }

    protected int bindviews(View layout) {
        int total_fields = 0;
        if (number_of_pieces >= 1) {
            s1 = (ImageView) layout.findViewById(R.id.ns_display_number_s1);
            total_fields = 1;
        }
        if (number_of_pieces >= 2) {
            s2 = (ImageView) layout.findViewById(R.id.ns_display_number_s2);
            total_fields = 2;
        }
        if (number_of_pieces >= 3) {
            s3 = (ImageView) layout.findViewById(R.id.ns_display_number_s3);
            total_fields = 3;
        }
        if (number_of_pieces >= 4) {
            s4 = (ImageView) layout.findViewById(R.id.ns_display_number_s4);
            total_fields = 4;
        }
        return total_fields;
    }

    public CompactSliderView setLinksOnEach(final ArrayList<String> urls_list) {
        uris = urls_list;
        return this;
    }

    public CompactSliderView setLinksOnEach(final String[] urls_list) {
        uris = Arrays.asList(urls_list);
        return this;
    }

    public CompactSliderView setDisplayOnlyImageUrls(final ArrayList<String> ArUrls) {
        if (ArUrls.size() < number_of_pieces) {
            number_of_pieces = ArUrls.size();
        }
        urls.clear();
        urls = ArUrls;
        return this;
    }

    public CompactSliderView setDisplayOnlyImageUrls(final String[] ArUrls) {
        if (ArUrls.length < number_of_pieces) {
            number_of_pieces = ArUrls.length;
        }
        urls.clear();
        urls = Arrays.asList(ArUrls);
        return this;
    }

    protected RequestCreator prepare_request_save_image;
    protected String link_on_click_current;

    public Uri getCurrentClickUri() {
        return Uri.parse(link_on_click_current);
    }

    private void bindEventAndShow(
            @NonNull final ImageView targetImageView,
            @NonNull final String mURI
    ) {

        //  mLoadListener.onStart(me);
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

        mreq.into(targetImageView, new Callback() {
            @Override
            public void onSuccess() {
                //  if (v.findViewById(R.id.ns_loading_progress) != null) {
                //    hideoutView(v.findViewById(R.id.ns_loading_progress));
                //  }

                if (mLongClickSaveImage && fmg != null) {
                    targetImageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public boolean onLongClick(View v) {
                            prepare_request_save_image = mreq;
                            final saveImageDialog saveImageDial = new saveImageDialog();
                            saveImageDial.show(fmg.get(), "DESC_SAVE_IM");
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

    public CompactSliderView build() {

        return this;
    }
}
