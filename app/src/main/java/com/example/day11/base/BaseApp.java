package com.example.day11.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.ResourceBundle;

//1个应用有几个上下文: Activity数量 + Service数量 + 1
public class BaseApp extends Application {

    public static Context sContext;

    public static Resources getRes() {
        return sContext.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
