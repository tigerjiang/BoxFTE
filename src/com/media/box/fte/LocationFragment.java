/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class LocationFragment extends GenericFragment {
    private static final String AREA_DIALOG_ACTION = "android.settings.AREA_DIALOG";
    // logging variables
    private static final String TAG = "FTE";
    private static boolean DEBUG = false;

    // extras' names
    static final String PROVINCE = "province";
    static final String CITY = "city";

    protected static final int REQUEST_CODE = 105;

    // are static to be saved if the fragment is recreated
    private static String sProvince;
    private static String sCity;

    private Button mButton;
    private TextView mTextViewCurrLoc;

    /*
     * (non-Javadoc)
     * @see com.jamdeo.tv.fte.GenericFragment#initValues()
     */
    protected void initValues() {
        mActionAreaId = R.layout.location_action_area;
        mBackgroundId = R.drawable.fte_bg_location;
        mTitleStrId = R.string.location_title;
        mDescrStrId = R.string.location_descr;
        mTipStrId = R.string.location_tip;

        getCurrLocationFromSettings();
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

        mButton = (Button) view.findViewById(R.id.button_change_loc);
        mTextViewCurrLoc = (TextView) view.findViewById(R.id.textview_curr_loc);

        if (sProvince == null) {
            sProvince = getResources().getString(R.string.default_location);
            Settings.System.putString(getActivity().getContentResolver(),
                    getResources().getString(R.string.settings_personal_area_province_key),
                    sProvince);
        }
        updateLocationLabel();

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AREA_DIALOG_ACTION);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (DEBUG) {
            Log.d(TAG, "Request code: " + requestCode + " result code:" + resultCode);
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            getCurrLocationFromSettings();
            updateLocationLabel();
        }
    }

    /**
     * Retrieve current province and city from the Settings.
     */
    private void getCurrLocationFromSettings() {
        sProvince = Settings.System.getString(getActivity().getContentResolver(),
                getResources().getString(R.string.settings_personal_area_province_key));
        sCity = Settings.System.getString(getActivity().getContentResolver(), getResources()
                .getString(R.string.settings_personal_area_city_key));
    }

    /**
     * Update location.
     */
    private void updateLocationLabel() {
        String strChina = getResources().getString(R.string.china);
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.str_curr_loc)).append(' ');

        if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            sb.append(strChina).append(", ").append(sProvince);
            if (sCity != null && !"".equals(sCity)) {
                sb.append(", ").append(sCity);
            }
        } else {
            if (sCity != null && !"".equals(sCity)) {
                sb.append(sCity).append(", ");
            }
            sb.append(sProvince).append(", ").append(strChina);
            if (!Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                Log.w(TAG, "Unsupported locale: " + Locale.getDefault().getLanguage());
            }
        }
        mTextViewCurrLoc.setText(sb);
    }
}
