package io.github.capturemathan.videodownloader.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.muddzdev.styleabletoast.StyleableToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.github.capturemathan.videodownloader.R;
import io.github.capturemathan.videodownloader.helpers.checkNetwork;

public class fragment_fb extends Fragment {
    ProgressDialog progressDialog;
    EditText editText;
    String downlink;
    String matag;
    TextView t;
    Button b;
    AlertDialog.Builder builder;

    public fragment_fb() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fb, container, false);
        Button button = rootView.findViewById(R.id.download);
        progressDialog = new ProgressDialog(getContext(),R.style.ProgressDialogTheme);
        editText = rootView.findViewById(R.id.dsrlink);
        t = rootView.findViewById(R.id.genlink);
        b = rootView.findViewById(R.id.fbdload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downlink = editText.getText().toString();
                if (downlink.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter link", Toast.LENGTH_SHORT).show();
                }
                else if (!checkNetwork.isConnected(getActivity())) {
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please Connect to the Internet")
                            .setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Internet Required");
                    alert.show();
                } else {
                    new Facebook().execute();
                }
            }
        });
        return rootView;
    }

    private class Facebook extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://www.getfvid.com/downloader")
                        .data("url", downlink)
                        .post();
                Element p = doc.select("p.card-text").first();
                title = p.text();
                Log.e("Main", title);
                Element link = doc.select("p#sdlink").first();
                String atag = link.select("a").first().attr("href");
                matag = atag;
                Log.e("Main", matag);

            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Invalid Link", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (title == null) {
                b.setVisibility(View.INVISIBLE);
            } else {
                t.setText(title);
                b.setVisibility(View.VISIBLE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(matag);
                        DownloadManager.Request req = new DownloadManager.Request(uri);
                        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        String filename = title;
                        filename += ".mp4";
                        req.setDestinationInExternalPublicDir("/VideoDownloader", filename);
                        StyleableToast.makeText(getActivity(), "Download Started", Toast.LENGTH_SHORT, R.style.mytoast).show();
                        Long ref = dm.enqueue(req);
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}