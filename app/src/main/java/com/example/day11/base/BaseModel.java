package com.example.day11.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseModel {
    CompositeDisposable compositeDisposable=null;
    public void addDisposable(Disposable disposable){
        if (compositeDisposable==null){
            compositeDisposable=new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
    public void onDestroy(){
        if (compositeDisposable!=null)
        compositeDisposable.clear();
    }
}
