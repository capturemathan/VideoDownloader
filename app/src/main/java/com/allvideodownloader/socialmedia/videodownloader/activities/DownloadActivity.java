package com.allvideodownloader.socialmedia.videodownloader.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.allvideodownloader.socialmedia.videodownloader.R;
import com.allvideodownloader.socialmedia.videodownloader.activities.Content;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.muddzdev.styleabletoast.StyleableToast;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DownloadActivity extends AppCompatActivity {

    public static String DOWNLOAD_URL = "https://qdownloader.net/download?video=";
    public String matag;
    EditText mlink;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Main", "Permission is granted");
                //return true;
            } else {

                Log.v("Main", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                //return false;
            }
        } else {
            int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission == PermissionChecker.PERMISSION_GRANTED) {
                // good to go
                Log.v("Main", "Permission is granted");
            } else {
                // permission not granted, you decide what to do
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        View optionview = findViewById(R.id.option);
        optionview.setVisibility(View.GONE);
        Button b = (Button) findViewById(R.id.download);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View downbutton = findViewById(R.id.download);
                downbutton.setVisibility(View.GONE);
                String encodeURL = null;
                mlink = (EditText) findViewById(R.id.dsrlink);
                temp = mlink.getText().toString();
                try {
                    encodeURL = URLEncoder.encode(temp, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                DOWNLOAD_URL += encodeURL;

                if (Content.id == 1) {
                    DOWNLOAD_URL = "https://downvideo.net/download.php";
                    new Facebook().execute();
                } else if (Content.id == 3) {
                    new Insta().execute();
                } else if (Content.id == 2) {
                    new InstaVideo().execute();
                }

            }
        });
    }

    /*private class Title extends AsyncTask<Void,Void,Void>
    {
        String title;
        List<Video> videos = new ArrayList<Video>();
        List<String> durl=new ArrayList<>();
        List<String> dres=new ArrayList<>();
        List<String> dsize=new ArrayList<>();
        List<String> dformat=new ArrayList<>();
        int c =0;

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.loading_indicator);
            progressBar.setVisibility(View.VISIBLE);
            DoubleBounce doubleBounce = new DoubleBounce();
            progressBar.setIndeterminateDrawable(doubleBounce);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://fbdownload.io/download?video=https%3A%2F%2Ffacebook.com%2F10155827466654576").get();
                title = doc.select("span.largeMargin").text();
                Log.e("Hello",doc.title());
                Elements info = doc.select("div.info");
                //img = info.select("img[src]").text();
                Element table = doc.select("table.downloadsTable").first();
                Elements tbody = table.select("tbody");
                Elements tr = tbody.select("tr");
                //int i=0;
                for(Element row: tr)
                {
                    Elements td = row.select("td");
                    for (Element tabledata: td) {
                        //download+=tabledata.text();
                        if(c==0)
                        {
                            dres.add(tabledata.text());
                            c++;
                        }
                        else if(c==1)
                        {
                            dformat.add(tabledata.text());
                            c++;
                        }
                        else if(c==2)
                        {
                            dsize.add(tabledata.text());
                            c++;
                        }
                        else if(c==3)
                        {
                            Element atag = tabledata.select("a").first();
                            durl.add(atag.attr("href"));
                            c=0;
                        }
                }

            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for(int j=0;j<durl.size();j++)
            {
                Video vid = new Video(dres.get(j),dformat.get(j),dsize.get(j),durl.get(j));
                videos.add(vid);
            }
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            TextView t = (TextView) findViewById(R.id.genlink);
            t.setText(title);
            View optionview = findViewById(R.id.option);
            optionview.setVisibility(View.VISIBLE);
            final ListView videoListView = (ListView) findViewById(R.id.list);
            mAdapter = new VideoAdapter(getBaseContext(),videos,R.color.colorAccent);
            videoListView.setAdapter(mAdapter);
            videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(durl.get(i));
                    DownloadManager.Request req = new DownloadManager.Request(uri);
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    String filename = durl.get(i).substring(durl.get(i).lastIndexOf('=')+1, durl.get(i).length());
                    filename.replace("+"," ");
                    if(dformat.get(i).equals("mp4"))
                    {
                        filename+=".mp4";
                    }
                    else
                    {
                        filename+=".3gp";
                    }
                    req.setDestinationInExternalPublicDir("/VideoDownloader",filename);
                    Toast.makeText(DownloadActivity.this,"Download Started",Toast.LENGTH_SHORT).show();
                    Long ref = dm.enqueue(req);
                }
            });
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class Facebook extends AsyncTask<Void, Void, Void> {
        String title;
        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
            progressBar.setVisibility(View.VISIBLE);
            DoubleBounce doubleBounce = new DoubleBounce();
            progressBar.setIndeterminateDrawable(doubleBounce);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://www.getfvid.com/downloader")
                        .data("url", temp)
                        .post();
                Element p = doc.select("p.card-text").first();
                title = p.text();
                Log.e("Main", title);
                Element link = doc.select("p#sdlink").first();
                String atag = link.select("a").first().attr("href");
                matag = atag;
                Log.e("Main", matag);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            TextView t = (TextView) findViewById(R.id.genlink);
            t.setText(title);
            Button b = (Button) findViewById(R.id.fbdload);
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(matag);
                    DownloadManager.Request req = new DownloadManager.Request(uri);
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    String filename = title;
                    filename += ".mp4";
                    req.setDestinationInExternalPublicDir("/VideoDownloader", filename);
                    StyleableToast.makeText(getBaseContext(), "Download Started", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    Long ref = dm.enqueue(req);
                }
            });
        }
    }

    private class InstaVideo extends AsyncTask<Void, Void, Void> {

        String dlink, imglink;

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
            progressBar.setVisibility(View.VISIBLE);
            DoubleBounce doubleBounce = new DoubleBounce();
            progressBar.setIndeterminateDrawable(doubleBounce);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://www.10insta.net/#grid-gallery")
                        .data("url", temp)
                        .post();
                Log.v("Hello", doc.title());
                Element srctag = doc.select("img.card-img-top").first();
                Element ptag = doc.select("p.card-text").first();
                Element atag = ptag.select("a").first();
                imglink = srctag.attr("src");
                dlink = "https://www.10insta.net/";
                dlink += atag.attr("href");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            TextView t = (TextView) findViewById(R.id.imgtxt);
            if (Content.id == 2) {
                t.setText("Video Preview");
            } else {
                t.setText("Image Preview");
            }
            t.setVisibility(View.VISIBLE);
            Button b = (Button) findViewById(R.id.instadownload);
            b.setVisibility(View.VISIBLE);
            final ImageView img = (ImageView) findViewById(R.id.instaimg);
            Picasso.get().load(imglink).placeholder(R.drawable.loading).into(img);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(dlink);
                    DownloadManager.Request req = new DownloadManager.Request(uri);
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    if (Content.id == 2) {
                        req.setDestinationInExternalPublicDir("/VideoDownloader", "insta.mp4");
                    } else {
                        req.setDestinationInExternalPublicDir("/VideoDownloader", "insta.jpg");
                    }
                    StyleableToast.makeText(getBaseContext(), "Download Started", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    Long ref = dm.enqueue(req);
                }
            });
        }
    }

    private class Insta extends AsyncTask<Void, Void, Void> {
        String dlink;
        String imglink;

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
            progressBar.setVisibility(View.VISIBLE);
            DoubleBounce doubleBounce = new DoubleBounce();
            progressBar.setIndeterminateDrawable(doubleBounce);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://www.dinsta.com/photos/")
                        .data("url", temp)
                        .post();
                Log.v("Hello", doc.title());
                Element imgtag = doc.select("img").first();
                dlink = imgtag.attr("src");
                imglink = imgtag.attr("src");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            TextView t = (TextView) findViewById(R.id.imgtxt);
            if (Content.id == 2) {
                t.setText("Video Preview");
            } else {
                t.setText("Image Preview");
            }
            t.setVisibility(View.VISIBLE);
            Button b = (Button) findViewById(R.id.instadownload);
            b.setVisibility(View.VISIBLE);
            final ImageView img = (ImageView) findViewById(R.id.instaimg);
            Picasso.get().load(imglink).placeholder(R.drawable.loading).into(img);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(dlink);
                    DownloadManager.Request req = new DownloadManager.Request(uri);
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    if (Content.id == 2) {
                        req.setDestinationInExternalPublicDir("/VideoDownloader", "insta.mp4");
                    } else {
                        req.setDestinationInExternalPublicDir("/VideoDownloader", "insta.jpg");
                    }
                    StyleableToast.makeText(getBaseContext(), "Download Started", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    Long ref = dm.enqueue(req);
                }
            });
        }
    }
}