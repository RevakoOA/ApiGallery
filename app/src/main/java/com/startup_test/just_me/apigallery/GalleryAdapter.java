package com.startup_test.just_me.apigallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by just_me on 03.08.16.
 */
final class GalleryAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> urls = new ArrayList<>();
    Picasso picasso;

    public GalleryAdapter(Context context, String[] picturesURLs, final String userID) {
        this.context = context;

        // init picasso for using special header
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request newRequest = chain.request().newBuilder()
                                .addHeader("user_id", userID)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        // Ensure we get a different ordering of images on each run.
        Collections.addAll(urls, picturesURLs);
        Collections.shuffle(urls);

        // x4 up the list.
        ArrayList<String> copy = new ArrayList<>(urls);
        urls.addAll(copy);
        urls.addAll(copy);
        urls.addAll(copy);

    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(context);
            view.setScaleType(CENTER_CROP);
        }

        // Get the image URL for the current position.
        String url = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        picasso.load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .fit()
                .tag(context)
                .into(view);

        return view;
    }

    @Override public int getCount() {
        return urls.size();
    }

    @Override public String getItem(int position) {
        return urls.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }
}

