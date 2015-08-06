/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Contains logic for welcome screen fragment.
 *
 * @author brltluza
 */
public class WelcomeScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_screen_fragment, container, false);

        //set focus
        ImageButton buttonNext = (ImageButton) view.findViewById(R.id.button_next);
        buttonNext.requestFocus();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // show/change the progress bar only after the fragment is attached.
        ((MainActivity) activity).updateProgressBar();
    }
}
