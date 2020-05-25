package com.example.findmypet.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.findmypet.Fragment_.fragment_cat;
import com.example.findmypet.Fragment_.fragment_dog;
import com.example.findmypet.Fragment_.fragment_other;
import com.example.findmypet.Navigation_fragment.listFragment;
import com.example.findmypet.Navigation_fragment.postFragment;
import com.example.findmypet.Navigation_fragment.profileFragment;
import com.example.findmypet.R;
import com.example.findmypet.Adapter.ViewPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity  {

    private Dialog dialog_addpost;
    public Toolbar toolbar;
    private BottomNavigationView bt_nav;


    //private TabLayout hometabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;
    private TabLayout hometabLayout;

    private Fragment active_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // init
        viewPager = (ViewPager) findViewById(R.id.view_contrainer);
        hometabLayout = (TabLayout) findViewById(R.id.home_tab);
        active_fragment = new profileFragment();

        // dialog
        dialog_addpost = new Dialog(this);

        setupToolbar();
        setupTabLayout();
        setupBottomNav();

    }

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // setting toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    protected void setupTabLayout() {

        adapter = new ViewPageAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new fragment_dog(), "Dog");
        adapter.AddFragment(new fragment_cat(), "Cat");
        adapter.AddFragment(new fragment_other(), "Other");

        viewPager.setAdapter(adapter);
        hometabLayout.setupWithViewPager(viewPager);

    }

    protected void setupBottomNav() {
        // *****    navigation
        //          Home        -->    display total post information
        //                                  1.  dog
        //                                  2.  cat
        //                                  3.  other
        //          Profile     -->     personal information
        //                                  1.  name
        //                                  2.  e-mail
        //                                  3.  picture (from facebook)
        //          Post        -->     history user posted
        //                                  1.  title
        //                                  2.  picture (Pet)
        //                                  3.  name (poster)
        //                                  4.  description
        //                                  5.  tel no.
        //          List        --->    interesting post
        //                                  1.  title
        //                                  2.  picture (Pet)
        //                                  3.  name (poster)
        //                                  4.  description
        //                                  5.  tel no.


        bt_nav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bt_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.nav_home) {

                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_LONG).show();
                    Intent goHome = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(goHome);
                    return true;

                } else if (id == R.id.nav_profile) {

                    Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_LONG).show();
                    active_fragment = new profileFragment();

                    viewPager.setVisibility(View.INVISIBLE);
                    hometabLayout.removeAllTabs();


                } else if (id == R.id.nav_post) {

                    Toast.makeText(getApplicationContext(), "my post", Toast.LENGTH_LONG).show();
                    active_fragment = new postFragment();

                    viewPager.setVisibility(View.INVISIBLE);
                    hometabLayout.removeAllTabs();

                } else if (id == R.id.nav_list) {

                    Toast.makeText(getApplicationContext(), "list", Toast.LENGTH_LONG).show();
                    active_fragment = new listFragment();
                    
                    viewPager.setVisibility(View.INVISIBLE);
                    hometabLayout.removeAllTabs();

                }

                return loadFragment();
            }
        });
    }

    private boolean loadFragment(){
        if(active_fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, active_fragment).commit();
            Toast.makeText(getApplicationContext(), "success load fragment", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(getApplicationContext(), "failed load fragment", Toast.LENGTH_SHORT).show();
        return false;

    }

    // init menu on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // user click add post
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_post) {
            Toast.makeText(getApplicationContext(), "Click add post", Toast.LENGTH_LONG).show();
            goAddPost();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void goAddPost() {
        Intent AddPost = new Intent(HomeActivity.this, AddPostActivity.class);
        startActivity(AddPost);
    }



}
