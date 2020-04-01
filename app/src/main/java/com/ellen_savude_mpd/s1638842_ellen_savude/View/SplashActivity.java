// Name: Ellen Savude Ngatia
// Student ID: S1638842
package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.DataLoader;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;

//Is the splash screen activity loads the app as it initializes some threads running on the background


public class SplashActivity extends AppCompatActivity
{
    ChannelPersister p;
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                startProgress();
                finish();
            }
        }, SPLASH_TIME_OUT);

    }


    public void startProgress() {
        // Run thread to access network
        DataLoader dl = new DataLoader();
        InflatorModel channel = dl.getLoadedChannel();
        p = new ChannelPersister(channel);
      Intent  i = new Intent(SplashActivity.this, MainActivity.class);
        i.putExtra("persister",p);
        startActivity(i);

    }
}





