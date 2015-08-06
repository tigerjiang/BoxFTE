/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;


/**
 * Main activity class. Contains moving between fragments logic, and some
 * application-wide logic.
 * 
 * @author brltluza
 */
public class MainActivity extends Activity {
    // debug variables
    private static boolean DEBUG = true;
    private static final String TAG = "FTE";




    // ids of the fragments used in the app in the specific order
    private int mFragmentIds[] = {
            R.id.welcome_screen_fragment, R.id.main_fragment, /*R.id.main_fragment,*/
            /*R.id.tutorial_fragment,*/ R.id.main_fragment, /* R.id.main_fragment, */ //cvte no hicloud
            /*R.id.main_fragment,*/
            R.id.completion_screen_fragment
    };

    // classes of fragments in the specific order
    private Class<?> mClasses[] = {
            WelcomeScreenFragment.class, LanguagesFragment.class,/* UserAgreementFragment.class,*/
            /*TutorialFragment.class,*/ NetworkFragment.class, /* HiCloudFragment.class, */ //cvte no hicloud
           /* LocationFragment.class, */
            CompletionScreenFragment.class
    };

    // current fragment index
    public int mCurrIndex = 0;

    // progress bar of the app
    private ProgressBar mProgressBar;

    // preferences of the app
    private SharedPreferences mSharedPref;

    private volatile boolean mIsSourceManagerAvailable = false;

    static final String PREF_CURR_INDEX = "curr_index";
    static final String PREF_FILE_KEY = "com.media.box.fte.PREFERENCE_FILE_KEY";

    private static final String UPDATE_LOCATION = "android.settings.UPDATE_LOCATION";


    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // set container view
        setContentView(R.layout.fragment_container);
        updateProgressBar();

        // a workaround fix for setting language - the system restarts
        // activity after change of language
        mSharedPref = getBaseContext().getSharedPreferences(
                PREF_FILE_KEY, Context.MODE_PRIVATE);

        // retrieve currIndex
        // in case if FTE previously failed for some reason will start from the
        // saved index
        mCurrIndex = mSharedPref.getInt(PREF_CURR_INDEX, 0);
//        if (mCurrIndex != 0) {
//            Log.w(TAG,
//                    "FTE was restarted for some reason, maybe unhandled change of configuration, restarting from screen "
//                            + mCurrIndex);
//        }else{
//            loadFragment();
//        }
        loadFragment();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (DEBUG) {
            Log.d(TAG, "OnConfiguration changed was called: " + newConfig);
        }
        loadFragment();
        Intent intent = new Intent(UPDATE_LOCATION);
        startActivity(intent);
    }

   

    /**
     * Load fragment.
     * 
     * @param add
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void loadFragment() {
        Fragment currFragment = getFragmentManager().findFragmentById(mFragmentIds[mCurrIndex]);
        if (currFragment == null) {
            // Make new fragment to show this selection.
            try {
                currFragment = (Fragment) mClasses[mCurrIndex].newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, currFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();

        // save screen index
        saveCurrentScreenNumber();
    }

    /**
     * Save current screen number. In case FTE is restarted (unhandled
     * configuration change for example), it will be restarted from the very
     * screen on which it was before.
     */
    private void saveCurrentScreenNumber() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(PREF_CURR_INDEX, mCurrIndex);
        editor.apply();
    }

    /**
     * Move to previous fragment. On button click handler.
     * 
     * @param view - source view
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void onBackClick(View view) {
        if (mCurrIndex > 0) {
            mCurrIndex--;
            loadFragment();
        }
    }

    /**
     * Move to next fragment. On button click handler.
     * 
     * @param view - source view
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void onNextClick(View view) {
        if (mCurrIndex < mFragmentIds.length - 1) {
            mCurrIndex++;
            loadFragment();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        // do nothing
    }

    /**
     * On button 'Done' click handler. Finish the activity.
     * 
     * @param view
     */
    public void onDoneClick(View view) {
        // clear currIndex
        final SharedPreferences.Editor editor = mSharedPref.edit();
        editor.remove(PREF_CURR_INDEX);
        editor.apply();

        

        // request the correct silo to be launched
        // TODO...

        // terminate the activity.
        finish();
    }

    /**
     * Update progress bar status.
     */
    public void updateProgressBar() {
        if (mProgressBar == null) {
            mProgressBar = (ProgressBar)
                    findViewById(R.id.progress_bar);
        }

        if (DEBUG) {
            Log.d(TAG, "ProgressBar.currIndex: " + mCurrIndex);
        }
        if (mProgressBar != null) {
            // progress bar should not be shown on splash and welcome screens
            if (mCurrIndex == 0 || mCurrIndex == mFragmentIds.length - 1) {
                mProgressBar.setVisibility(View.GONE);
            }
            else {
                mProgressBar.setVisibility(View.VISIBLE);
                int progress = mCurrIndex * mProgressBar.getMax()
                        / (mFragmentIds.length - 2);
                mProgressBar.setProgress(progress);
            }
        }
    }
}
