package com.wen.bangumi.module.collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wen.bangumi.R;
import com.wen.bangumi.base.BaseLazyFragment;
import com.wen.bangumi.base.NormalAdapterWrapper;
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.module.bangumidetail.BangumiDetailActivity;
import com.wen.bangumi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public class SingleCollectionFragment extends BaseLazyFragment implements SingleCollectionContract.View{

    //这个fragment代表的状态
    private BangumiStatus status;

    public void setStatus(BangumiStatus status) {
        this.status = status;
    }

    @BindView(R.id.bangumi_list)
    public RecyclerView recyclerView;

    @BindView(R.id.no_bangumi_layout)
    public View noBangumiView;

    /**
     * adapter是普通的adapter
     * 而newAdapter是带有header和footer的adapter
     */
    private QuickAdapter<BangumiItem> adapter;
    NormalAdapterWrapper<QuickAdapter<BangumiItem>> newAdapter;

    private SingleCollectionContract.Presenter mPresenter;

    public static SingleCollectionFragment newInstance(BangumiStatus status) {
        Bundle args = new Bundle();
        SingleCollectionFragment fragment = new SingleCollectionFragment();
        fragment.setStatus(status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(SingleCollectionContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void initAdapter() {

        /**
         * 初始化recyclerView的adapter
         */
        adapter = new QuickAdapter<BangumiItem>(new ArrayList<BangumiItem>()) {

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.single_collection_item;
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

        newAdapter = new NormalAdapterWrapper<>(adapter);

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.single_collection_header_view, null, false);
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.single_collection_footer_view, null, false);

        newAdapter.addHeaderView(headerView);
        newAdapter.addFooterView(footerView);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.single_collection_frag, container, false);

        ButterKnife.bind(this, root);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        int totalItemCount = linearLayoutManager.getItemCount();

                        if (dy > 0 && lastVisibleItem == totalItemCount - 1) {
                            ToastUtils.showToast(getContext(), "你拉到最底了！！！");
                        }

                    }
                }
        );

        recyclerView.setAdapter(newAdapter);

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

        showBangumiView();

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.loadBangumi(status, true);
                    }
                }
        );

        //视图已经初始化完成
        isPrepared = true;
        return root;
    }

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
        mPresenter.subscribe(status);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void showBangumiCollection(List<BangumiItem> mBangumiItemList) {
        adapter.replaceData(mBangumiItemList);
        newAdapter.notifyDataSetChanged();

        showBangumiView();
    }

    @Override
    public void showBangumiView() {
        recyclerView.setVisibility(View.VISIBLE);
        noBangumiView.setVisibility(View.GONE);
    }

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
