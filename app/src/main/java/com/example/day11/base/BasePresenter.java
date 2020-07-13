package com.example.day11.base;

import java.util.ArrayList;

public abstract class BasePresenter<V extends BaseView> {
    public V mview;
    private ArrayList<BaseModel>baseModels;
    public BasePresenter(){
        initModel();
    }

    protected abstract void initModel();
    public void addModel(BaseModel baseModel){
        if (baseModels==null){
            baseModels=new ArrayList<>();
        }
        baseModels.add(baseModel);
    }
    public void bindView(V view){
      mview=view;
    }
    public void onDestroy(){
        if (baseModels.size()>0){
            for (int i = 0; i <baseModels.size() ; i++) {
                baseModels.get(i).onDestroy();
            }
            mview=null;
        }
    }
}
