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
import com.hkm.loyalns.demos.ExampleClassic;
import com.hkm.loyalns.demos.MultSections;
import com.hkm.loyalns.demos.NewsArticle;
import com.hkm.loyalns.demos.ZoomScreenSlider;
import com.hkm.loyalns.demos.ZoomView3;
import com.hkm.loyalns.demos.ZoomView4;
import com.hkm.loyalns.demos.ZoomViewPager2;

import java.util.ArrayList;

/**
 * Created by hesk on 7/3/16.
 */
public class TheMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        mList = (ListView) findViewById(android.R.id.list);
        ArrayList<String> items = new ArrayList<>();

        items.add("Full Screen Demp");
        //1
        items.add("Zoom in and out slider Example");
        items.add("News Article Example");
        //3
        items.add("MultiSection Example");
        //4
        items.add("Classic Example - with content height adjustment");
        items.add("Zoom View Pager Local Files");
        items.add("Zoom View Pager Load URL");
        //7
        items.add("Zoom View Pager With Single Page");
        items.add("Single Slide - debug on single slide with the arrows disappearing on the screen");
        //19
        items.add("Github repos");
        mList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        mList.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;

        if (position == 0) {
            intent = new Intent(this, BigScreenDemo.class);
        } else if (position == 1) {
            intent = new Intent(this, ZoomScreenSlider.class);
        } else if (position == 2) {
            intent = new Intent(this, NewsArticle.class);
        } else if (position == 3) {
            intent = new Intent(this, MultSections.class);
        } else if (position == 4) {
            intent = new Intent(this, ExampleClassic.class);
        } else if (position == 5) {
            intent = new Intent(this, ZoomViewPager2.class);
        } else if (position == 6) {
            intent = new Intent(this, ZoomView3.class);
        } else if (position == 7) {
            intent = new Intent(this, ZoomView4.class);
        } else if (position == 8) {
            intent = new Intent(this, BigScreenDL.class);
        } else if (position == 9) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urlgithub)));
        }

        startActivity(intent);
    }

}
