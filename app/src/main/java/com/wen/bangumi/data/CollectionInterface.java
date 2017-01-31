package com.wen.bangumi.data;

import android.support.annotation.NonNull;

import com.wen.bangumi.collection.BangumiStatus;
import com.wen.bangumi.greenDAO.BangumiItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public interface CollectionInterface {

    /**
     * 获取番剧列表
     * @return
     */
    Observable<List<BangumiItem>> loadBangumi(@NonNull BangumiStatus status);

    /**
     * 当需要强制刷新时调用，表示需要清除缓存，并重新加载
     */
    void refreshBangumi();

}
