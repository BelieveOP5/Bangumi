package com.wen.bangumi.BangumiDetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wen.bangumi.R;

/**
 * Created by BelieveOP5 on 2017/2/1.
 */

public class BangumiSelectionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public BangumiSelectionRecyclerViewAdapter() {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }


}
