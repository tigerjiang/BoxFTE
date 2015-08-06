/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

import java.util.Locale;


/**
 * Class implementing logic for User Agreement screen.
 * 
 * @author brltluza
 */
public class UserAgreementFragment extends GenericFragment {
    // debug variables
    static final String TAG = "FTE";
    static boolean DEBUG = false;

    // contains max height of the scrollable text depending on the language
    protected int mMaxTextHeight = 0;

    private Button mButtonNext;

    // true if user once scrolled the agreement to the very end
    private static boolean sIsUserAgreementRead = false;

    // scroll handler
    private ScrollViewListener mScrollListener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                int oldy) {
            if (DEBUG) {
                Log.d("FTE", "y=" + y);
            }
            if (y >= mMaxTextHeight) {
                mButtonNext.setEnabled(true);
                sIsUserAgreementRead = true;
            }
        }
    };
    private ObservableScrollView mScrollView;

    /*
     * (non-Javadoc)
     * @see com.jamdeo.tv.fte.GenericFragment#initValues()
     */
    @Override
    protected void initValues() {
        mActionAreaId = R.layout.user_agreement_action_area;
        mBackgroundId = R.drawable.fte_bg_ula;
        mTitleStrId = R.string.user_agreement_title;
        mDescrStrId = R.string.user_agreement_descr;
        mTipStrId = R.string.user_agreement_tip;
        mIconTipId = R.drawable.fte_dpad_tip;
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
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Change button 'Next' name to 'Agree' on this page
        mButtonNext = (Button) mView.findViewById(R.id.button_next);
        // disable if not read
        if (!sIsUserAgreementRead) {
            mButtonNext.setEnabled(false);
        }
        mButtonNext.setText(R.string.agree);

        mScrollView = (ObservableScrollView) mView
                .findViewById(R.id.scrollview_user_agreement_info);
        mScrollView.setScrollbarFadingEnabled(false);

        return view;
    }

    /*
     * (non-Javadoc)
     * @see com.jamdeo.tv.fte.GenericFragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();

        if (!sIsUserAgreementRead) {
            mScrollView.setScrollViewListener(mScrollListener);
            mScrollView.requestFocus();
        } else {
            mScrollView.setScrollViewListener(null);
        }
        // set scroll view height
        mMaxTextHeight = mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight();
    }
}
