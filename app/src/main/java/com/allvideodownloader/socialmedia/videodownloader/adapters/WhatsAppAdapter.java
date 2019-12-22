package com.allvideodownloader.socialmedia.videodownloader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.allvideodownloader.socialmedia.videodownloader.R;

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
        // check if there is an existing list item (called convertView) that we can use,
        // otherwise if convertView is null, then inflate a new list item layout .
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.whatsapp_list_item, parent, false);
        }
        File currentFile = filesList.get(position);
        TextView t = listItemView.findViewById(R.id.statustitle);
        t.setText(currentFile.getName());
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);
        ImageView i = listItemView.findViewById(R.id.listimage);
        if (currentFile.getAbsolutePath().endsWith(".mp4")) {
            //Uri video = Uri.parse(currentFile.getAbsolutePath());
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(currentFile.getAbsolutePath(),
                    MediaStore.Images.Thumbnails.MINI_KIND);
            i.setImageBitmap(thumb);
        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            i.setImageBitmap(myBitmap);
        }
        return listItemView;
    }
}