package com.example.vinayg.timer;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * Created by vinay.g.
 */

public class Timer2Fragment extends Fragment {
    private static final String TAG = Timer2Fragment.class.getName();
    TextView mTextView;
    Button mStartButton,mResetButton;
    ProgressBar mProgressBar;
    private boolean mBoolean = false;
    private Intent mStartServiceIntent = null;
    private SharedPreferences mPref; // 0 - for private mode
    private SharedPreferences.Editor mEditor;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            if (intent.getStringExtra("Timeup")==null) {
                long millisUntilFinished = intent.getLongExtra("countdown", 0);
                int output = (int) (millisUntilFinished / 1000);
                mTextView.setText(Long.toString(output));
                mProgressBar.setProgress(mProgressBar.getMax()-output);
            } else {
                mProgressBar.setProgress(mProgressBar.getMax());
                mTextView.setText(getString(R.string.timeup));
                mBoolean = false;
                mStartServiceIntent=null;
                Log.d(TAG,"timerStopped");
                mEditor.putBoolean("ISrunning", mBoolean); // Storing boolean - true/false
                mEditor.putString("Text", getString(R.string.timeup)); // Storing string
                mEditor.putInt("Progress", mProgressBar.getMax()); // Storing integer
                mEditor.commit();
            }


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer2, container, false);
        mTextView = (TextView) v.findViewById(R.id.textView2);
        mStartButton = (Button) v.findViewById(R.id.start2);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mStartServiceIntent ==null) {
                    mStartServiceIntent = new Intent(getActivity(), T2Service.class);
                    mBoolean = true;
                    getActivity().startService(mStartServiceIntent);
                    Log.d(TAG,"clicked");
                }
            }
        });
        mResetButton = (Button) v.findViewById(R.id.reset2);
        mProgressBar = (ProgressBar) v.findViewById(R.id.ProgressBar2);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStartServiceIntent !=null) {
                    getActivity().stopService(mStartServiceIntent);
                    mStartServiceIntent =null;
                }
                mTextView.setText("");
                mProgressBar.setProgress(0);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPref = getActivity().getSharedPreferences("MyPref2", 0); // 0 - for private mode
        mEditor = mPref.edit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        if(mPref !=null) {
            mTextView.setText(mPref.getString("Text", "")); // getting String
            mProgressBar.setProgress(mPref.getInt("Progress", 0)); // getting Integer
            mPref.getBoolean("ISrunning", false); // getting boolean
        }
        getActivity().registerReceiver(br,new IntentFilter("Timer2"));
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");
    }


    @Override
    public void onStop() {
        Log.d(TAG,"onStop()");
        super.onStop();
        mEditor.putBoolean("ISrunning", mBoolean); // Storing boolean - true/false
        mEditor.putString("Text", mTextView.getText().toString()); // Storing string
        mEditor.putInt("Progress", mProgressBar.getProgress()); // Storing integer
        mEditor.commit(); // commit changes
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
        mEditor.clear();
        mEditor.commit(); // commit changes
        getActivity().unregisterReceiver(br);
    }
}
