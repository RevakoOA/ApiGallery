package com.startup_test.just_me.apigallery;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by just_me on 03.08.16.
 */
public class GalleryFragment extends Fragment {

    @BindView(R.id.grid_view)
    GridView gridView;

    GalleryAdapter galleryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up adapter
        Bundle bundle = getArguments();
        String[] imagesURLs = bundle.getStringArray(getString(R.string.images_urls));
        String userID = bundle.getString(getString(R.string.userID));
        galleryAdapter = new GalleryAdapter(getActivity(), imagesURLs, userID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.gallery_fragment, container, false);
        ButterKnife.bind(this, result);
        gridView.setAdapter(galleryAdapter);
        gridView.setOnScrollListener(new GalleryScrollListener(getActivity()));
        return result;
    }
}
