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
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.entity.EpisodeUpdateReply;
import com.wen.bangumi.entity.bangumi.EpisodesEntity;
import com.wen.bangumi.entity.bangumi.UserEpisodeStatus;
import com.wen.bangumi.network.RetrofitHelper;
import com.wen.bangumi.module.user.UserPreferences;
import com.wen.bangumi.util.JsoupUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by BelieveOP5 on 2017/1/29.
 */

public class BangumiDetailActivity extends AppCompatActivity {

    @BindView(R.id.bangumi_detail_episode_recycler_view)
    public RecyclerView recyclerView;

    public QuickAdapter<EpisodesEntity.Episode> episodeAdapter;

    @BindView(R.id.collapsing_toolbar_layout)
    public CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.image_view)
    public ImageView imageView;

    @BindView(R.id.bangumi_total_episode_text_view)
    public TextView textView;

    private BottomSheetDialog dialog;

    private int id;
    private String name;
    private String largeImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bangumi_detail_act);

        ButterKnife.bind(this);

        initView();

        initAdapter();

        loadData();

    }

    private void initView() {

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

    private void initAdapter() {

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
                                initDialog(data);
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

    }

    private void loadData() {

        Observable.zip(
                //基于html获取的章节信息
                RetrofitHelper.getBangumiWebApi().loadEpisodes(String.valueOf(id)).map(
                        new Function<String,  List<EpisodesEntity>>() {
                            @Override
                            public  List<EpisodesEntity> apply(String s) throws Exception {
                                List<EpisodesEntity> episodesEntities = JsoupUtils.parseBangumiEpi(s);
                                if (episodesEntities == null) {
                                    // TODO: 2017/2/4 调用显示还没有章节
                                    return new ArrayList<>();
                                }
                                return episodesEntities;
                            }
                        }
                )
                ,
                //基于api获取到的用户观看信息
                RetrofitHelper.getBangumiApi().loadEpisodeStatus(
                        UserPreferences.getId(Bangumi.getInstance()),
                        String.valueOf(id),
                        UserPreferences.getAuth(Bangumi.getInstance())
                ).map(
                        new Function<UserEpisodeStatus, UserEpisodeStatus>() {
                            /**
                             * 确保episodeStatus至少有一个，如果episodeStatus为空，则zip中的BiFunction则不会执行，也不会进入onNext中
                             * 因为zip返回的数量是两个Observable中数量更少的一个
                             * @param userEpisodeStatus
                             * @return
                             * @throws Exception
                             */
                            @Override
                            public UserEpisodeStatus apply(UserEpisodeStatus userEpisodeStatus) throws Exception {
                                if (userEpisodeStatus == null)
                                    return new UserEpisodeStatus();
                                return userEpisodeStatus;
                            }
                        }
                ),
                new BiFunction<List<EpisodesEntity>, UserEpisodeStatus, List<EpisodesEntity>>() {
                    /**
                     * 将自己构造好的章节实体和从api中获取的用户已看的章节结合，形成完整的章节实体
                     * @param episodesEntities 从网页解析出来的所有章节
                     * @param userEpisodeStatus api获取的用户观看或抛弃的章节
                     * @return 整合后的所有章节状态
                     * @throws Exception
                     */
                    @Override
                    public List<EpisodesEntity> apply(List<EpisodesEntity> episodesEntities, UserEpisodeStatus userEpisodeStatus) throws Exception {

                        //用户还一个章节都没看过,或者这个动漫还没有章节
                        if (userEpisodeStatus.getSubject_id() == 0 || episodesEntities.size() == 0)
                            return episodesEntities;

                        // FIXME: 2017/2/2 当数据量大时，处理太慢
                        for (EpisodesEntity episodesEntity : episodesEntities) {
                            List<EpisodesEntity.Episode>  episodeList = episodesEntity.getEpisodeList();
                            for (EpisodesEntity.Episode episode : episodeList) {
                                for (UserEpisodeStatus.EpsBean epsBean : userEpisodeStatus.getEps()) {
                                    if (episode.getId() == epsBean.getId()) {
                                        episode.setMy_status(EpisodeStatus.getStatusMap().get(epsBean.getStatus().getUrl_name()));
                                        break;
                                    }
                                }
                            }
                        }
                        return episodesEntities;
                    }
                }
        ).doOnSubscribe(
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }
        ).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Consumer<List<EpisodesEntity>>() {
                    @Override
                    public void accept(List<EpisodesEntity> episodesEntities) throws Exception {
                        finishTask(episodesEntities);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }
        );

    }

    private void finishTask(List<EpisodesEntity> episodesEntities) {

        textView.setText(String.valueOf(episodesEntities.get(0).getEpisodeList().size()));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));

        recyclerView.setAdapter(episodeAdapter);

        episodeAdapter.replaceData(episodesEntities.get(0).getEpisodeList());

    }

    /**
     * 更新该章节的状态
     * @param episode 要修改的章节
     * @param status 要修改的状态
     */
    public void updateEpisodeStatus(final EpisodesEntity.Episode episode, final String status) {

        RetrofitHelper.getBangumiApi()
                .updateEpisodeStatus(
                        episode.getId(),
                        status,
                        UserPreferences.getAuth(Bangumi.getInstance())
                )
                .doOnSubscribe(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        }
                )
                .doOnComplete(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                episodeAdapter.replaceData(episode);
                                dialog.dismiss();
                            }
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<EpisodeUpdateReply>() {
                            @Override
                            public void accept(EpisodeUpdateReply episodeUpdateReply) throws Exception {
                                // TODO: 2017/2/5 检查返回信息
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                episode.setMy_status(EpisodeStatus.getStatusMap().get(status));
                            }
                        }
                );

    }

    public void initDialog(final EpisodesEntity.Episode episode) {

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
                                updateEpisodeStatus(episode, data);
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

}
