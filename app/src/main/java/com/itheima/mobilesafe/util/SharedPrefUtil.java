package com.itheima.mobilesafe.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class SharedPrefUtil {
    private static SharedPreferences mSP;

    private static SharedPreferences getSharedPref(Context context) {
        if (mSP == null) {
            mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return mSP;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPref(context).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPref(context).getBoolean(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        return getSharedPref(context).getString(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getSharedPref(context).edit().putString(key, value).commit();
    }


    public static void removeString(Context context, String key) {
        getSharedPref(context).edit().remove(key).commit();
    }

    public static void removeBoolean(Context context, String key) {
        getSharedPref(context).edit().remove(key).commit();
    }


}
