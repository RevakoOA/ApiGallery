package com.startup_test.just_me.apigallery;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by just_me on 29.07.16.
 */
public class MainActivity extends Activity {
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        loadingFragment = new LoadingFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, loadingFragment)
                .addToBackStack(null)
                .commit();
    }

    public void galleryStart(Bundle bundle) {
        // create gallery fragment and set info in it
        GalleryFragment gallery = new GalleryFragment();
        gallery.setArguments(bundle);

        // set fragment inside activity
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit);
        fragmentTransaction.replace(R.id.container, gallery)
                .addToBackStack(null)
                .commit();
    }

}