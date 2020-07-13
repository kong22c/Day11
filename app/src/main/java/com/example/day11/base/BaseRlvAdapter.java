package com.example.day11.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
//BaseRecyclerViewAdapterHelper:封装了RecyclerView的适配器，单布局和多布局的都有
//T 指定的是子条目的数据
public abstract class BaseRlvAdapter<T> extends RecyclerView.Adapter<BaseRlvAdapter.BaseViewHolder> {
    private Context context;
    private ArrayList<T>list;

    public BaseRlvAdapter(Context context, ArrayList<T> list1) {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public BaseRlvAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(getLayout(), parent, false);
        return new BaseViewHolder(inflate);
    }

    protected abstract int getLayout();


    @Override
    public void onBindViewHolder(@NonNull BaseRlvAdapter.BaseViewHolder holder, int position) {
        T t = list.get(position);
       bindData(holder,t);
       if (onItemclickLister!=null){
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   onItemclickLister.onItemClick(view,position);
               }
           });
       }
       if (onItemLongClickLister!=null){
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  onItemclickLister.onItemClick(view,position);
              }
          });
       }
    }

    protected abstract void bindData(BaseViewHolder holder, T t);
    //加载更多
    public void addData(List<T> list1) {
        if (list1 != null && list1.size() > 0) {
            list.addAll(list1);
            notifyDataSetChanged();
        }
    }

    //下拉刷新,旧的数据需要清除
    public void updateData(ArrayList<T> list1) {
        if (list1 != null && list1.size() > 0) {
            list.clear();
           list.addAll(list1);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        //Android开发的时候一般不用HashMap
        //为什么不用,因为HashMap消耗内存比较多
        //HashMap<Integer, View> mMap = new HashMap<>();

        //和hashMap一样,都是存储键值对的,但是它更轻量级,
       /* ArrayMap;
        SparseArray*/
    SparseArray<View>msa=new SparseArray<>();

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            //父类不值道最终子类子条目的布局究竟有哪些id
            //itemView.findViewById(R.id.xx);
        }
        //用来findviewBYid的
        //findViewById 查找到的控件保存到容器中,下次再调用这个方法的时候,先从容器中找,找不到再findViewByid
        public View findView(@IdRes int id) {
            View view = msa.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
               msa.put(id,view);
            }

            return view;
        }

        public void setText(@IdRes int id, String text){
            //有可能有人传过来的id不是TextView的id,这样强转的时候就崩了
            try {
                TextView view = (TextView) findView(id);
                view.setText(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private OnItemclickLister onItemclickLister;

    public void setOnItemclickLister(OnItemclickLister onItemclickLister) {
        this.onItemclickLister = onItemclickLister;
    }

    public interface OnItemclickLister{
        void onItemClick(View v,int position);
    }
    private OnItemLongClickLister onItemLongClickLister;

    public void setOnItemLongClickLister(OnItemLongClickLister onItemLongClickLister) {
        this.onItemLongClickLister = onItemLongClickLister;
    }

    public interface OnItemLongClickLister{
        void onItemLongClick(View v,int position);
    }
}
