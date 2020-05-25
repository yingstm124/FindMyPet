package com.example.findmypet.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.example.findmypet.R;
import com.example.findmypet.View.LoginActivity;

public class SlideAdapter extends PagerAdapter {

    Context mcontext;
    LayoutInflater inflater;


    public int[] imageArray = new int[] {R.drawable.dogview1,
                                R.drawable.dog_home,
                                R.drawable.dog_home};

    public String[] descriptionArray = new String[] {"ปัดซ้าย",
                                        "ปัดซ้ายอีก"};

    public int[] bgArray  = new int[] {R.drawable.yellow,
            R.drawable.purple,
            R.drawable.red};

    // constructor
    public SlideAdapter(Context context) {
        mcontext = context;
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        // create view
        inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.slide, container, false);


        // init
        final Animation Anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounce);

        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout_view);
        ImageView imageView = (ImageView) v.findViewById(R.id.slide_img);
        TextView textView = (TextView) v.findViewById(R.id.slide_des);
        Button button = (Button) v.findViewById(R.id.slide_btn);

        if(position <= 1){
            linearLayout.setBackgroundResource(bgArray[position]);
            imageView.setImageResource(imageArray[position]);
            textView.setText(descriptionArray[position]);
            button.setVisibility(View.INVISIBLE);
        }
        else{
            linearLayout.setBackgroundResource(bgArray[position]);
            imageView.setImageResource(imageArray[position]);
            button.startAnimation(Anim);

            // when user click go to login
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myApp myapp = new myApp();
                    myapp.goLogin();
                }
            });
        }




        container.addView(v);

        return v;
    }

    public class myApp extends AppCompatActivity{
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public void goLogin(){
            Intent goLogin = new Intent(mcontext, LoginActivity.class);
            mcontext.startActivity(goLogin);
        }
    }


}
