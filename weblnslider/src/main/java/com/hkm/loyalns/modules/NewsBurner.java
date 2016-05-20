package com.hkm.loyalns.modules;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hkm.ezwebview.Util.Fx9C;
import com.hkm.ezwebview.webviewleakfix.NonLeakingWebView;
import com.hkm.loyalns.R;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.NewsFeedArticleView;


import java.io.File;

/**
 * Created by hesk on 15/12/15.
 */
public class NewsBurner extends NewsFeedArticleView {
    protected NonLeakingWebView block;
   // protected HBEditorialClient clientApi;
    protected RelativeLayout rlout;
    protected long article_id;
    protected String full_path;

    public NewsBurner(Context context) {
        super(context);
      //  clientApi = HBEditorialClient.getInstance(context);
    }

    @Deprecated
    @Override
    public BaseSliderView image(@DrawableRes int res) {
        return super.image(res);
    }

    @Deprecated
    @Override
    public BaseSliderView description(String description) {
        return super.description(description);
    }

    @Deprecated
    @Override
    public BaseSliderView setUri(Uri info) {
        return super.setUri(info);
    }

    @Deprecated
    @Override
    public BaseSliderView image(String url) {
        return super.image(url);
    }

    @Deprecated
    @Override
    public BaseSliderView image(File file) {
        return super.image(file);
    }

    @Override
    public Uri getTouchURI() {
        return super.getTouchURI();
    }

    @Deprecated
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Deprecated
    @Override
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        return super.setOnSliderClickListener(l);
    }

    @Deprecated
    @Override
    public BaseSliderView enableSaveImageByLongClick(FragmentManager fmg) {
        return super.enableSaveImageByLongClick(fmg);
    }

    @Deprecated
    @Override
    public BaseSliderView enableImageLocalStorage() {
        return super.enableImageLocalStorage();
    }

    @Override
    protected int getLayoutHolder() {
        return R.layout.news_feed_item;
    }

    @Override
    protected void filter_apply_event_to_view(View root_view) {
        rlout = (RelativeLayout) root_view.findViewById(R.id.ns_display_number_container);
        block = (NonLeakingWebView) root_view.findViewById(R.id.wv_content_block);
        progressBar_circle = (ProgressBar) root_view.findViewById(R.id.ns_loading_progress);
        triggerLoading();
    }

    public NewsBurner setID(final long _id) {
        this.article_id = _id;
        return this;
    }

    public NewsBurner setPath(final String full_path) {
        this.full_path = full_path;
        return this;
    }

    protected void triggerLoading() {
        try {
            if (full_path != null) {
              //  clientApi.createAPIUniversal(full_path).getSingleArticle(res);
            } else if (article_id > 0) {
             //   clientApi.createFeedInterface().the_post(article_id, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void triggerLoading_althernative() {
     //   try {
            if (article_id > 0) {
              //  clientApi.createFeedInterface().the_post(article_id, res);
            } else if (full_path != null) {
              //  clientApi.createAPIUniversal(full_path).getSingleArticle(res);
            }
      //  } catch (ApiException e1) {
      //      e1.printStackTrace();
     //   }
    }

    protected void removeLoader() {
        if (progressBar_circle != null) {
            progressBar_circle.animate().alpha(0.0f);
        }
    }
/*
    protected Callback<ResponseSingle> res = new Callback<ResponseSingle>() {
        @Override
        public void success(ResponseSingle responsePostW, Response response) {
            try {
                Fx9C.setup_content_block_wb(mContext,
                        rlout,
                        block,
                        responsePostW.post.single_article_content,
                        new Runnable() {
                            @Override
                            public void run() {
                                removeLoader();
                            }
                        }
                );
            } catch (Exception e) {

            }
        }

        @Override
        public void failure(RetrofitError error) {
            triggerLoading_althernative();
        }
    };*/

}
