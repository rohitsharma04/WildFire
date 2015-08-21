package com.bitshifter.wildfire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initializeEverything();

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getApplicationContext(), ChooseUserActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }

    private void initializeEverything() {
        //Activating GPS
        GPSLocator gps = new GPSLocator(this);
        if(!gps.isGPSEnabled){
            gps.showSettingsAlert();
        }
        //Generating DB
        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
