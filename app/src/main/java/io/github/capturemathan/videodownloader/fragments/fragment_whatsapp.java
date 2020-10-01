package io.github.capturemathan.videodownloader.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import io.github.capturemathan.videodownloader.R;
import io.github.capturemathan.videodownloader.adapters.WhatsAppAdapter;

public class fragment_whatsapp extends Fragment {
    ArrayAdapter<File> fileAdapter;
    private ArrayList<File> fileList = new ArrayList<File>();

    public fragment_whatsapp() {
        // Required empty public constructor
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatsapp, container, false);
        final String dirPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
        Log.e("Main", dirPath);
        final GridView gridView = rootView.findViewById(R.id.whatsappGridView);
        File dir = new File(dirPath);
        final File[] files = dir.listFiles();
        Log.e("Main", files[0].getName());
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                fileList.add(file);
            }
        }
        if (!fileList.isEmpty()) {
            fileAdapter = new WhatsAppAdapter(fileList, getContext(), R.color.colorPrimaryDark);
            final GridView downloads = gridView;
            downloads.setAdapter(fileAdapter);

            downloads.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final ArrayList<String> vidOptions = new ArrayList<>();
                                vidOptions.add("View");
                                vidOptions.add("Save");
                                vidOptions.add("Share");
                                @SuppressLint("ResourceType") AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                                alertDialog.setTitle("Select Your Choice");
                                ArrayAdapter<String> optionsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, vidOptions);
                                alertDialog.setAdapter(optionsAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int pos) {
                                        if (pos == 0) {
                                            Intent intent = new Intent();
                                            intent.setAction(Intent.ACTION_VIEW);
                                            if (fileList.get(i).toString().endsWith(".jpg")) {
                                                intent.setDataAndType(Uri.parse(fileList.get(i).toString()), "image/*");
                                            } else {
                                                intent.setDataAndType(Uri.parse(fileList.get(i).toString()), "video/*");
                                            }
                                            startActivity(intent);
                                        } else if (pos == 1) {
                                            try {
                                                copyFile(fileList.get(i), new File(Environment.getExternalStorageDirectory().toString() + "/VideoDownloader/" + fileList.get(i).getName()));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            StyleableToast.makeText(getActivity(), "Succesfully Saved :)", Toast.LENGTH_SHORT, R.style.wapptoast).show();
                                        } else if (pos == 2) {
                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                            if (fileList.get(i).toString().endsWith(".jpg")) {
                                                intent.setType("image/*");
                                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileList.get(i).toString()));

                                                intent.putExtra(Intent.EXTRA_SUBJECT,
                                                        "Sharing Image");
                                                intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");

                                                startActivity(Intent.createChooser(intent, "Share Image"));
                                            } else {
                                                intent.setType("video/*");
                                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileList.get(i).toString()));

                                                intent.putExtra(Intent.EXTRA_SUBJECT,
                                                        "Sharing Video");
                                                intent.putExtra(Intent.EXTRA_TEXT, "Sharing Video");

                                                startActivity(Intent.createChooser(intent, "Share Video"));
                                            }
                                        }

                                    }
                                });
                                alertDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                StyleableToast.makeText(getActivity(), "Some Problem Occured :(", Toast.LENGTH_SHORT, R.style.wapptoast).show();
                            }
                        }
                    }.run();
                }
            });
        }
        return rootView;
    }
}