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

public class Timer1Fragment extends Fragment{
    private static final String TAG = Timer1Fragment.class.getName();
    TextView mTextView;
    Button mStartButton,mResetButton;
    ProgressBar mProgressBar;
    private boolean mBoolean = false;
    private Intent mIntent;
    private SharedPreferences pref; // 0 - for private mode
    private SharedPreferences.Editor editor;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);// or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = getActivity().getSharedPreferences("MyPref1", 0); // 0 - for private mode
        editor = pref.edit();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            if (intent.getStringExtra("Timeup")==null) {
                long millisUntilFinished = intent.getLongExtra("countdown", 0);
                int output = (int) (millisUntilFinished / 1000);
//                Log.i(TAG, "Countdown seconds remaining: " + output);
                mTextView.setText(Long.toString(output));
                mProgressBar.setProgress(mProgressBar.getMax()-output);
            } else {
                mProgressBar.setProgress(mProgressBar.getMax());
                mTextView.setText(getString(R.string.timeup));
                mBoolean = false;
                mIntent=null;
                editor.putBoolean("ISrunning", mBoolean); // Storing boolean - true/false
                editor.putString("Text", getString(R.string.timeup)); // Storing string
                editor.putInt("Progress", mProgressBar.getMax()); // Storing integer
                editor.commit();
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer1, container, false);
        mTextView = (TextView) v.findViewById(R.id.textView);
        mStartButton = (Button) v.findViewById(R.id.start1);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIntent==null) {
                    mIntent = new Intent(getActivity(), T1Service.class);
                    mBoolean = true;
                    getActivity().startService(mIntent);
                }
            }
        });
        mResetButton = (Button) v.findViewById(R.id.reset1);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIntent!=null) {
                    getActivity().stopService(mIntent);
                }
                mTextView.setText("");
                mProgressBar.setProgress(0);
            }
        });
        mProgressBar = (ProgressBar) v.findViewById(R.id.ProgressBar1);
        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        mTextView.setText(pref.getString("Text", "")); // getting String
        mProgressBar.setProgress(pref.getInt("Progress", 0)); // getting Integer
        pref.getBoolean("ISrunning", false); // getting boolean
        getActivity().registerReceiver(br,new IntentFilter("Timer1"));
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
        editor.putBoolean("ISrunning", mBoolean); // Storing boolean - true/false
        editor.putString("Text", mTextView.getText().toString()); // Storing string
        editor.putInt("Progress", mProgressBar.getProgress()); // Storing integer
        editor.commit(); // commit changes

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
        editor.clear();
        editor.commit(); // commit changes
        Log.d(TAG,"unreg");
        getActivity().unregisterReceiver(br);
    }

}
