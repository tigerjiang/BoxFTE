/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


/**
 * Class implementing logic for Change Language screen.
 * 
 * @author brltluza
 */
public class LanguagesFragment extends GenericFragment {

    // debug variables
    static final String TAG = "FTE";
    static boolean DEBUG = false;

    private Button mButton;
    private TextView mTextViewCurrLang;

    /*
     * (non-Javadoc)
     * @see com.jamdeo.tv.fte.GenericFragment#initValues()
     */
    protected void initValues() {
        mActionAreaId = R.layout.languages_action_area;
        mBackgroundId = R.drawable.fte_bg_language;
        mTitleStrId = R.string.languages_title;
        mDescrStrId = R.string.languages_descr;
        mTipStrId = R.string.languages_tip;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.jamdeo.tv.fte.GenericFragment#onCreateView(android.view.LayoutInflater
     * , android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View view =
                super.onCreateView(inflater, container, savedInstanceState);

        mButton = (Button) view.findViewById(R.id.button_change_lang);
        mTextViewCurrLang = (TextView) view.findViewById(R.id.textview_curr_lang);

        updateUI();

        // make 'Back' button invisible on this screen
        final Button buttonBack = (Button) mView.findViewById(R.id.button_back);
        buttonBack.setVisibility(View.INVISIBLE);

        // set 'Change Language' button click processing
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // change language
                final Locale newLocale = getNewLocale();
                com.android.internal.app.LocalePicker.updateLocale(newLocale);

                updateUI();
            }
        });

        return view;
    }

    /**
     * Returns locale to be changed to.
     * 
     * @return
     */
    private Locale getNewLocale() {
        Locale newLocale;
        if (Locale.getDefault().getLanguage().startsWith(Locale.CHINESE.getLanguage())) {
            newLocale = Locale.ENGLISH;
        }
        else {
            newLocale = Locale.CHINA;
            if (!Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                Log.w(TAG, "Unsupported locale: " + Locale.getDefault().getLanguage());
            }
        }

        return newLocale;
    }

    /**
     * Update buttons and textViews with correct text labels, depending on what
     * language is current and which it can be switched to.
     */
    private void updateUI() {
        final Locale newLocale = getNewLocale();
        final String strChangeLanguage = getResources().getString(R.string.str_change_lang_to);
        // set change language to
        mButton.setText(strChangeLanguage + " " + newLocale.getDisplayLanguage(newLocale));
        mTextViewCurrLang.setText(Locale.getDefault().getDisplayLanguage());
    }
}
