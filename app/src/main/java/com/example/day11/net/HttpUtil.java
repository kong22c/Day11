package com.example.day11.net;

import androidx.constraintlayout.widget.Constraints;

import com.example.day11.base.Constants;
import com.example.day11.util.LogUtil;
import com.example.day11.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//网络框架
//Rxjava + Retrofit +Okhttp
public class HttpUtil {
    private static volatile HttpUtil mhttpUtil=null;
    private Retrofit.Builder mbuilder;


    private HttpUtil(){
        //初始化ok
        OkHttpClient okHttpClient = initOkHttp();
        //初始Retrofit
        initRetrofit(okHttpClient);
    }

    private void initRetrofit(OkHttpClient okHttpClient) {
        mbuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    }

    private OkHttpClient initOkHttp() {
        //设置本地缓存文件
        File cacheFile = new File(Constants.PATH_CACHE);
        ////设置缓存文件大小
        //        //1 M = 1024Kb = 1024 * 1024 byte
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new LogInterceptor())//日志拦截器
                .addInterceptor(new MyCacheinterceptor())
                .addNetworkInterceptor(new MyCacheinterceptor())
                .cache(cache)
                //设置写入时间
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置错误重连
                .retryOnConnectionFailure(true)
                .build();
        return client;
    }

    public static HttpUtil getInstance(){
        if (mhttpUtil==null){
            synchronized (HttpUtil.class){
                if (mhttpUtil==null){
                    mhttpUtil=new HttpUtil();
                }
            }
        }
        return mhttpUtil;
    }
    //简单日志拦截器
    class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //发起的网络请求对象
            Request request = chain.request();
            //发起网络请求
            Response reponse = chain.proceed(request);
            //reponse.body().string() 只能使用1次
            //String data = reponse.body().string();
            String data = reponse.peekBody(2048).string();
            LogUtil.print(data);
            return reponse;
        }
    }

    //日志拦截器
    public class LoggingInterceptor implements Interceptor {
        private static final String TAG = "LoggingInterceptor";

        @Override
        public Response intercept(Chain chain) throws IOException {
            //通过系统时间的差打印接口请求的信息
            //1s = 1000ms = 1000 * 1000 us = 1000* 1000 *1000ns
            long time = System.nanoTime();
            Request request = chain.request();
            StringBuilder sb = new StringBuilder();
            if ("GET".equals(request.method())) { // GET方法
                HttpUrl httpUrl = request.url().newBuilder().build();

                sb.append("GET,");
                // 打印所有get参数
                Set<String> paramKeys = httpUrl.queryParameterNames();
                for (String key : paramKeys) {
                    String value = httpUrl.queryParameter(key);
                    sb.append(key + ":" + value + ",");
                }

            } else if ("POST".equals(request.method())) { // POST方法
                sb.append("POST,");
                // FormBody和url不太一样，若需添加公共参数，需要新建一个构造器
                /*FormBody.Builder bodyBuilder = new FormBody.Builder();
                // 把已有的post参数添加到新的构造器
                if (request.body() instanceof FormBody) {
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                }

                // 这里可以添加一些公共post参数
                bodyBuilder.addEncoded("key_xxx", "value_xxx");
                FormBody newBody = bodyBuilder.build();

                // 打印所有post参数
                for (int i = 0; i < newBody.size(); i++) {
                    Log.d("TEST", newBody.name(i) + " " + newBody.value(i));
                }*/

                if (request.body() instanceof FormBody) {
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        sb.append(formBody.name(i) + ":" + formBody.value(i) + ",");
                    }
                }

            }

            LogUtil.logD("Request:", String.format("Sending request %s %n %s %n%s", request.url(), sb.toString(), request.headers()));
            Response response = chain.proceed(request);
            long now = System.nanoTime();
            LogUtil.logD("Received:", String.format("Received response for %s in %.1fms%n%s", response.request().url(), (now - time) / 1e6d, response.headers()));

            LogUtil.logD("Data:", response.peekBody(4096).string());
            return response;
        }
    }

    /**
     * 请求的修改设置
     */
    public static class HeadersInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //sputil :shareprefrences
            //String token = (String) SpUtil.getParam(Constants.TOKEN,"");
            //LogUtils.print("token:"+token);
            Request request = chain.request().newBuilder()
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader("", "")
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 固定模板
     */
    private class MyCacheinterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //这里就是说判读我们的网络条件，要是有网络的话我么就直接获取网络上
            // 面的数据，要是没有网络的话我么就去缓存里面取数据
            if (!SystemUtil.isNetworkConnected()) {
                request = request
                        .newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//强制使用缓存
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (SystemUtil.isNetworkConnected()) {
                int maxAge = 0;
                // 有网络时, 不缓存, 最大保存时长为0
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                //s秒
                int maxStale = 60 * 60 * 24;
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        //这里的设置的是我们的没有网络的缓存时间，
                        // 想设置多少就是多少。
                        .header("Cache-Control", "public, only-if-cached," +
                                " max-age=" + maxStale)
                        .build();
            }

        }
    }
    private static volatile ApiService mapiService=null;
    public  ApiService getMapiService(){
        if (mapiService==null){
            synchronized (ApiService.class){
                if (mapiService==null){
                    mapiService=mbuilder.baseUrl(ApiService.BASS)
                            .build()
                            .create(ApiService.class);

                }
            }
        }
        return mapiService;
    }
}
