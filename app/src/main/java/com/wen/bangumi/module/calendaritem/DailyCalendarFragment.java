package com.wen.bangumi.module.calendaritem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wen.bangumi.base.BaseLazyFragment;
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.R;
import com.wen.bangumi.module.bangumidetail.BangumiDetailActivity;
import com.wen.bangumi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by BelieveOP5 on 2017/1/16.
 */

public class DailyCalendarFragment extends BaseLazyFragment<DailyCalendarContract.Presenter> implements DailyCalendarContract.View {

    private WeekDay weekday;

    public void setDate(WeekDay weekday) {
        this.weekday = weekday;
    }

    @BindView(R.id.bangumiList)
    public RecyclerView recyclerView;
    private QuickAdapter<BangumiItem> adapter;

    @BindView(R.id.noBangumi)
    public View noBangumiView;

    /**
     * 实例化一个DailyCalendarFragment，并返回
     * @param date 该DailyCalendarFragment是属于哪一个日期的
     * @return
     */
    public static DailyCalendarFragment newInstance(WeekDay date) {
        Bundle args = new Bundle();
        DailyCalendarFragment fragment = new DailyCalendarFragment();
        fragment.setDate(date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.daily_calendar_frag;
    }

    @Override
    protected void initViews(View view) {

        initSwipeRefreshLayout(view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        showBangumiView();

        isPrepared = true;

    }

    /**
     * 初始化RecyclerView的adapter
     */
    @Override
    protected void initAdapter() {

        adapter = new QuickAdapter<BangumiItem>(new ArrayList<BangumiItem>()) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.daily_calendar_item;
            }

            @Override
            public void convert(final VH holder, final BangumiItem data, int position) {
                holder.setText(R.id.item_title, data.getName_cn());
                holder.setImage(R.id.item_image, data.getLarge_image());

                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), BangumiDetailActivity.class);
                                intent.putExtra("Bangumi_id", data.getBangumi_id());
                                intent.putExtra("Name_cn", data.getName_cn());
                                intent.putExtra("Large_image", data.getLarge_image());
                                startActivity(intent);
                            }
                        }
                );
            }
        };

    }

    @Override
    public void refresh() {
        getPresenter().loadDailyCalendar(weekday, true);
    }

    @Override
    protected void lazyLoad() {
        getPresenter().subscribe(weekday);
    }

    /**
     * 显示某日的番剧
     * @param mBangumiItemList 当日的番剧列表
     */
    @Override
    public void showDailyCalendar(List<BangumiItem> mBangumiItemList) {
        adapter.replaceData(mBangumiItemList);
        showBangumiView();
    }

    @Override
    public void showBangumiView() {
        recyclerView.setVisibility(View.VISIBLE);
        noBangumiView.setVisibility(View.GONE);
    }

    /**
     * 当日没有番剧可以获取
     */
    @Override
    public void showNoBangumiView() {
        recyclerView.setVisibility(View.INVISIBLE);
        noBangumiView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        refresh(active);
    }

    /**
     * 显示读取番剧失败的消息
     */
    @Override
    public void showLoadingBangumiError() {
        ToastUtils.showToast(getActivity(), "Error while loading Bangumi!!!");
    }

}
