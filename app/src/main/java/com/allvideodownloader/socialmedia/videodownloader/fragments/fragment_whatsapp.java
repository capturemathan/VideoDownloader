package com.allvideodownloader.socialmedia.videodownloader.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.allvideodownloader.socialmedia.videodownloader.R;

public class fragment_whatsapp extends Fragment {

    public fragment_whatsapp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatsapp, container, false);
        return rootView;
    }
}