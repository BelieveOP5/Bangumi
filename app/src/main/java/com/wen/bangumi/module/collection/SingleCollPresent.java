package com.wen.bangumi.module.collection;

import android.support.annotation.NonNull;

import com.wen.bangumi.data.CollectionRepository;
import com.wen.bangumi.greenDAO.BangumiItem;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public class SingleCollPresent implements SingleCollContract.Presenter {

    private SingleCollContract.View mSingleCollView;

    @NonNull
    private final CollectionRepository mCollectionRepository;

    /**
     * RxJava1.0版本的是CompositeSubscription, 对应RxJava2.0版本的CompositeDisposable
     * @see <a href="https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#subscription"></a>
     */
    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public SingleCollPresent(@NonNull CollectionRepository collectionRepository,
                             @NonNull SingleCollContract.View view) {
        mCollectionRepository = checkNotNull(collectionRepository, "CollectionRepository cannot be null!");
        mSingleCollView = checkNotNull(view, "SingleCollView cannot be null!");

        mCompositeDisposable = new CompositeDisposable();
        mSingleCollView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void subscribe(BangumiStatus status) {
        loadBangumi(status, false);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    /**
     * 是否为第一次加载应用，初始化为是
     */
    private boolean mFirstLoad = true;

    @Override
    public void loadBangumi(BangumiStatus status, boolean forceUpdate) {
        loadBangumi(status, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadBangumi(BangumiStatus status, boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mSingleCollView.setLoadingIndicator(true);
        }

        if (forceUpdate) {
            mCollectionRepository.refreshBangumi();
        }

        mCompositeDisposable.clear();
        Disposable disposable = mCollectionRepository
                .loadBangumi(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<BangumiItem>>() {
                            @Override
                            public void accept(List<BangumiItem> bangumiItems) throws Exception {
                                //onNext
                                processBangumi(bangumiItems);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //onError
                                mSingleCollView.showLoadingBangumiError();
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                //onCompleted
                                mSingleCollView.setLoadingIndicator(false);
                            }
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    private void processBangumi(@NonNull List<BangumiItem> mList) {
        if (mList.isEmpty())
            //没有番剧可以获取
            mSingleCollView.showNoBangumiView();
        else
            mSingleCollView.showCollBangumi(mList);
    }

}
