/*
 * Copyright 2012 - Jamdeo
 */

package com.media.box.fte;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Contains logic for HiCloud fragment.
 * 
 * @author brltluza
 * @author brldmila
 */
public class HiCloudFragment extends GenericFragment {
    // debug variables
    private static final String TAG = "FTE";
    private static boolean DEBUG = false;

    // tips depending on the state
    private int[] mHiCloudTips = {
            R.string.hicloud_tip, R.string.hicloud_tip, R.string.hicloud_tip_no_network
    };

    // area ids depending on the state
    private int[] mAreaIds = {
            R.layout.hicloud_action_area, R.layout.hicloud_logged_in, R.layout.hicloud_no_network
    };

    // request code for start HiCloud activity
    protected static final int REQUEST_CODE = 101;

    // results returned by activity
    private static final int RESULT_LOGGED_IN = -1;

    // possible states
    private static int STATE_LOGGED_OUT = 0;
    private static int STATE_LOGGED_IN = 1;
    private static int STATE_NO_INTERNET = 2;

    // current state
    private static int sState = STATE_LOGGED_OUT;

    private Button mLoginButton;
    private View mLogoutButton;

    // encapsulates logic for detecting network
    private NetworkHelper mNetworkHelper;
    private static String sUserName;

    /*
     * (non-Javadoc)
     * @see com.jamdeo.tv.fte.GenericFragment#initValues()
     */
    protected void initValues() {
        mActionAreaId = mAreaIds[sState];
        mBackgroundId = R.drawable.fte_bg_account;
        mTitleStrId = R.string.hicloud_title;
        mDescrStrId = R.string.hicloud_descr;
        mTipStrId = mHiCloudTips[sState];
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
        mNetworkHelper = new NetworkHelper(mFteMainActivity);

        if (!mNetworkHelper.isWiFiConnected()
                && mNetworkHelper.isEthernetNetworkConnected() != NetworkHelper.WIRED_CONNECTED) {
            sState = STATE_NO_INTERNET;
        }
        // if previous state was 'no Internet' transition to logged out
        else if (sState == STATE_NO_INTERNET) {
            sState = STATE_LOGGED_OUT;
        }

        mView =
                super.onCreateView(inflater, container, savedInstanceState);

        if (sState == STATE_LOGGED_IN) {
            mLogoutButton = (Button) mView.findViewById(R.id.button_hicloud_logout);
            mLogoutButton.setOnClickListener(new ButtonLogOutListener(this));
            substituteUserName();
        }
        else if (sState == STATE_LOGGED_OUT) {
            mLoginButton = (Button) mView.findViewById(R.id.button_hicloud_login);
            mLoginButton.setOnClickListener(new ButtonLogInListener(this));
        }

        return mView;
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
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_LOGGED_IN) {
                sState = STATE_LOGGED_IN;
                sUserName = (String) data.getExtras().get("Name");
                loadLoggedInView();

                mLogoutButton.setOnClickListener(new ButtonLogOutListener(this));
            }
        }
    }

    /**
     * Load UI for the logged in state
     */
    private void loadLoggedOutView() {
        loadUserArea(R.layout.hicloud_action_area);

        mLoginButton = (Button) mView.findViewById(R.id.button_hicloud_login);
        mLoginButton.setOnClickListener(new ButtonLogInListener(this));
    }

    /**
     * Load UI for the logged out state
     */
    private void loadLoggedInView() {
        loadUserArea(R.layout.hicloud_logged_in);
        substituteUserName();
        mLogoutButton = (Button) mView.findViewById(R.id.button_hicloud_logout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadLoggedOutView();
            }
        });
    }

    /**
     * Load UI for the no network state
     */
    private void loadNoInternetView() {
        loadUserArea(R.layout.hicloud_no_network);
    }

    /**
     * Substitute user name in 'Congratulations' string.
     */
    private void substituteUserName() {
        final TextView textView = (TextView)
                mView.findViewById(R.id.textview_congratulations);
        if (textView != null) {
            if (sUserName == null) {
                sUserName = "";
                Log.w(TAG,
                        "UserName could not be retrieved for some reason from HiCloud login Activity");
            }
            final String congratulations = getString(R.string.str_hicloud_congratulations, sUserName);
            textView.setText(congratulations);
        }
    }

    private static class ButtonLogInListener implements View.OnClickListener {
        private HiCloudFragment mHiCloudFragment;
        
        
        /**
         * @param mHiCloudFragment
         */
        public ButtonLogInListener(HiCloudFragment mHiCloudFragment) {
            this.mHiCloudFragment = mHiCloudFragment;
        }


        public void onClick(View v) {
            // start login/register to HiCloud activity
            final Intent intent = new
                    Intent("com.hisense.hitv.hicloud.account.SIGNON");
            intent.putExtra("key", "aaa");
            // if need to send broadcast after login successfully, you
            // can use this value to indicate broadcast action
            intent.putExtra("BackAction", "bbb");
            // AppKey and appSecret
            // should be given by HiCloud.
            intent.putExtra("AppKey", "1217047798");
            intent.putExtra("AppSecret", "1wofof2j2fi2u642vvc39k82z4z3g72z");

            mHiCloudFragment.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private static class ButtonLogOutListener implements View.OnClickListener {
        private HiCloudFragment mHiCloudFragment;
        
        
        /**
         * @param mHiCloudFragment
         */
        public ButtonLogOutListener(HiCloudFragment mHiCloudFragment) {
            this.mHiCloudFragment = mHiCloudFragment;
        }


        public void onClick(View v) {
            mHiCloudFragment.loadLoggedOutView();
            sState = STATE_LOGGED_OUT;
        }
    }
}
