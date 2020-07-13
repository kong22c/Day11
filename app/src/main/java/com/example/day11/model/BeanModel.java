package com.example.day11.model;

import android.util.Log;

import com.example.day11.base.BaseModel;
import com.example.day11.bean.Bean;
import com.example.day11.net.ApiService;
import com.example.day11.net.BeanCallBack;
import com.example.day11.net.HttpUtil;
import com.example.day11.net.ResultSubscriber;
import com.example.day11.net.RxUtils;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeanModel extends BaseModel {
    public void getData(BeanCallBack<Bean>callBack){
//        ResourceSubscriber<Bean> resourceSubscriber = new Retrofit.Builder()
//                .baseUrl(ApiService.BASS)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//                .create(ApiService.class)
//                .getBean()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new ResourceSubscriber<Bean>() {
//                    @Override
//                    public void onNext(Bean bean) {
//                    callBack.onSuesss(bean);
//                        Log.i("111", "onNext:"+bean.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        callBack.onFain(t.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//            addDisposable(resourceSubscriber);
        addDisposable(HttpUtil.getInstance().getMapiService()
                .getBean()
                .compose(RxUtils.rxSchedulerHelper())
               .subscribeWith(new ResultSubscriber<Bean>() {
                   @Override
                   public void onNext(Bean bean) {
                       callBack.onSuesss(bean);
                   }
               })

        );

    }
}
