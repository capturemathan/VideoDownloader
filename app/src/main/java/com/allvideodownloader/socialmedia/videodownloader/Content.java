package com.allvideodownloader.socialmedia.videodownloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Content extends AppCompatActivity {

    public static int id =1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
    }

    public void facebook(View view) {
        Intent i = new Intent(Content.this,DownloadActivity.class);
        startActivity(i);
        id =1;
    }

    public void insta(View view) {
        Intent i = new Intent(Content.this,DownloadActivity.class);
        startActivity(i);
        id=2;
    }

    public void instapic(View view) {
        Intent i = new Intent(Content.this,DownloadActivity.class);
        startActivity(i);
        id=3;
    }

    public void whatsapp(View view) {
        Intent i = new Intent(Content.this,WhatsApp.class);
        startActivity(i);
        id=4;
    }
}