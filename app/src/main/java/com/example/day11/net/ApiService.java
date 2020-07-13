package com.example.day11.net;

import com.example.day11.bean.Bean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface ApiService {
    String BASS="https://www.wanandroid.com/";
    @GET("project/list/1/json?cid=294")
    Flowable<Bean>getBean();
}
