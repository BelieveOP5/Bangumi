package com.wen.bangumi.BangumiDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wen.bangumi.Bangumi;
import com.wen.bangumi.R;
import com.wen.bangumi.entity.EpisodesEntity;

import java.util.List;

/**
 * Created by BelieveOP5 on 2017/2/1.
 */

public class BangumiSelectionRecyclerViewAdapter extends RecyclerView.Adapter<BangumiSelectionRecyclerViewAdapter.ItemViewHolder>{

    private List<EpisodesEntity.Episode> episodeList;

    private Context context;

    public BangumiSelectionRecyclerViewAdapter(List<EpisodesEntity.Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bangumi_detail_selection_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        final EpisodesEntity.Episode episode = episodeList.get(position);

        holder.btn.setText(String.valueOf(position + 1));

        if (episode.getStatus().equals("NA")) {
            holder.btn.setClickable(false);
            holder.btn.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.btn.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        if (episode.getMy_status().equals("statusWatched")) {
            holder.btn.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            holder.btn.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        holder.btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Bangumi.getInstance(), episode.getName_cn(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        Button btn;

        ItemViewHolder(View itemView) {
            super(itemView);

            btn = (Button) itemView.findViewById(R.id.btn);
        }

    }


}
