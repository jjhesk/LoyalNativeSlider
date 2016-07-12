package com.hkm.loyalns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hkm.loyalns.demos.BigScreenDL;
import com.hkm.loyalns.demos.BigScreenDemo;
import com.hkm.loyalns.demos.DelayInitalzation;
import com.hkm.loyalns.demos.ExampleClassic;
import com.hkm.loyalns.demos.MultSections;
import com.hkm.loyalns.demos.NewsArticle;
import com.hkm.loyalns.demos.SliderAdjust1;
import com.hkm.loyalns.demos.ZoomScreenSlider;
import com.hkm.loyalns.demos.ZoomView_Three;
import com.hkm.loyalns.demos.ZoomView_Single;
import com.hkm.loyalns.demos.ZoomViewPager2;
import com.hkm.loyalns.demos.SliderAdjust2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hesk on 7/3/16.
 */
public class TheMain extends AppCompatActivity implements AdapterView.OnItemClickListener {
    LinkedHashMap<String, Class> data = new LinkedHashMap<>();
    ListView mList;
    ArrayList<Class> o = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        mList = (ListView) findViewById(android.R.id.list);
        ArrayList<String> items = new ArrayList<>();
        initList();
        for (Map.Entry<String, Class> entry : data.entrySet()) {
            String key = entry.getKey();
            items.add(key);
            o.add(entry.getValue());
        }
        mList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        mList.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, o.get(position));
        startActivity(intent);
    }

    Intent intent;

    private void gotoWeb() {
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urlgithub)));
        startActivity(intent);
    }


    public void initList() {

        data.put("Full screen", BigScreenDemo.class);
        data.put("Zoom in&out", ZoomScreenSlider.class);
        data.put("News Pager", NewsArticle.class);
        data.put("MultiSection - allow multiple mini sliders to fit into a single large slide. The max slides is suggested to 4", MultSections.class);
        data.put("Classic Example - with content height adjustment", ExampleClassic.class);
        data.put("Zoom View Pager Local Files", ZoomViewPager2.class);
        data.put("Zoom View Pager Load URL Single", ZoomView_Three.class);
        data.put("Zoom View Pager With Single Page", ZoomView_Single.class);
        data.put("Single Slide - debug on single slide with the arrows disappearing on the screen", BigScreenDL.class);
        data.put("Adjustable Slide View fit on screen demo", SliderAdjust1.class);
        data.put("Adjustable Slide + Real world application reading app demo", SliderAdjust2.class);
        data.put("debug time delay initialization", DelayInitalzation.class);

    }

}
