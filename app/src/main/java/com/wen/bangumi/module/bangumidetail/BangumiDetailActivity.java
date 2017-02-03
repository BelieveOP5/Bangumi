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

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.Bangumi;
import com.wen.bangumi.R;
import com.wen.bangumi.network.RetrofitHelper;
import com.wen.bangumi.entity.EpisodesEntity;
import com.wen.bangumi.entity.MyEpisodeStatus;
import com.wen.bangumi.module.user.UserPreferences;
import com.wen.bangumi.util.JsoupUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private RecyclerView mRecyclerView;

    private int id;
    private String name;
    private String largeImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bangumi_detail_act);

        this.id = getIntent().getIntExtra("Bangumi_id", 0);
        this.name = getIntent().getStringExtra("Name_cn");
        this.largeImage = getIntent().getStringExtra("Large_image");

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        Picasso.with(this).load(largeImage).into(imageView);

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(android.R.color.white)
        );
        collapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(android.R.color.transparent)
        );
        collapsingToolbarLayout.setTitle(name);

        mRecyclerView = (RecyclerView) findViewById(R.id.bangumi_selection_recycler_view);

        loadData();

    }

    public void loadData() {

        RetrofitHelper.getBangumiWebApi()
                .loadEpisodes(String.valueOf(id))
                .map(new Function<String, List<EpisodesEntity>>() {
                    /**
                     * 转化从网络获取到的html格式页面
                     * @param s html
                     * @return
                     * @throws Exception
                     */
                    @Override
                    public List<EpisodesEntity> apply(String s) throws Exception {
                        return JsoupUtils.parseBangumiEpi(s);
                    }
                })
                .zipWith(
                        getMyEpisodeStatus(),
                        new BiFunction<List<EpisodesEntity>, MyEpisodeStatus, List<EpisodesEntity>>() {
                            /**
                             * 将自己构造好的章节实体和从api中获取的用户已看的章节结合，形成完整的章节实体
                             * @param episodesEntities
                             * @param myEpisodeStatus
                             * @return
                             * @throws Exception
                             */
                            @Override
                            public List<EpisodesEntity> apply(List<EpisodesEntity> episodesEntities, MyEpisodeStatus myEpisodeStatus) throws Exception {

                                // FIXME: 2017/2/2 可以在另一个地方进行这样的处理，就是在界面需要显示的时候对按钮一个一个映射，放在这里循环数太多，当处理海贼王这样的数据的时候会很慢
                                for (EpisodesEntity episodesEntity : episodesEntities) {

                                    List<EpisodesEntity.Episode>  episodeList = episodesEntity.getEpisodeList();
                                    for (EpisodesEntity.Episode episode : episodeList) {

                                        for (MyEpisodeStatus.EpsBean epsBean : myEpisodeStatus.getEps()) {
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
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<EpisodesEntity>>() {
                            @Override
                            public void accept(List<EpisodesEntity> episodesEntities) throws Exception {
                                // TODO: 2017/2/2 显示数据
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

    private Observable<MyEpisodeStatus> getMyEpisodeStatus() {

        return RetrofitHelper.getBangumiApi()
                .loadMyEpisodeStatus(
                        UserPreferences.getId(Bangumi.getInstance()),
                        String.valueOf(id),
                        UserPreferences.getAuth(Bangumi.getInstance())
                );

    }

    private void finishTask(List<EpisodesEntity> episodesEntities) {

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 6));

        BangumiSelectionRecyclerViewAdapter adapter = new BangumiSelectionRecyclerViewAdapter(episodesEntities.get(0).getEpisodeList());

        mRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

}
