package com.allvideodownloader.socialmedia.videodownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class VideoAdapter extends ArrayAdapter<Video> {

    private int mColorResourceId;
    private Context mContext;

    public VideoAdapter(Context context, List<Video> newsfeed, int colorResourceId) {
        super(context, 0, newsfeed);
        mColorResourceId = colorResourceId;
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // check if there is an existing list item (called convertView) that we can use,
        // otherwise if convertView is null, then inflate a new list item layout .
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.video_list_item, parent, false);
        }
        final Video currentvid = getItem(position);
        TextView vidres = (TextView) listItemView.findViewById(R.id.res);
        vidres.setText(currentvid.getRes());
        TextView vidform = (TextView) listItemView.findViewById(R.id.form);
        vidform.setText(currentvid.getFormat());
        TextView vidsiz = (TextView) listItemView.findViewById(R.id.size);
        vidsiz.setText(currentvid.getSize());
        return listItemView;
    }
}

