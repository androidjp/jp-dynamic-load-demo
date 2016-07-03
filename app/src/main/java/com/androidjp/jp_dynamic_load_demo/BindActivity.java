package com.androidjp.jp_dynamic_load_demo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * 封装了Data Binding 的Activity
 *
 * Created by JP on 2016/7/2.
 */
public abstract class BindActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBindData();
    }


    //1:从view引出一个连接器
    //2：用连接器连上想要连上的数据

    /**
     *
     */
    protected abstract void viewBindData();

}
