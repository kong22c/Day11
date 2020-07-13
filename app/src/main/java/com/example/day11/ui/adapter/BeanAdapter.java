package com.example.day11.ui.adapter;

import android.content.Context;

import com.example.day11.R;
import com.example.day11.base.BaseRlvAdapter;
import com.example.day11.bean.Bean;

import java.util.ArrayList;

public class BeanAdapter extends BaseRlvAdapter<Bean.DataBean.DatasBean> {
    public BeanAdapter(Context context, ArrayList<Bean.DataBean.DatasBean> list1) {
        super(context, list1);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_bean;
    }

    @Override
    protected void bindData(BaseViewHolder holder, Bean.DataBean.DatasBean datasBean) {
        //这样写可以,但是不好,效率特别低
        //View viewById = holder.itemView.findViewById(R.id.tv);
        /*TextView view = (TextView) holder.findView(R.id.tv);
        view.setText(o.getName());*/
        holder.setText(R.id.tv_desc,datasBean.getDesc());

    }
}
