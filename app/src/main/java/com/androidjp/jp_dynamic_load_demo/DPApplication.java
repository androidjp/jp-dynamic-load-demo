package com.androidjp.jp_dynamic_load_demo;

import android.app.Application;
import android.content.Context;

import com.morgoo.droidplugin.PluginHelper;

/**
 * Droid Plugin 配置 Application
 * Created by JP on 2016/7/3.
 */
public class DPApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里必须在super.onCreate方法之后，顺序不能变
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }

}
