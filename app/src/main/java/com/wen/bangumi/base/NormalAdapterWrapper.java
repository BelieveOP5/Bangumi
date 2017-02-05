package com.wen.bangumi.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by BelieveOP5 on 2017/2/4.
 */

public class NormalAdapterWrapper<T extends QuickAdapter> extends RecyclerView.Adapter<QuickAdapter.VH>{

    enum ITEM_TYPE {
        HEADER,
        FOOTER,
        NORMAL
    }

    private T mAdapter;
    private View mHeaderView;
    private View mFooterView;

    public NormalAdapterWrapper(T adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.HEADER.ordinal();
        } else if (position == mAdapter.getItemCount() + 1) {
            return ITEM_TYPE.FOOTER.ordinal();
        } else {
            return ITEM_TYPE.NORMAL.ordinal();
        }
    }

    @Override
    public QuickAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.HEADER.ordinal()) {
            return new QuickAdapter.VH(mHeaderView);
        } else if (viewType == ITEM_TYPE.FOOTER.ordinal()) {
            return new QuickAdapter.VH(mFooterView);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(QuickAdapter.VH holder, int position) {
        if (position == 0) {
            return;
        } else if (position == mAdapter.getItemCount() + 1) {
            return;
        } else {
            mAdapter.onBindViewHolder(holder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    public void addHeaderView(View view) {
        this.mHeaderView = view;
    }

    public void addFooterView(View view) {
        this.mFooterView = view;
    }

}
