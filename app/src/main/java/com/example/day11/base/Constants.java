package com.example.day11.base;

import java.io.File;

//常量接口
public interface Constants {
    boolean isDebug = true;

    //网络缓存的地址
    String PATH_DATA = BaseApp.sContext.getCacheDir().getAbsolutePath() +
            File.separator + "data";

    String PATH_CACHE = PATH_DATA + "/NetCache";
    String DATA = "data";

}
