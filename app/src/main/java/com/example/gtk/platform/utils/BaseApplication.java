package com.example.gtk.platform.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by gutia on 2017-06-17.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        Fresco.initialize(this);
        super.onCreate();
    }
}
