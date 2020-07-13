package com.example.day11.presenter;

import com.example.day11.base.BasePresenter;
import com.example.day11.bean.Bean;
import com.example.day11.model.BeanModel;
import com.example.day11.net.BeanCallBack;
import com.example.day11.view.BeanView;

public class BeanPresenter extends BasePresenter<BeanView> {

    private BeanModel beanModel;

    @Override
    protected void initModel() {
        beanModel = new BeanModel();
        addModel(beanModel);
    }
    public void getData(){
        beanModel.getData(new BeanCallBack<Bean>() {
            @Override
            public void onSuesss(Bean bean) {
                mview.setData(bean);
            }

            @Override
            public void onFain(String str) {
                mview.showToast(str);
            }
        });
    }
}
