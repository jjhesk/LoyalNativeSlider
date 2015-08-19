package com.hkm.sliderdemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.Indicators.NumContainer;
import com.hkm.slider.Indicators.PagerIndicator;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.TransformerL;
import com.hkm.slider.Tricks.ViewPagerEx;
import com.hkm.sliderdemo.Util.ChildAnimationExample;
import com.hkm.sliderdemo.Util.DataProvider;
import com.hkm.sliderdemo.modules.CustomNumberView;
import com.hkm.sliderdemo.modules.TransformerAdapter;
import com.hkm.sliderdemo.modules.munum;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;


    @SuppressLint("ResourceAsColor")
    private void setupSlider() {
        // remember setup first
        mDemoSlider.setPresetTransformer(TransformerL.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(R.color.red_pink_24, R.color.red_pink_26);
        final munum n = new munum(this);
        n.setAlignment(NumContainer.Alignment.Center_Bottom);
        mDemoSlider.setNumLayout(n);
        mDemoSlider.presentation(SliderLayout.PresentationConfig.Numbers);
        ListView l = (ListView) findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //and data second. it is a must because you will except the data to be streamed into the pipline.
        defaultCompleteSlider(DataProvider.getDataSource());
    }

    protected void customSliderView(final HashMap<String, Integer> maps) {
        for (String name : maps.keySet()) {
            CustomNumberView textSliderView = new CustomNumberView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(maps.get(name))

                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, Integer> maps) {
        for (String name : maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        setupSlider();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean numbered = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle_number_display:
                numbered = !numbered;
                mDemoSlider.presentation(numbered ? SliderLayout.PresentationConfig.Numbers : SliderLayout.PresentationConfig.Dots);
                break;

            case R.id.indicator_default:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator2));
                break;

            case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;

            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;

            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;

            case R.id.custom_slider_layout:
                newcustomSliderView();
                break;

            case R.id.default_slider_layout:
                newloaddefaultCompleteSlider();
                break;

            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.urlgithub)));
                startActivity(browserIntent);
                break;

            case R.id.action_bigscreendemo:
                Intent hk_scn_demo = new Intent(this, BigScreenDemo.class);
                startActivity(hk_scn_demo);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void newcustomSliderView() {
        final Handler n = new Handler();
        mDemoSlider.removeAllSliders();
        n.postDelayed(new Runnable() {
            @Override
            public void run() {
                customSliderView(DataProvider.getDataSource());
            }
        }, 2000);
    }

    protected void newloaddefaultCompleteSlider() {
        final Handler n = new Handler();
        mDemoSlider.removeAllSliders();
        n.postDelayed(new Runnable() {
            @Override
            public void run() {
                defaultCompleteSlider(DataProvider.getDataSource());
            }
        }, 2000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
