package com.lyz.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;


public class AppInfoReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        if (null == intent.getData()) {
            return;
        }
        Uri uri = intent.getData();
        String packageName = uri.getSchemeSpecificPart();
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            LogUtil.d(TAG, "--------安装成功" + packageName);
            Toast.makeText(context, "安装成功" + packageName, Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new MessageEvent(MessageEvent.EventType.add, packageName));

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
            LogUtil.d(TAG, "--------替换成功" + packageName);
            Toast.makeText(context, "替换成功" + packageName, Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new MessageEvent(MessageEvent.EventType.replace, packageName));
        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            LogUtil.d(TAG, "--------卸载成功" + packageName);
            Toast.makeText(context, "卸载成功" + packageName, Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new MessageEvent(MessageEvent.EventType.replace, packageName));
        }
    }

} 