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
import android.widget.Button;

/**
 * Contains logic for completion screen fragment.
 *
 * @author brltluza
 */
public class CompletionScreenFragment extends Fragment {

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completion_screen_fragment, container, false);

        // set focus on the button 'Done'
        Button buttonDone = (Button) view.findViewById(R.id.button_done);
        buttonDone.requestFocus();

        return view;
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
