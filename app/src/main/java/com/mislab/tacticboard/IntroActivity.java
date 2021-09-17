package com.mislab.tacticboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager2 screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position=0;
    Button btnGetStarted, btnSkipped;
    Animation btnAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check prefs (if it is opened before or not?)
        if(restorePrefsData()){
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);

        // hide action bar
        getSupportActionBar().hide();

        // init views
        tabIndicator=findViewById(R.id.intro_tab);
        btnNext =findViewById(R.id.intro_next_button);
        btnGetStarted=findViewById(R.id.intro_start_btn);
        btnSkipped=findViewById(R.id.intro_skip_btn);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.intro_btn_anim);

        // list component
        final List<ScreenItem> mList =new ArrayList<>();
        mList.add(new ScreenItem("Click the record button",getString(R.string.step1),R.drawable.step1_final));
        mList.add(new ScreenItem("Draw the tactics",getString(R.string.step2),R.drawable.step2_final));
        mList.add(new ScreenItem("Play the tactics",getString(R.string.step3),R.drawable.step3_final));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager
        final TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabIndicator, screenPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        });
        tabLayoutMediator.attach();

        //next button click listener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position =screenPager.getCurrentItem();
                if(position < mList.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position==mList.size()-1){ // reach to the last screen
                    loadLastScreen();
                }
            }
        });

        //tab layout listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==mList.size()-1){

                    loadLastScreen();

                }else{

                    loadPreviousScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Get Started btn click listener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open main activity
                Intent mainActivity= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);

                // user shared preference to record the whether user is first time open this app?
                savePrefsData();
                tabLayoutMediator.detach(); //detach才可以gc
                finish();
            }
        });

        // skip btn click listener
        btnSkipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open main activity
                Intent mainActivity= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);

                // user shared preference to record the whether user is first time open this app?
                savePrefsData();
                tabLayoutMediator.detach();
                finish();
            }
        });
    }

    //restore prefs record
    private Boolean restorePrefsData(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        Boolean isOpenedBefore= pref.getBoolean("isIntroOpened",false);
        return isOpenedBefore;

    }

    // 存取prefs到local
    private void savePrefsData() {

        SharedPreferences prefs= getApplicationContext().getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();

    }

    // show the GETSTARTED button and hide the next/skip btn
    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        btnSkipped.setVisibility(View.INVISIBLE);

        // btn animation
        btnGetStarted.startAnimation(btnAnimation); //如果用setAnimation 只會在第一次有結果

    }

    // show skip/next btn
    private void loadPreviousScreen(){

        if(btnNext.getVisibility()==View.INVISIBLE){

            btnNext.setVisibility(View.VISIBLE);
            btnGetStarted.setVisibility(View.INVISIBLE);
            btnSkipped.setVisibility(View.VISIBLE);

        }

    }
}