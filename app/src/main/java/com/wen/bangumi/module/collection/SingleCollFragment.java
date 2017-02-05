package com.wen.bangumi.module.collection;

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

import com.wen.bangumi.R;
import com.wen.bangumi.base.BaseLazyFragment;
import com.wen.bangumi.base.NormalAdapterWrapper;
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.module.bangumidetail.BangumiDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public class SingleCollFragment extends BaseLazyFragment implements SingleCollContract.View{

    private BangumiStatus status;

    public void setStatus(BangumiStatus status) {
        this.status = status;
    }

    private RecyclerView mRecyclerView;
    private QuickAdapter<BangumiItem> adapter;

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

        initAdapter();
    }

    @Override
    protected void initAdapter() {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.single_collection_frag, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.bangumi_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        NormalAdapterWrapper<QuickAdapter<BangumiItem>> newAdapter = new NormalAdapterWrapper<>(adapter);
        mRecyclerView.setAdapter(adapter);

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

        showBangumiView();

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // TODO: 2017/1/28
                    }
                }
        );

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

    /**
     * 显示某日的番剧
     * @param mBangumiItemList 当日的番剧列表
     */
    @Override
    public void showCollBangumi(List<BangumiItem> mBangumiItemList) {
        adapter.replaceData(mBangumiItemList);

        showBangumiView();
    }

    public void showBangumiView() {
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
