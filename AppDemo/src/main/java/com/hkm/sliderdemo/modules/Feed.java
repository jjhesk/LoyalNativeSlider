package com.hkm.sliderdemo.modules;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hkm.ezwebview.Util.Fx9C;
import com.hkm.ezwebview.webviewleakfix.NonLeakingWebView;
import com.hkm.slider.SliderTypes.NewsFeedArticleView;
import com.hkm.sliderdemo.R;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostW;
import com.hypebeast.sdk.api.model.hbeditorial.ResponseSingle;
import com.hypebeast.sdk.clients.HBEditorialClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hesk on 15/12/15.
 */
public class Feed extends NewsFeedArticleView {
    protected NonLeakingWebView block;
    protected HBEditorialClient clientApi;
    protected RelativeLayout rlout;

    protected Feed(Context context) {
        super(context);
        clientApi = HBEditorialClient.getInstance(context);
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

    protected void triggerLoading() {
        try {
            clientApi.createAPIUniversal(getUrl()).getSingleArticle(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removeLoader() {
        if (progressBar_circle != null) {
            progressBar_circle.animate().alpha(0.0f);
        }
    }

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
                e.printStackTrace();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };

}
