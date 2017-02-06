/*
 * Copyright (C) 2016 Bowen Zhen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wen.bangumi.module.bangumidetail;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.Bangumi;
import com.wen.bangumi.R;
import com.wen.bangumi.base.BaseActivity;
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.entity.EpisodeUpdateReply;
import com.wen.bangumi.entity.bangumi.EpisodesEntity;
import com.wen.bangumi.network.RetrofitHelper;
import com.wen.bangumi.module.user.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 番剧详细信息界面
 * Created by BelieveOP5 on 2017/1/29.
 */

public class BangumiDetailActivity extends BaseActivity implements BangumiDetailContract.View{

    @BindView(R.id.bangumi_detail_episode_recycler_view)
    public RecyclerView episodeRecyclerView;

    public QuickAdapter<EpisodesEntity.Episode> episodeAdapter;

    @BindView(R.id.collapsing_toolbar_layout)
    public CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.bangumi_image_view)
    public ImageView imageView;

    @BindView(R.id.bangumi_total_episode_text_view)
    public TextView textView;

    @BindView(R.id.progress)
    public ContentLoadingProgressBar progressBar;

    private BottomSheetDialog dialog;

    private BangumiDetailContract.Presenter mPresenter;

    private int id;
    private String name;
    private String largeImage;

    @Override
    public int getLayoutId() {
        return R.layout.bangumi_detail_act;
    }

    @Override
    public void setPresenter(BangumiDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BangumiDetailPresent.newInstance(this);

        mPresenter.subscribe(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        this.id = getIntent().getIntExtra("Bangumi_id", 0);
        this.name = getIntent().getStringExtra("Name_cn");
        this.largeImage = getIntent().getStringExtra("Large_image");

        Picasso.with(this).load(largeImage).into(imageView);

        collapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(android.R.color.white)
        );
        collapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(android.R.color.transparent)
        );
        collapsingToolbarLayout.setTitle(name);

    }

    @Override
    protected void initAdapter() {

        episodeAdapter = new QuickAdapter<EpisodesEntity.Episode>(new ArrayList<EpisodesEntity.Episode>()) {

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.bangumi_detail_episode_item;
            }

            @Override
            public void convert(final VH holder, final EpisodesEntity.Episode data, int position) {
                holder.setBtnText(R.id.btn, String.valueOf(data.getEpisode_id()));

                Button btn = holder.getView(R.id.btn);
                Resources res = holder.itemView.getResources();

                btn.setTextColor(res.getColor(android.R.color.white));

                if (data.getStatus().equals("NA")) {
                    btn.setClickable(false);
                    btn.setBackgroundColor(res.getColor(android.R.color.darker_gray));
                    return;
                }

                if (data.getStatus().equals("Today")) {
                    btn.setBackgroundColor(res.getColor(android.R.color.holo_green_dark));
                }

                if (data.getMy_status().equals("statusWatched")) {
                    btn.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
                }

                btn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showBangumiEpisodeBottomSheetDialog(data);
                            }
                        }
                );
            }

            @Override
            public void replaceData(EpisodesEntity.Episode episode) {
                for (int i = 0; i <= getDatas().size(); ++i) {
                    if (getDatas().get(i).getId() == episode.getId()) {
                        getDatas().set(i, episode);
                        notifyItemChanged(i);
                        return;
                    }
                }
            }

        };

        episodeRecyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        episodeRecyclerView.setAdapter(episodeAdapter);

    }

    @Override
    public void showBangumiEpisode(List<EpisodesEntity> episodesEntities) {

        textView.setText(String.valueOf(episodesEntities.get(0).getEpisodeList().size()));
        episodeAdapter.replaceData(episodesEntities.get(0).getEpisodeList());

    }

    /**
     * 点击章节按钮之后，显示相关章节的信息，以及需要进行的操作的BottomSheet
     * @param episode
     */
    @Override
    public void showBangumiEpisodeBottomSheetDialog(final EpisodesEntity.Episode episode) {

        dialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.bangumi_detail_episode_bottom_sheet, null, false);

        TextView titleTextView = (TextView) view.findViewById(R.id.episode_title_text_view);
        titleTextView.setText(episode.getName());

        TextView subTitleTextView = (TextView) view.findViewById(R.id.episode_subtitle_text_view);
        subTitleTextView.setText(episode.getName_cn());

        TextView infoTextView = (TextView) view.findViewById(R.id.episode_info_text_view);
        infoTextView.setText(episode.getInfo());

        final QuickAdapter<String> episodeBottomSheetAdapter = new QuickAdapter<String>(new ArrayList<String>()) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.bangumi_detail_episode_bottom_sheet_recycler_view_item;
            }

            @Override
            public void convert(VH holder, final String data, int position) {

                holder.setBtnText(R.id.btn, EpisodeStatus.getActionMap().get(data));

                holder.getView(R.id.btn).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.updateEpisodeStatus(episode, data);
                            }
                        }
                );
            }
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(episodeBottomSheetAdapter);

        List<String> stringList = new ArrayList<>();
        stringList.add("queue");
        stringList.add("watched");
        stringList.add("drop");
        stringList.add("remove");

        episodeBottomSheetAdapter.replaceData(stringList);

        dialog.setContentView(view);

        dialog.show();

    }

    @Override
    public void updateBangumiEpisode(EpisodesEntity.Episode episode) {
        episodeAdapter.replaceData(episode);
        dialog.dismiss();
    }

    @Override
    public void setProgressBar(boolean active) {

        if (active) {
            progressBar.show();
        } else {
            progressBar.hide();
        }

    }
}
