package com.wen.bangumi.base;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 能快速创建基础的RecyclerViewAdapter，不带有HeaderView和FooterView
 * Created by BelieveOP5 on 2017/2/4.
 */

public abstract class QuickAdapter<T> extends RecyclerView.Adapter<QuickAdapter.VH> {

    private List<T> datas;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void replaceData(List<T> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

    public void replaceData(T data) {
    }

    public QuickAdapter(List<T> datas) {
        this.datas = datas;
    }

    /**
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.get(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        convert(holder, datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 实现BindViewHolder操作，如果需要，还可以实现onItemClick的具体功能
     * @param holder
     * @param data
     * @param position
     */
    public abstract void convert(VH holder, T data, int position);

    public static class VH extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;
        private View mConvertView;

        public VH(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutId) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new VH(convertView);
        }

        public <T extends View> T getView(int id) {
            View v = mViews.get(id);
            if (v == null) {
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T)v;
        }

        public void setText(int id, String value) {
            TextView view = getView(id);
            view.setText(value);
        }

        public void setBtnText(int id, String value) {
            Button btn = getView(id);
            btn.setText(value);
        }

        public void setImage(int id, String url) {

            ImageView imageView = getView(id);

            Picasso.with(mConvertView.getContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .transform(new ImageTransformation())
                    .into(imageView);

        }

    }

}
