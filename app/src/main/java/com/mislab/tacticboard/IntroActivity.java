package com.mislab.tacticboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager2 screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //
        List<ScreenItem> mList =new ArrayList<>();
        mList.add(new ScreenItem("Step 1","",R.drawable.basketball));
        mList.add(new ScreenItem("Step 2","",R.drawable.basketball));
        mList.add(new ScreenItem("Step 3","",R.drawable.basketball));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        
        screenPager.setAdapter(introViewPagerAdapter);

    }
}