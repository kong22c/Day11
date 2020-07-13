package com.example.day11;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day11.base.BaseActivity;
import com.example.day11.base.BaseRlvAdapter;
import com.example.day11.bean.Bean;
import com.example.day11.presenter.BeanPresenter;
import com.example.day11.ui.adapter.BeanAdapter;
import com.example.day11.view.BeanView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<BeanPresenter> implements BeanView {

    @BindView(R.id.rv)
    RecyclerView rv;
    private ArrayList<Bean.DataBean.DatasBean> list;
    private BeanAdapter adapter;


    @Override
    protected void initData() {
        mPresenter.getData();
    }

    @Override
    protected void initView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        list = new ArrayList<>();
        adapter = new BeanAdapter(this, list);
        rv.setAdapter(adapter);
        adapter.setOnItemclickLister(new BaseRlvAdapter.OnItemclickLister() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(MainActivity.this, list.get(position).getDesc(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter=new BeanPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setData(Bean bean) {
        adapter.addData(bean.getData().getDatas());
    }

    @Override
    public void showToast(String str) {

    }
}
