package com.hkm.sliderdemo.Util;

import com.hkm.sliderdemo.R;

import java.util.HashMap;

/**
 * Created by hesk on 19/8/15.
 */
public class DataProvider {
    public static HashMap<String, Integer> getFileSrcHorizontal() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.hannibal);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);
        return file_maps;
    }

    public static HashMap<String, String> getDataUrlSource() {
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        return url_maps;
    }

    public static HashMap<String, String> getVerticalDataSrc() {

        HashMap<String, String> file_maps = new HashMap<String, String>();
        file_maps.put("Hannibal", "file:///android_asset/g1.jpg");
        file_maps.put("summer girl", "file:///android_asset/g2.jpg");
        file_maps.put("fe", "file:///android_asset/g3.jpg");
        file_maps.put("long leg", "file:///android_asset/g4.jpg");
        file_maps.put("high heels", "file:///android_asset/g5.jpg");
        file_maps.put("sf", "file:///android_asset/g6.jpg");
        file_maps.put("sfaf", "file:///android_asset/g7.jpg");
        file_maps.put("sfaf", "file:///android_asset/g8.jpg");
        file_maps.put("sfaf", "file:///android_asset/g9.jpg");
        return file_maps;

    }
}
