package com.androidjp.jp_dynamic_load_demo.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.widget.Toast;

import com.androidjp.jp_dynamic_load_demo.bean.ApkItem;
import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;

/**
 * Apk操作, 包含删除\安装\卸载\启动Apk
 * <p>
 * Created by wangchenlong on 16/1/13.
 */
public class ApkOperator {

    public static final int TYPE_STORE = 0; // 存储Apk
    public static final int TYPE_START = 1; // 启动Apk

    private Activity mActivity;       // 绑定Dialog
    private RemoveCallback mCallback; // 删除Item的回调

    public ApkOperator(Activity activity, RemoveCallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    // 删除Apk
    public void deleteApk(final ApkItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("警告");
        builder.setMessage("你确定要删除" + item.title + "么？");
        builder.setNegativeButton("删除", (dialog, which) -> {
            if (new File(item.apkFile).delete()) {
                mCallback.removeItem(item);
                Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
            }
        });

//        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (new File(item.apkFile).delete()) {
//                    mCallback.removeItem(item);
//                    Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        builder.setNeutralButton("取消", null);
        builder.show();
    }

    /**
     * 安装Apk, 耗时较长, 需要使用异步线程
     *
     * @param item Apk项
     * @return [0:成功, 1:已安装, -1:连接失败, -2:权限不足, -3:安装失败]
     */
    public String installApk(final ApkItem item) {
        if (!PluginManager.getInstance().isConnected()) {
            return "连接失败"; // 连接失败
        }

        if (isApkInstall(item)) {
            return "已安装"; // 已安装
        }

        try {
            int result = PluginManager.getInstance().installPackage(item.apkFile, 0);
            boolean isRequestPermission = (result == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION);
            if (isRequestPermission) {
                return "权限不足";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return "安装失败";
        }

        return "成功";
    }

    // Apk是否安装
    private boolean isApkInstall(ApkItem apkItem) {
        PackageInfo info = null;
        try {
            /*看其是否能够获取到对应的插件包信息，可获取到，则已安装*/
            info = PluginManager.getInstance().getPackageInfo(apkItem.packageInfo.packageName, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info != null;
    }

    // 卸载Apk
    public void uninstallApk(final ApkItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("警告");
        builder.setMessage("警告，你确定要卸载" + item.title + "么？");
        builder.setNegativeButton("卸载", (dialog, which) -> {
            if (!PluginManager.getInstance().isConnected()) {
                Toast.makeText(mActivity, "服务未连接", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    PluginManager.getInstance().deletePackage(item.packageInfo.packageName, 0);
                    mCallback.removeItem(item);
                    Toast.makeText(mActivity, "卸载完成", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNeutralButton("取消", null);
        builder.show();
    }

    // 打开Apk
    public void openApk(final ApkItem item) {
        PackageManager pm = mActivity.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(item.packageInfo.packageName);//获取插件应用的入口Intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//以singleTask的模式启动插件的入口Activity
        mActivity.startActivity(intent);
    }

    // 删除Item回调, Adapter调用删除Item
    public interface RemoveCallback {
        void removeItem(ApkItem apkItem);
    }
}
