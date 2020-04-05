package io.github.capturemathan.videodownloader.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.capturemathan.videodownloader.BuildConfig;
import io.github.capturemathan.videodownloader.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View aboutPage = new AboutPage(this)
                .setImage(R.drawable.about_appicon)
                .setDescription("Essential application in every phone that makes Social media downloads easier")
                .addItem(new Element().setTitle("Version: " + BuildConfig.VERSION_NAME))
                .addGroup("Connect with us")
                .addEmail("capturesmk@gmail.com")
                .addWebsite("http://capturemathan.github.io/")
                .addPlayStore("io.github.capturemathan.videodownloader")
                .addGitHub("capturemathan")
                .create();
        setContentView(aboutPage);
        aboutPage.setBackgroundColor(getResources().getColor(R.color.colorBackground));
    }
}
