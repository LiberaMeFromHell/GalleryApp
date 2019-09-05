package com.example.galleryapp.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.galleryapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewFragment extends Fragment {

    private final String imageUri;
    private final int imageID;

    public PreviewFragment(String imageUri, int imageID) {
        this.imageUri = imageUri;
        this.imageID = imageID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        ImageView imageView = view.findViewById(R.id.previewImage);

        Glide.with(getActivity())
                .load(imageUri)
                .into(imageView);

        return view;
    }

    public int getImageID() {
        return imageID;
    }
}
