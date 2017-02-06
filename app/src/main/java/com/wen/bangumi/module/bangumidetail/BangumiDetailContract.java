package com.wen.bangumi.module.bangumidetail;

import com.wen.bangumi.base.BasePresenter;
import com.wen.bangumi.base.BaseView;
import com.wen.bangumi.entity.bangumi.EpisodesEntity;

import java.util.List;

/**
 * Created by BelieveOP5 on 2017/2/6.
 */

public interface BangumiDetailContract {

    interface View extends BaseView<Presenter> {

        /**
         * 显示章节信息
         * @param episodesEntities
         */
        void showBangumiEpisode(List<EpisodesEntity> episodesEntities);

        /**
         * 更新章节的显示状态
         * @param episode
         */
        void updateBangumiEpisode(EpisodesEntity.Episode episode);

        /**
         * 显示章节操作的BottomSheet
         * @param episode
         */
        void showBangumiEpisodeBottomSheetDialog(EpisodesEntity.Episode episode);

        /**
         * 设置更新的圆圈是否出现
         * @param active
         */
        void setProgressBar(boolean active);

    }

    interface Presenter extends BasePresenter {

        void subscribe(int id);

        /**
         * 根据id获取该番据的详细信息
         * @param id
         */
        void loadBangumiInfo(int id);

        /**
         * 从网上加载章节信息
         * @param id 需要加载章节的番剧id
         */
        void loadBangumiEpisode(int id);

        /**
         * 更新章节的状态
         * @param episode 要更新的章节
         * @param status 要更新的状态
         */
        void updateEpisodeStatus(EpisodesEntity.Episode episode, String status);

    }

}
