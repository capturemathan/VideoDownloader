package io.github.capturemathan.videodownloader.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muddzdev.styleabletoast.StyleableToast;

import io.github.capturemathan.videodownloader.R;
import io.github.capturemathan.videodownloader.fragments.fragment_fb;
import io.github.capturemathan.videodownloader.fragments.fragment_insta_image;
import io.github.capturemathan.videodownloader.fragments.fragment_whatsapp;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
            case R.id.menuTheme:
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    StyleableToast.makeText(this, "Dark Mode : ON", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    editor.putBoolean("DarkModeOn", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    StyleableToast.makeText(this, "Dark Mode : OFF", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    editor.putBoolean("DarkModeOn", false);
                }
                editor.apply();
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

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("ThemeSharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean DarkMode = sharedPreferences.getBoolean("DarkModeOn", false);

        if (!DarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Main", "Permission is granted");
            } else {
                Log.v("Main", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PermissionChecker.PERMISSION_GRANTED) {
                // good to go
                Log.v("Main", "Permission is granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Main", "Permission is granted");
            } else {
                Log.v("Main", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permission == PermissionChecker.PERMISSION_GRANTED) {
                Log.v("Main", "Permission is granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
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
