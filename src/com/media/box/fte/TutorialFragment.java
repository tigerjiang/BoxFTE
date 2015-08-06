/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;

import java.util.ArrayList;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Contains logic for the tutorial fragment.
 * 
 * @author brltluza
 */
public class TutorialFragment extends Fragment {

    // log variables
    private static final String TAG = "FTE";
    private static boolean DEBUG = false;

    // the inflated tutorial view - root element
    private View mView;

    /**
     * Tutorial images
     */
    private static int[] TUTORIAL_THUMBNAILS = {
            R.drawable.fte_remote_1,
            R.drawable.fte_remote_2,
            R.drawable.fte_remote_3,
    };

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.tutorial_fragment, container, false);

        BannerCarousel carousel = (BannerCarousel) mView.findViewById(R.id.carousel);

        ImageView  thumbnail;
        ArrayList<ImageView> carouselItems = new ArrayList<ImageView>();
        for (int i = 0; i < TUTORIAL_THUMBNAILS.length; i++) {
            thumbnail = new ImageView(getActivity());
            thumbnail.setImageResource(TUTORIAL_THUMBNAILS[i % TUTORIAL_THUMBNAILS.length]);

            carouselItems.add(thumbnail);
        }
        carousel.setCarouselItems(carouselItems);
        // set carousel interval to high
        carousel.setFlipInterval(BannerCarousel.FlipRate.READING);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // should be focused initially
        Button buttonNext = (Button) mView.findViewById(R.id.button_next);
        buttonNext.requestFocus();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // show/change the progress bar only after the fragment is attached.
        ((MainActivity) activity).updateProgressBar();
    }
}
