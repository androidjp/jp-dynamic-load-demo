package com.androidjp.jp_dynamic_load_demo.bean;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Apk项的条目
 * 包含的apk信息：图标、标题、版本名称、版本号、apk路径、包信息（包含application信息（包含source路径等）等）
 * @author wangchenlong
 */
public class ApkItem {

    public Drawable icon; // 图标
    public CharSequence title; // 标题
    public String versionName; // 版本名称
    public int versionCode; // 版本号
    public String apkFile; // Apk路径
    public PackageInfo packageInfo; // 包信息

    public ApkItem(PackageManager pm, PackageInfo pi, String path) {

        // 必须设置, 否则title无法获取
        pi.applicationInfo.sourceDir = path;
        pi.applicationInfo.publicSourceDir = path;

        //设置完成后，packageInfo即有了对象的路径（资源路径和公共资源路径）

        try {
            //通过：包中的应用信息 --> packageManager --> 获取应用Icon
            icon = pm.getApplicationIcon(pi.applicationInfo);
        } catch (Exception e) {
            icon = pm.getDefaultActivityIcon();
        }
        try {
            title = pm.getApplicationLabel(pi.applicationInfo);
        } catch (Exception e) {
            title = pi.packageName;
        }
        versionName = pi.versionName;
        versionCode = pi.versionCode;
        apkFile = path;
        packageInfo = pi;
    }
}