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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.Bangumi;
import com.wen.bangumi.R;
import com.wen.bangumi.base.QuickAdapter;
import com.wen.bangumi.entity.bangumi.EpisodesEntity;
import com.wen.bangumi.entity.bangumi.EpisodeStatus;
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

    static final String TAG = "BangumiDetailActivity";

    @BindView(R.id.bangumi_detail_episode_recycler_view)
    public RecyclerView recyclerView;

    private QuickAdapter<EpisodesEntity.Episode> adapter;

    @BindView(R.id.collapsing_toolbar_layout)
    public CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.image_view)
    public ImageView imageView;

    @BindView(R.id.bangumi_total_episode_text_view)
    public TextView textView;

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

        adapter = new QuickAdapter<EpisodesEntity.Episode>(new ArrayList<EpisodesEntity.Episode>()) {

            /**
             * 用户观看过的章节信息
             */
            private EpisodeStatus episodeStatus;

            @Override
            public EpisodeStatus getEpisodeStatus() {
                return episodeStatus;
            }

            @Override
            public void setEpisodeStatus(EpisodeStatus episodeStatus) {
                this.episodeStatus = episodeStatus;
            }

            @Override
            public void initEpisodeStatus() {

                RetrofitHelper.getBangumiApi()
                        .loadEpisodeStatus(
                                UserPreferences.getId(Bangumi.getInstance()),
                                String.valueOf(id),
                                UserPreferences.getAuth(Bangumi.getInstance())
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<EpisodeStatus>() {
                            @Override
                            public void accept(EpisodeStatus episodeStatus) throws Exception {
                                setEpisodeStatus(episodeStatus);
                            }
                        });

            }

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.bangumi_detail_episode_item;
            }

            @Override
            public void convert(VH holder, final EpisodesEntity.Episode data, int position) {
                holder.setBtnText(R.id.btn, String.valueOf(position + 1));

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
                                Toast.makeText(
                                        Bangumi.getInstance(),
                                        data.getName_cn().isEmpty() ? data.getName() : data.getName_cn(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
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
                                    return new ArrayList<EpisodesEntity>();
                                }
                                return JsoupUtils.parseBangumiEpi(s);
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
                        new Function<EpisodeStatus, EpisodeStatus>() {
                            /**
                             * 确保episodeStatus至少有一个，如果episodeStatus为空，则zip中的BiFunction则不会执行，也不会进入onNext中
                             * 因为zip返回的数量是两个Observable中数量更少的一个
                             * @param episodeStatus
                             * @return
                             * @throws Exception
                             */
                            @Override
                            public EpisodeStatus apply(EpisodeStatus episodeStatus) throws Exception {
                                if (episodeStatus == null)
                                    return new EpisodeStatus();
                                return episodeStatus;
                            }
                        }
                ),
                new BiFunction<List<EpisodesEntity>, EpisodeStatus, List<EpisodesEntity>>() {
                    /**
                     * 将自己构造好的章节实体和从api中获取的用户已看的章节结合，形成完整的章节实体
                     * @param episodesEntities
                     * @param episodeStatus
                     * @return
                     * @throws Exception
                     */
                    @Override
                    public List<EpisodesEntity> apply(List<EpisodesEntity> episodesEntities, EpisodeStatus episodeStatus) throws Exception {

                        //用户还一个章节都没看过,或者这个动漫还没有章节
                        if (episodeStatus.getSubject_id() == 0 || episodesEntities.size() == 0)
                            return episodesEntities;

                        // FIXME: 2017/2/2 可以在另一个地方进行这样的处理，就是在界面需要显示的时候对按钮一个一个映射，放在这里循环数太多，当处理海贼王这样的数据的时候会很慢
                        for (EpisodesEntity episodesEntity : episodesEntities) {

                            List<EpisodesEntity.Episode>  episodeList = episodesEntity.getEpisodeList();
                            for (EpisodesEntity.Episode episode : episodeList) {

                                for (EpisodeStatus.EpsBean epsBean : episodeStatus.getEps()) {
                                    if (episode.getId() == epsBean.getId()) {
                                        switch (epsBean.getStatus().getUrl_name()) {
                                            case "watched":
                                                episode.setMy_status("statusWatched");
                                                break;
                                            case "drop":
                                                episode.setMy_status("statusDrop");
                                                break;
                                        }
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

        recyclerView.setAdapter(adapter);

        adapter.replaceData(episodesEntities.get(0).getEpisodeList());

    }

}
