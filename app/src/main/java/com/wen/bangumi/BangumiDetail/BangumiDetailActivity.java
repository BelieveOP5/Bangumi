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

package com.wen.bangumi.BangumiDetail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.R;
import com.wen.bangumi.network.RetrofitHelper;
import com.wen.bangumi.responseentity.EpisodesEntity;
import com.wen.bangumi.util.JsoupUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by BelieveOP5 on 2017/1/29.
 */

public class BangumiDetailActivity extends AppCompatActivity {

    static final String TAG = "BangumiDetailActivity";

    private BangumiDetailContract.Presenter mPresenter;

    private String id;
    private String name;
    private String largeImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bangumi_detail_act);

        this.id = getIntent().getStringExtra("Bangumi_id");
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            BangumiSelectionRecyclerViewAdapter recyclerViewAdapter = new BangumiSelectionRecyclerViewAdapter();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        loadData();

    }

    public void loadData() {

        RetrofitHelper.getBangumiWebApi()
                .loadEpisodes(id)
                .map(new Function<String, List<EpisodesEntity>>() {
                    @Override
                    public List<EpisodesEntity> apply(String s) throws Exception {
                        return JsoupUtils.parseBangumiEpi(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<EpisodesEntity>>() {
                            @Override
                            public void accept(List<EpisodesEntity> episodesEntities) throws Exception {

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

}
