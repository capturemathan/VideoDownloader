package com.allvideodownloader.socialmedia.videodownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.allvideodownloader.socialmedia.videodownloader.R;
import com.allvideodownloader.socialmedia.videodownloader.fragments.fragment_fb;
import com.allvideodownloader.socialmedia.videodownloader.fragments.fragment_insta_image;
import com.allvideodownloader.socialmedia.videodownloader.fragments.fragment_whatsapp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    fragment_fb fragmentFb = new fragment_fb();
    fragment_insta_image fragmentInstaImage = new fragment_insta_image();
    fragment_whatsapp fragmentWhatsapp = new fragment_whatsapp();
    Fragment fragment = fragmentFb;
    int c = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_facebook:
                    fragment = fragmentFb;
                    break;
                case R.id.navigation_instagram:
                    fragment = fragmentInstaImage;
                    break;
                case R.id.navigation_whatsapp:
                    fragment = fragmentWhatsapp;
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(fragment);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setHapticFeedbackEnabled(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft =
                    getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }
}
