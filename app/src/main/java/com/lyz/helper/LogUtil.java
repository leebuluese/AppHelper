package com.lyz.helper;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志工具类
 */

public class LogUtil {
    private static boolean bLog = true;
    private static final String TAG = "sf_log";
    public static final String TAG_REQUEST = "sf_request";
    public static final String TAG_TEST = "sf_test";
    public static final String TAG_PUSH = "sf_push";
    public static final String TAG_ADD_SHOPCAR = "sf_add_shopcar";
    public static final String TAG_ORDERLIST = "sf_order_list";
    public static final String TAG_AUTO_LOGIN = "sf_auto_login";
    public static final String TAG_CUT_DOWN = "sf_cut_down";
    public static final String TAG_WEIXIN_LOGIN = "weixin_login";

    private LogUtil(){
        //only for sonar!
    }

    public static void d(String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.d(TAG, msg);

        }

    }

    public static void d(String Tag, String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.d(Tag, msg);

        }

    }

    public static void e(String Tag, String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.d(Tag, msg);

        }

    }

    public static void i(String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.i(TAG, msg);

        }

    }

    public static void e(String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.e(TAG, msg);

        }

    }

    public static void e(Throwable e) {
        if (!bLog || null == e)
            return;
        Log.e("Sfbest Error", "err", e);
    }

    public static void v(String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.v(TAG, msg);

        }

    }

    public static void w(String msg) {

        if (bLog && !TextUtils.isEmpty(msg)) {

            Log.w(TAG, msg);

        }

    }

    public static void logRequestParam(Object object) {
        try {
            Class<? extends Object> cls = object.getClass();
            java.lang.reflect.Field[] flds = cls.getFields();
            if (flds != null) {
                for (int i = 0; i < flds.length; i++) {
                    d(TAG_REQUEST, flds[i].getName() + " = " + flds[i].get(object));
                }
            }
        } catch (IllegalAccessException e) {
            LogUtil.e(e);
        } catch (IllegalArgumentException e) {
            LogUtil.e(e);
        }
    }

}
