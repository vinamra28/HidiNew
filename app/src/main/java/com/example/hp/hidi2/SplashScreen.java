package com.example.hp.hidi2;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;

import retrofit2.http.POST;

public class SplashScreen extends AppCompatActivity
{
    GPSTracker gps;
    SessionManager session;
    private static int SPLASH_TIME_OUT = 1000;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission= Manifest.permission.ACCESS_FINE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        gps=new GPSTracker(this);
        session=new SessionManager(getApplicationContext());
        try
        {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != MockPackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{mPermission},REQUEST_CODE_PERMISSION);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.saveLoc(gps.latitude,gps.longitude);
        new Handler().postDelayed(new Runnable()
        {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run()
            {
                // This method will be executed once the timer is over
                // Start your app main activity
//                session.checkLogin();
                if(!session.isLoggedIn())
                {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(SplashScreen.this, PostActivity.class);
                    startActivity(intent);
                    finish();
                }
                // close this activity

            }
        }, SPLASH_TIME_OUT);
    }
}
