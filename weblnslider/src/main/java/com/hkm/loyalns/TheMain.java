package com.hkm.loyalns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        items.add("Zoom in and out slider Example");
        items.add("News Article Example");
        items.add("MultiSection Example");
        items.add("Classic Example");
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
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urlgithub)));
        }

        startActivity(intent);
    }

}
