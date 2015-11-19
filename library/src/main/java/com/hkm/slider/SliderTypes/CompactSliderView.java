package com.hkm.slider.SliderTypes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hkm.slider.R;

/**
 * Created by hesk on 19/11/15.
 */
public class CompactSliderView extends BaseSliderView {

    protected int number_of_pieces;
    protected ImageView
            s1, s2, s3, s4;

    protected CompactSliderView(Context context, final int pieces) throws Exception {
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

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return View
     */
    @Override
    public View getView() {
        View layoutview = LayoutInflater.from(getContext()).inflate(R.layout.render_type_default, null);
        s1 = (ImageView) layoutview.findViewById(R.id.ns_display_number_s1);
        s2 = (ImageView) layoutview.findViewById(R.id.ns_display_number_s2);
        if (number_of_pieces >= 3) {
            s3 = (ImageView) layoutview.findViewById(R.id.ns_display_number_s3);
        }
        if (number_of_pieces >= 4) {
            s4 = (ImageView) layoutview.findViewById(R.id.ns_display_number_s4);
        }

        return layoutview;
    }

    public CompactSliderView setDisplayOnlyImageUrls(String[] urls) throws Exception {
        if (urls.length < number_of_pieces) {
            throw new Exception("Do not have enough urls for fetching the images into the holder");
        }

        return this;
    }

    public void build() {
        bindEventAndShow(s1, s1);
        bindEventAndShow(s2, s2);
        if (number_of_pieces >= 3) {
            bindEventAndShow(s3, s3);
        }
        if (number_of_pieces >= 4) {
            bindEventAndShow(s4, s4);
        }
    }
}
