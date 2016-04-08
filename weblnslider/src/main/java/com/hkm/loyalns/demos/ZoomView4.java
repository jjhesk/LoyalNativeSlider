package com.hkm.loyalns.demos;

import com.hkm.loyalns.mod.NsZoomable;
import com.hkm.slider.GalleryWidget.GalleryViewPager;
import com.hkm.slider.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hesk on 17/3/16.
 */
public class ZoomView4 extends NsZoomable {
    @Override
    protected void startViewPager(GalleryViewPager mViewpager) {
        String[] urls = {
                "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg",
        };
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setCurrentItem(3);
    }
}
