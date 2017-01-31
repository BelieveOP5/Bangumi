package com.wen.bangumi.calendaritem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wen.bangumi.BangumiDetail.BangumiDetailActivity;
import com.wen.bangumi.base.BaseFragment;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.util.ScrollChildSwipeRefreshLayout;
import com.wen.bangumi.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/16.
 */

public class DailyCalendarFragment extends BaseFragment implements DailyCalendarContract.View{

    @NonNull
    private static final String CURRENT_POSITION = "DailyCalendarFragment";

    private WeekDay weekday;

    public void setDate(WeekDay weekday) {
        this.weekday = weekday;
    }

    private DailyCalendarContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private View mNoBangumiView;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化当前RecyclerView对应的Adapter
        mRecyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<BangumiItem>(), getActivity());
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
        View root = inflater.inflate(R.layout.dailycalendar_frag, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.bangumiList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.setOnItemClickListener(
                new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, BangumiItem item) {
                        Intent intent = new Intent(getActivity(), BangumiDetailActivity.class);
                        intent.putExtra("Bangumi_id", item.getBangumi_id());
                        intent.putExtra("Name_cn", item.getName_cn());
                        intent.putExtra("Large_image", item.getLarge_image());
                        startActivity(
                                intent,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),
                                        view,
                                        "image_view"
                                ).toBundle()
                        );
                    }
                }
        );

        mNoBangumiView =  root.findViewById(R.id.noBangumi);

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setScrollUpChild(mRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.loadDailyCalendar(weekday, true);
                    }
                }
        );

        return root;
    }

    /**
     * 当界面加载完成的时候，并且在正确的运行的时候调用该函数
     */
    @Override
    public void onResume() {
        super.onResume();

        // FIXME: 2017/1/27 每次界面加载完成之后，再回到该界面的话会出现重新加载的情况
        //获取数据
        mPresenter.subscribe(weekday);

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
        mRecyclerViewAdapter.replaceData(mBangumiItemList);

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoBangumiView.setVisibility(View.GONE);
    }

    /**
     * 当日没有番剧可以获取
     */
    @Override
    public void showNoBangumiView() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoBangumiView.setVisibility(View.VISIBLE);
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
