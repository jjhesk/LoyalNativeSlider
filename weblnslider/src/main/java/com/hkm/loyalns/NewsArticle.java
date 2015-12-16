package com.hkm.loyalns;

import com.hkm.loyalns.modules.Feed;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hypebeast.sdk.api.model.hbeditorial.ArticleData;
import com.hypebeast.sdk.api.model.hbeditorial.ResponsePostW;
import com.hypebeast.sdk.clients.HBEditorialClient;

import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hesk on 15/12/15.
 * Test of API on the news feed
 */
public class NewsArticle extends BigScreenDemo {
    protected HBEditorialClient clientApi;

    protected void defaultCompleteSlider(SliderLayout slide, List<ArticleData> list) {
        Iterator<ArticleData> it = list.iterator();
        while (it.hasNext()) {
            ArticleData article = it.next();
            Feed textSliderView = new Feed(this);

            textSliderView
                    //feed
                    .setID(article.getId())
                    .setPath(article._links.getSelf());

            //add your extra information
            slide.addSlider(textSliderView);

        }
    }

    protected Callback<ResponsePostW> res = new Callback<ResponsePostW>() {
        @Override
        public void success(ResponsePostW responsePostW, Response response) {
            try {
                defaultCompleteSlider(mDemoSlider, responsePostW.postList.getArticles());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };

    @Override
    protected boolean shouldRequestAPIBeforeLayoutRender() {
        clientApi = HBEditorialClient.getInstance(this);
        try {
            clientApi.createFeedInterface().the_recent_page(1, res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
