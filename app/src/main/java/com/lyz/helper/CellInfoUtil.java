package com.lyz.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyz on 2018/8/8.
 *
 */

public class CellInfoUtil {

    private static final int PERMISSIONS_CODE = 1024;

    /**
     * 获取手机IMEI号
     *
     * @param activity
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getImei(Activity activity) {
        if (!PermissionUtil.hasPermissions(activity, Manifest.permission.READ_PHONE_STATE)) {
            PermissionUtil.requestPermissions(activity, PERMISSIONS_CODE, Manifest.permission.READ_PHONE_STATE);
            return null;
        } else {
            TelephonyManager mTm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTm) {
                return mTm.getDeviceId() == null ? null : mTm.getDeviceId().replace(" ", "");
            } else {
                return null;
            }
        }
    }

    /**
     * 获取手机IMSI号
     *
     * @param activity
     * @return
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImsi(Activity activity) {
        if (!PermissionUtil.hasPermissions(activity, Manifest.permission.READ_PHONE_STATE)) {
            PermissionUtil.requestPermissions(activity, PERMISSIONS_CODE, Manifest.permission.READ_PHONE_STATE);
            return null;
        } else {
            TelephonyManager mTm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTm) {
                return mTm.getSubscriberId() == null ? null : mTm.getSubscriberId().replace(" ", "");
            } else {
                return null;
            }
        }
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileType() {
        String type = android.os.Build.MODEL;
        return type.replace(" ", "");
    }

    /**
     * 获取手机号码 （有的手机可以获取到有的获取不到）
     *
     * @param activity
     * @return
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getPhoneNum(Activity activity) {
        if (!PermissionUtil.hasPermissions(activity, Manifest.permission.READ_PHONE_STATE)) {
            PermissionUtil.requestPermissions(activity, PERMISSIONS_CODE, Manifest.permission.READ_SMS);
            return null;
        } else {
            TelephonyManager mTm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTm) {
                return mTm.getLine1Number() == null ? null : mTm.getLine1Number().replace(" ", "");
            } else {
                return null;
            }
        }

    }

    /**
     * Role:Telecom service providers获取手机服务商信息
     * 需要加入权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getProvidersName(Activity activity) {

        if (!PermissionUtil.hasPermissions(activity, Manifest.permission.READ_PHONE_STATE)) {
            PermissionUtil.requestPermissions(activity, PERMISSIONS_CODE, Manifest.permission.READ_PHONE_STATE);
            return null;
        } else {
            TelephonyManager mTm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != mTm) {
                String IMSI = mTm.getSubscriberId();
                if (TextUtils.isEmpty(IMSI)) {
                    return null;
                } else {
                    if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                        return "中国移动";
                    } else if (IMSI.startsWith("46001")) {
                        return "中国联通";
                    } else if (IMSI.startsWith("46003")) {
                        return "中国电信";
                    }
                }
                return mTm.getSubscriberId() == null ? null : mTm.getSubscriberId().replace(" ", "");
            } else {
                return null;
            }
        }
    }

    /**
     * 获取app的版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取app的versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }

    /**
     * 判断手机号码的运营商
     *
     * @param phone_number
     * @return
     */
    public static int matchesPhoneNumber(String phone_number) {

        String cm = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}$";
        String cu = "^((13[0-2])|(145)|(15[5,6])|(18[5,6])|(176))\\d{8}$";
        String ct = "^((133)|(153)|(18[0,1,9])|(173)|(177))\\d{8}$";

        int flag = 0;
        if (phone_number.matches(cm)) {
            //移动
            flag = 2;
        } else if (phone_number.matches(cu)) {
            //联通
            flag = 3;
        } else if (phone_number.matches(ct)) {
            //电信
            flag = 1;
        } else {
            flag = 4;
        }
        return flag;
    }

    public static List<AppInfoBean> getAllApk(Context context) {
        List<AppInfoBean> appBeanList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            AppInfoBean bean = new AppInfoBean();
            bean.setIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
            bean.setPkgName(p.applicationInfo.packageName);
            bean.setPath(p.applicationInfo.sourceDir);
            bean.setVersionName(p.versionName);
            bean.setVersionCode(p.versionCode);
            File file = new File(p.applicationInfo.sourceDir);
            bean.setSize((int) file.length());
            int flags = p.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                bean.setFlag(0);
            } else {
                bean.setFlag(1);
            }
            appBeanList.add(bean);
        }
        return appBeanList;
    }

    public static List<AppInfoBean> getSdApk(Context context) {
        List<AppInfoBean> appBeanList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            int flags = p.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            AppInfoBean bean = new AppInfoBean();
            bean.setIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
            bean.setPkgName(p.applicationInfo.packageName);
            bean.setPath(p.applicationInfo.sourceDir);
            bean.setVersionName(p.versionName);
            bean.setVersionCode(p.versionCode);
            bean.setUpdateTime(p.lastUpdateTime);
            File file = new File(p.applicationInfo.sourceDir);
            bean.setSize((int) file.length());
            bean.setFlag(1);
            appBeanList.add(bean);
        }
        return appBeanList;
    }


    /**
     * 安装apk
     * @param context
     * @param fileName
     */
    public static void installApk(Context context, String fileName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + fileName),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

}
