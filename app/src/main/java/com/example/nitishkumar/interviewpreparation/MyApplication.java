package com.example.nitishkumar.interviewpreparation;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by NITISH KUMAR on 15-06-2018.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.admod_app_id));
    }
}
