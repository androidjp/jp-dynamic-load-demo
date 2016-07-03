package com.androidjp.plugin_droid;

import android.app.Application;
import android.util.Log;

/**
 * Created by JP on 2016/7/3.
 */
public class PluginAppl extends Application{

    private static final String TAG = "Plugin-Droid"+"."+ PluginAppl.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate()");
    }
}
