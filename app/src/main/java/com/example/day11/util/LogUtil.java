package com.example.day11.util;

import android.util.Log;

import com.example.day11.base.Constants;


public class LogUtil {
    public static void print(String msg) {
        if (Constants.isDebug) {
            System.out.println(msg);
        }
    }

    public static void logD(String tag, String msg) {
        if (Constants.isDebug) {
            Log.d(tag, msg);
        }
    }

}
