package com.wen.bangumi.module.calendaritem;

import android.support.annotation.NonNull;

import com.wen.bangumi.data.CalendarRepository;
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
 * Created by BelieveOP5 on 2017/1/17.
 */

public class DailyCalendarPresenter implements DailyCalendarContract.Presenter {

    @NonNull
    private final CalendarRepository mCalendarRepository;

    @NonNull
    private final DailyCalendarContract.View mDailyCalendarView;

    /**
     * RxJava1.0版本的是CompositeSubscription, 对应RxJava2.0版本的CompositeDisposable
     * @see <a href="https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#subscription"></a>
     */
    @NonNull
    private CompositeDisposable mCompositeDisposable;

    /**
     * 是否为第一次加载应用，初始化为是
     */
    private boolean mFirstLoad = true;

    public DailyCalendarPresenter(@NonNull CalendarRepository mCalendarRepository,
                                  @NonNull DailyCalendarContract.View view) {
        this.mCalendarRepository = checkNotNull(mCalendarRepository, "mCalendarRepository cannot be null!");
        this.mDailyCalendarView = checkNotNull(view, "mDailyCalendarView cannot be null!");

        mCompositeDisposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    /**
     * 这个类里用不上这个无参的subscribe方法
     */
    @Override
    public void subscribe() {

    }

    @Override
    public void subscribe(WeekDay weekday) {
        loadDailyCalendar(weekday, false);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    /**
     * 加载一日的番剧列表
     * @param forceUpdate 是否为强制刷新
     */
    @Override
    public void loadDailyCalendar(WeekDay weekday, boolean forceUpdate) {
        loadData(weekday, forceUpdate || mFirstLoad);
        mFirstLoad = false;
    }


    /**
     *
     * @param weekday
     * @param forceUpdate
     */
    private void loadData(WeekDay weekday, boolean forceUpdate) {

        /**
         * 如果是第一次加载或者强制刷新，则显示刷新小圆圈
         */
        if (forceUpdate) {
            mCalendarRepository.refreshBangumi();
            mDailyCalendarView.setLoadingIndicator(true);
        }

        //不管是第一次加载还是需要强制刷新，都应该清除
        mCompositeDisposable.clear();

        Disposable disposable = mCalendarRepository
                .loadBangumi(weekday)
                /**
                 * subscribeOn(): 指定 Subscribe()所发生的线程,即Observable.OnSubscribe 被激活时所处的线程,或者叫做事件产生的线程
                 * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                 */
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //不用lambda表达式,不用Jack编译，用这两个经常容易出现NoMethodError，原因不明
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
                                mDailyCalendarView.showLoadingBangumiError();
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                //onCompleted
                                mDailyCalendarView.setLoadingIndicator(false);
                            }
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    private void processBangumi(@NonNull List<BangumiItem> mList) {
        if (mList.isEmpty())
            //没有番剧可以获取
            mDailyCalendarView.showNoBangumiView();
        else
            mDailyCalendarView.showDailyCalendar(mList);
    }

}
