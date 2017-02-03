package com.wen.bangumi.collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wen.bangumi.BangumiDetail.BangumiDetailActivity;
import com.wen.bangumi.R;
import com.wen.bangumi.base.BaseFragment;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.util.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public class SingleCollFragment extends BaseFragment implements SingleCollContract.View{

    private BangumiStatus status;

    public void setStatus(BangumiStatus status) {
        this.status = status;
    }

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private SingleCollContract.Presenter mPresenter;

    private View mNoBangumiView;

    public static SingleCollFragment newInstance(BangumiStatus status) {
        Bundle args = new Bundle();
        SingleCollFragment fragment = new SingleCollFragment();
        fragment.setStatus(status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(SingleCollContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<BangumiItem>(), getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.single_coll_frag, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.bangumi_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.setOnItemClickListener(
                new com.wen.bangumi.collection.RecyclerViewAdapter.OnItemClickListener() {
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

        mNoBangumiView = root.findViewById(R.id.no_bangumi_layout);
        TextView textView = (TextView) root.findViewById(R.id.no_bangumi_text);

        switch (status) {
            case WISH:
                textView.setText(getString(R.string.no_wish_bangumi_text));
                break;
            case DO:
                textView.setText(getString(R.string.no_do_bangumi_text));
                break;
            case COLLECT:
                textView.setText(getString(R.string.no_coll_bangumi_text));
                break;
            case ON_HOLD:
                textView.setText(getString(R.string.no_on_hold_bangumi_text));
                break;
            case DROPPED:
                textView.setText(getString(R.string.no_dropped_bangumi_text));
                break;
            default:
                break;
        }

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setScrollUpChild(mRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // TODO: 2017/1/28
                    }
                }
        );
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(status);
    }

    /**
     * 显示某日的番剧
     * @param mBangumiItemList 当日的番剧列表
     */
    @Override
    public void showCollBangumi(List<BangumiItem> mBangumiItemList) {
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
