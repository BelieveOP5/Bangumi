package com.wen.bangumi.module.calendaritem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wen.bangumi.base.BaseLazyFragment;
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.R;
import com.wen.bangumi.module.bangumidetail.BangumiDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/16.
 */

public class DailyCalendarFragment extends BaseLazyFragment implements DailyCalendarContract.View{

    private WeekDay weekday;

    public void setDate(WeekDay weekday) {
        this.weekday = weekday;
    }

    private DailyCalendarContract.Presenter mPresenter;

    @BindView(R.id.bangumiList)
    public RecyclerView recyclerView;

    @BindView(R.id.noBangumi)
    public View noBangumiView;

    private QuickAdapter<BangumiItem> adapter;

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
                                startActivity(
                                        intent,
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                getActivity(),
                                                holder.getView(R.id.item_image),
                                                "image_view"
                                        ).toBundle()
                                );
                            }
                        }
                );
            }
        };

    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.daily_calendar_frag, container, false);

        ButterKnife.bind(this, root);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        showBangumiView();

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.loadDailyCalendar(weekday, true);
                    }
                }
        );

        isPrepared = true;

        return root;
    }

    /**
     * 当界面加载完成的时候，并且在正确的运行的时候调用该函数
     */
    @Override
    public void onResume() {
        super.onResume();

        //获取数据
        if (!isPrepared || !isVisible)
            return;

        lazyLoad();
        isPrepared = false;

    }

    @Override
    protected void lazyLoad() {
        mPresenter.subscribe(weekday);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull DailyCalendarContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter);
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
        Toast.makeText(getActivity(), "Error while loading Bangumi!!!", Toast.LENGTH_SHORT).show();
    }
}
