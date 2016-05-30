package com.hkm.loyalns.Util;


import com.hkm.loyalns.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

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
        file_maps.put("Choro Q N64", "file:///android_asset/q65.jpg");
        file_maps.put("Choro 4 HQ PS2", "file:///android_asset/q66.jpg");
        file_maps.put("Choro Rainbow Wings", "file:///android_asset/q67.jpg");
        file_maps.put("Choro Q Boat Race", "file:///android_asset/q68.jpg");
        return file_maps;

    }

    public static HashMap<String, String> getSingle() {
        HashMap<String, String> file_maps = new HashMap<String, String>();
        file_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        return file_maps;

    }

    public static Map.Entry<String, String> getRandomSingle() {
        int total = getVerticalDataSrc().size();
        Random n = new Random();
        int out = n.nextInt(total);
        LinkedHashMap<String, String> f = new LinkedHashMap<>(getVerticalDataSrc());
        final Map.Entry<String, String>[] test = new Map.Entry[total];
        f.entrySet().toArray(test);
        return test[out];
    }

}
