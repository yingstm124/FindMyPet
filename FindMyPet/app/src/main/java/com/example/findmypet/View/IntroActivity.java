package com.example.findmypet.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.findmypet.Adapter.SlideAdapter;
import com.example.findmypet.R;

public class IntroActivity extends AppCompatActivity {

    ViewPager viewPager_intro;
    SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // setup viewpager
        viewPager_intro = (ViewPager) findViewById(R.id.intro_vp);
        slideAdapter = new SlideAdapter(this);
        viewPager_intro.setAdapter(slideAdapter);

    }
}
