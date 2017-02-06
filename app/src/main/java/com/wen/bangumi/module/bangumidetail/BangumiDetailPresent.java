package com.wen.bangumi.module.bangumidetail;

import android.support.annotation.NonNull;

import com.wen.bangumi.Bangumi;
import com.wen.bangumi.entity.EpisodeUpdateReply;
import com.wen.bangumi.entity.bangumi.EpisodesEntity;
import com.wen.bangumi.entity.bangumi.SingleBangumiItem;
import com.wen.bangumi.entity.bangumi.UserEpisodeStatus;
import com.wen.bangumi.module.user.UserPreferences;
import com.wen.bangumi.network.RetrofitHelper;
import com.wen.bangumi.util.Injection;
import com.wen.bangumi.util.JsoupUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/2/6.
 */

public class BangumiDetailPresent implements BangumiDetailContract.Presenter {

    private BangumiDetailContract.View bangumiDetailView;

    private CompositeDisposable compositeDisposable;

    public BangumiDetailPresent(@NonNull BangumiDetailContract.View view,
                                @NonNull CompositeDisposable compositeDisposable) {
        this.bangumiDetailView = checkNotNull(view);
        this.compositeDisposable = checkNotNull(compositeDisposable);

        bangumiDetailView.setPresenter(this);
    }

    public static BangumiDetailPresent newInstance(BangumiDetailContract.View view) {
        return new BangumiDetailPresent(
                view,
                new CompositeDisposable()
        );
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void subscribe(int id) {
//        loadBangumiInfo(id);
        loadBangumiEpisode(id);
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void loadBangumiInfo(int id) {

        Disposable disposable = RetrofitHelper
                .getBangumiApi()
                .getSubject(String.valueOf(id))
                .doOnSubscribe(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<SingleBangumiItem>() {
                            @Override
                            public void accept(SingleBangumiItem singleBangumiItem) throws Exception {

                            }
                        }
                );

        compositeDisposable.add(disposable);

    }

    @Override
    public void loadBangumiEpisode(int id) {

        Disposable disposable = Observable.zip(
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
                        bangumiDetailView.setProgressBar(true);
                    }
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<EpisodesEntity>>() {
                            @Override
                            public void accept(List<EpisodesEntity> episodesEntities) throws Exception {
                                bangumiDetailView.showBangumiEpisode(episodesEntities);
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
                                bangumiDetailView.setProgressBar(false);
                            }
                        }
                );

        compositeDisposable.add(disposable);

    }

    @Override
    public void updateEpisodeStatus(final EpisodesEntity.Episode episode, final String status) {

        RetrofitHelper.getBangumiApi()
                .updateEpisodeStatus(
                        episode.getId(),
                        status,
                        UserPreferences.getAuth(Bangumi.getInstance())
                )
                .doOnComplete(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                episode.setMy_status(EpisodeStatus.getStatusMap().get(status));
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
                                bangumiDetailView.updateBangumiEpisode(episode);
                            }
                        }
                );

    }
}
