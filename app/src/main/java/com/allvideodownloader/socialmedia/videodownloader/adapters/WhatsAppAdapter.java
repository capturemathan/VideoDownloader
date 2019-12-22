package com.allvideodownloader.socialmedia.videodownloader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allvideodownloader.socialmedia.videodownloader.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class WhatsAppAdapter extends ArrayAdapter {
    private ArrayList<File> filesList;
    private Context mContext;
    private int mColorResourceId;

    public WhatsAppAdapter(ArrayList<File> filesList, Context context, int colorResourceId) {
        super(context, 0, filesList);
        this.filesList = filesList;
        mContext = context;
        mColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.whatsapp_grid_item, parent, false);
        }
        File currentFile = filesList.get(position);
        TextView t = gridItemView.findViewById(R.id.whatsapp_text);
        t.setText(currentFile.getName());
        ImageView imageView = gridItemView.findViewById(R.id.playbtn);
        ImageView i = gridItemView.findViewById(R.id.whatsapp_picture);
        if (currentFile.getAbsolutePath().endsWith(".mp4")) {
            Glide.with(getContext()).load(currentFile.getAbsolutePath()).thumbnail(0.4f).into(i);
            imageView.setVisibility(View.VISIBLE);
        } else {
            Glide.with(getContext()).load(currentFile.getAbsolutePath()).thumbnail(0.4f).into(i);
        }
        return gridItemView;
    }
}