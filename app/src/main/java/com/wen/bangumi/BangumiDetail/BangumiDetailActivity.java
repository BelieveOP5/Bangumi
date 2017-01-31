package com.wen.bangumi.BangumiDetail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.R;

/**
 * Created by BelieveOP5 on 2017/1/29.
 */

public class BangumiDetailActivity extends AppCompatActivity {

    private static final String TAG = "BangumiDetailActivity";

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

        CollapsingToolbarLayout mCollapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(android.R.color.white)
        );
        mCollapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(android.R.color.transparent)
        );
        mCollapsingToolbarLayout.setTitle(name);

        Picasso.with(this).load(largeImage).into(imageView);

    }

}
