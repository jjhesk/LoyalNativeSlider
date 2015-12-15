package com.hkm.slider.SliderTypes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.hkm.slider.R;

/**
 * Created by hesk on 15/12/15.
 */
public abstract class NewsFeedArticleView extends BaseSliderView {
    protected Typeface typeface_1, typeface_2;
    protected ProgressBar progressBar_circle;

    protected NewsFeedArticleView(Context context) {
        super(context);
    }

    public NewsFeedArticleView setDisplayFontName_1(String name) {
        typeface_1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + name);
        return this;
    }

    public NewsFeedArticleView setDisplayFontName_2(String name) {
        typeface_2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + name);
        return this;
    }

    public NewsFeedArticleView set_a() {
        return this;
    }

    public NewsFeedArticleView set_b() {
        return this;
    }

    public NewsFeedArticleView set_c() {
        return this;
    }

    public NewsFeedArticleView set_d() {
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

    @LayoutRes
    protected abstract int getLayoutHolder();

    protected abstract void filter_apply_event_to_view(View root_view);
}
