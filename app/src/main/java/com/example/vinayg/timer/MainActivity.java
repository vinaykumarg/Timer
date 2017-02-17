package com.example.vinayg.timer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private TextView mTimer1TextView,mTimer2TextView;
    private Fragment fragment1;
    private Fragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer1TextView = (TextView)findViewById(R.id.tv1);
        mTimer2TextView = (TextView)findViewById(R.id.tv2);
        mTimer1TextView.setOnClickListener(this);
        mTimer2TextView.setOnClickListener(this);
        fragment1 = new Timer1Fragment();
        fragment2 = new Timer2Fragment();
        showFragment(fragment1);
        mTimer1TextView.setBackgroundResource(R.drawable.textlines);
        mTimer2TextView.setBackgroundResource(R.drawable.nolines);
        mFragmentManager();
    }
    private void mFragmentManager(){
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragmentSpace);
                if (f != null){
                    updateTitleAndDrawer (f);
                }

            }
        });
    }

    private void updateTitleAndDrawer (Fragment fragment){
        String fragClassName = fragment.getClass().getName();

        if (fragClassName.equals(Timer1Fragment.class.getName())){
            setTitle ("A");
            //set selected item position, etc
        }
        else if (fragClassName.equals(Timer2Fragment.class.getName())){
            setTitle ("B");
            //set selected item position, etc
        }
    }

    private void showFragment(Fragment myFragment ){
        String backStateName =  myFragment.getClass().getName();
        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        FragmentTransaction ft = manager.beginTransaction();
        backStateName = myFragment.getClass().getName();
        if (!fragmentPopped) {
            ft.replace(R.id.fragmentSpace, myFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(backStateName);
        ft.commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                showFragment(fragment1);
                mTimer1TextView.setBackgroundResource(R.drawable.textlines);
                mTimer2TextView.setBackgroundResource(R.drawable.nolines);
                break;
            case R.id.tv2:
                showFragment(fragment2);
                mTimer2TextView.setBackgroundResource(R.drawable.textlines);
                mTimer1TextView.setBackgroundResource(R.drawable.nolines);
                break;
        }
    }

}
