package com.example.vinayg.timer;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class T1Service extends Service {
    private MyCountDownTimer cdt;
    private Intent bi;
    private static final String TAG = T1Service.class.getName();

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        bi = new Intent("Timer1");
        cdt = new MyCountDownTimer(30*1000);
        cdt.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cdt.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind()");
        return null;
    }
    public class MyCountDownTimer extends CountDownTimer {


        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, (long) 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
            bi.putExtra("countdown", millisUntilFinished);
            sendBroadcast(bi);
        }

        @Override
        public void onFinish() {
            Log.i(TAG, "Timer1 finished");
            Toast.makeText(getApplicationContext(),"Timer1 finished",Toast.LENGTH_SHORT).show();
            bi.putExtra("Timeup", "Time is up!");
            sendBroadcast(bi);
            stopSelf();
        }


    }
}
