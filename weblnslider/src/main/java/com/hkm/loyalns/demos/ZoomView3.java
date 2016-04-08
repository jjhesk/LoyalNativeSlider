package com.hkm.loyalns.demos;

import com.hkm.loyalns.mod.NsZoomable;
import com.hkm.slider.GalleryWidget.GalleryViewPager;
import com.hkm.slider.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hesk on 8/3/16.
 */
public class ZoomView3 extends NsZoomable {
    @Override
    protected void startViewPager(GalleryViewPager mViewpager) {
        String[] urls = {
                //   "http://pcdn.500px.net/35939982/127d53ceac436e2e17a11ea42bb2cd7719b9f1e1/4.jpg",
                "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg",
                //    special url with error
                "http://cs407831.userapi.com/v407831207/1906/oxoP6URjFtA.jpg",
                "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
                "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
                "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
                "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
                "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
                "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
                "http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg"
        };
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setCurrentItem(3);
    }
}
