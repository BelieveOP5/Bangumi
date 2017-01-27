package com.wen.bangumi.calendaritem;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    /**
     * 一日的番剧放送列表
     */
    private List<BangumiItem> mBangumiItemList;

    private Context mContext;

    /**
     * 构造函数，初始化mBangumiItemList
     * @param mBangumiItemList
     */
    public RecyclerViewAdapter(List<BangumiItem> mBangumiItemList,
                               @NonNull Context mContext) {
        setList(mBangumiItemList);
        this.mContext = checkNotNull(mContext, "mContext cannot be null!");
    }

    public void replaceData(List<BangumiItem> mBangumiItemList) {
        setList(mBangumiItemList);
        /**
         * 提醒observers中数据已经发生了改变，相应的RecyclerView需要发生改变
         */
        notifyDataSetChanged();
    }

    private void setList(List<BangumiItem> mBangumiItemList) {
        this.mBangumiItemList = checkNotNull(mBangumiItemList);
    }

    /**
     * 当RecyclerView需要创建一个指定类型的新的{@link ViewHolder}来具体的表示一个Item的时候会被调用
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //获取新的ViewHolder需要将界面显示在哪个View上
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailycalendar_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 当RecyclerView需要将某个position上面的数据显示出来的时候，调用这个方法
     * 这个方法应该实现：更新在某个position中{@link ViewHolder#itemView}的内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BangumiItem item = mBangumiItemList.get(position);

        /**
         * 设置mTextView的文本
         */
        holder.mTextView.setText(item.getName_cn());

        /**
         * 设置从网上获取到的图像
         */
        Picasso.with(mContext)
                .load(item.getLarge_image())
                .config(Bitmap.Config.RGB_565)
//                .transform(new Transfrom())
                .into(holder.mImageView);
    }

    /**
     * 获取Adapter中所有的数据的数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mBangumiItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.itemImage);
            mTextView = (TextView) itemView.findViewById(R.id.itemTitle);

        }

    }

    /**
     * 进行图片变换，并回收之前的大图，防止OOM（内存溢出）
     */
    public static class Transfrom implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {

            //原图不可为空
            checkNotNull(source, "source cannot be null!");

            //进行一半的缩小
            Bitmap result = source.createScaledBitmap(
                    source,
                    source.getWidth(),
                    source.getHeight(),
                    false);

            //回收原图
            source.recycle();

            return result;
        }

        @Override
        public String key() {
            return "Half_Transfrom";
        }

    }
}