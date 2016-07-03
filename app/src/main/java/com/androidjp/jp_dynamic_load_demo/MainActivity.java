package com.androidjp.jp_dynamic_load_demo;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidjp.jp_dynamic_load_demo.controllers.PagerAdapter;
import com.androidjp.jp_dynamic_load_demo.utils.PluginConsts;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String[] TITLES = {
      "已安装", "未安装"
    };

    @Bind(R.id.tl_main)
    TabLayout mTlTabs; // Tabs
    @Bind(R.id.vp_main)
    ViewPager mVpContainer; // ViewPager
    @Bind(R.id.btn_main_goto)
    Button mBGoto; // 跳转插件的按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        mVpContainer.setAdapter(adapter);
        mTlTabs.setupWithViewPager(mVpContainer);
        mBGoto.setOnClickListener(this::gotoPlugin);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(PluginConsts.MASTER_EXTRA_STRING) != null) {
            String words = "say: " + intent.getStringExtra(PluginConsts.MASTER_EXTRA_STRING);
            Toast.makeText(this, words, Toast.LENGTH_SHORT).show();
        }
    }

    // 跳转控件
    private void gotoPlugin(View view) {
        if (isActionAvailable(view.getContext(), PluginConsts.PLUGIN_ACTION_MAIN)) {
            Intent intent = new Intent(PluginConsts.PLUGIN_ACTION_MAIN);
            intent.putExtra(PluginConsts.PLUGIN_EXTRA_STRING, "Hello, My Plugin!");
            startActivity(intent);
        } else {
            Toast.makeText(view.getContext(), "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    // Action是否允许
    public static boolean isActionAvailable(Context context, String action) {
        Intent intent = new Intent(action);
        return context.getPackageManager().resolveActivity(intent, 0) != null;
    }
}
