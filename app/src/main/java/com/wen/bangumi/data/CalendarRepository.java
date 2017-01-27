package com.wen.bangumi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wen.bangumi.api.BiliBiliApi;
import com.wen.bangumi.calendar.MainActivity;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.greenDAO.BangumiItemDao;
import com.wen.bangumi.greenDAO.DaoSession;
import com.wen.bangumi.responseentity.DailyCalendar;
import com.wen.bangumi.responseentity.TimeLine_BiliBili;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BelieveOP5 on 2017/1/18.
 */

public class CalendarRepository implements CalendarInterface {

    @NonNull
    private static final String CURRENT_POSITION = "CalendarRepository";

    @Nullable
    private static CalendarRepository INSTANCE;

    /**
     * BangumiApi的基本网址
     */
    public static final String BANGUMI_BASE_URL = "http://api.bgm.tv/";
    /**
     * BiliBiliApi的基本网址
     */
    public static final String BiliBili_BASE_URL = "https://bangumi.bilibili.com/";

    /**
     * 默认连接超时时间为10s
     */
    public static final int DEFAULT_TIMEOUT = 10;

    /**
     * 三天时间间隔
     */
    public static final long DAY_TIME = 24*60*60*1000;

    /**
     * 暂存获取到的每日番剧
     */
    @VisibleForTesting
    @Nullable
    List<DailyCalendar> mDailyCalendarList;

    /**
     * 使应用的数据库里存的数据无效化，一般在强制刷新的时候修改为true
     */
    @VisibleForTesting
    boolean mCacheIsDirty = false;

    /**
     * 返回一个番剧实例
     * @return
     */
    public static CalendarRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CalendarRepository();
        }
        return INSTANCE;
    }

    /**
     * 从本地缓存或者网络获取数据
     * @return
     */
    @Override
    public Observable<List<BangumiItem>> loadBangumi(@NonNull final int mDate) {

        //如果缓存不Dirty（或者不在强制刷新的情况下），则从数据库中读取数据
        if (!mCacheIsDirty) {
            Observable<List<BangumiItem>> mCache = Observable.create(new ObservableOnSubscribe<List<BangumiItem>>() {
                @Override
                public void subscribe(ObservableEmitter<List<BangumiItem>> e) throws Exception {
                    List<BangumiItem> items = new ArrayList<>();
                    items.addAll(MainActivity.mActivity.getDaoSession().getBangumiItemDao().queryBuilder()
                            .where(BangumiItemDao.Properties.Air_weekday.eq(mDate))
                            .build().list());
                    e.onNext(items);
                    e.onComplete();
                }
            });
            return mCache;
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

//        BangumiApi mBangumiApi = new Retrofit.Builder()
//                .baseUrl(BANGUMI_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client)
//                .build()
//                .create(BangumiApi.class);

//        return mBangumiApi
//                .listCalendar()
//                .map(new Function<List<DailyCalendar>, List<BangumiItem>>() {
//                    @Override
//                    public List<BangumiItem> apply(List<DailyCalendar> calendarList) throws Exception {
//                        return saveBangumi(calendarList, mDate);
//                    }
//                })
//                //完成了之后
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mCacheIsDirty = false;
//                    }
//                });

        BiliBiliApi mBiliBiliApi = new Retrofit.Builder()
                .baseUrl(BiliBili_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(BiliBiliApi.class);

        return mBiliBiliApi
                .listCalendar()
                .map(new Function<TimeLine_BiliBili, List<BangumiItem>>() {
                    @Override
                    public List<BangumiItem> apply(TimeLine_BiliBili timeLine_biliBili) throws Exception {
                        return saveBiliBiliBangumi(timeLine_biliBili, mDate);
                    }
                })
                //完成了之后
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mCacheIsDirty = false;
                    }
                });

    }

    /**
     * 将每日的番剧数据存入到数据库中，并返回该模块所处的日期的番剧信息
     * @param mList 要储存的番剧信息
     * @param mDate 模块所处的日期
     * @return 该模块所处的日期的番剧信息
     */
    @Override
    public List<BangumiItem> saveBangumi(@NonNull List<DailyCalendar> mList, @NonNull int mDate) {

        //获取与数据库的连接
        DaoSession mDaoSession = MainActivity.mActivity.getDaoSession();

        //要存到数据库里的数据
        List<BangumiItem> dbList = new ArrayList<>();
        //要返回的数据
        List<BangumiItem> _dbList = new ArrayList<>();

        for (int i = 0; i < mList.size(); ++i) {
            List<DailyCalendar.ItemsBean> _mList = mList.get(i).getItems();
            for (int j = 0; j < _mList.size(); ++j) {

                DailyCalendar.ItemsBean item = _mList.get(j);
                BangumiItem entity = new BangumiItem();

                entity.setBangumi_id(item.getId());
                entity.setAir_weekday(item.getAir_weekday());

                if (item.getName_cn() != null)
                    entity.setName_cn(item.getName_cn());
                else
                    entity.setName_cn(item.getName());

                entity.setLarge_image(item.getImages().getLarge());

                dbList.add(entity);

                //不能用上面这个比较方法，一使用就报错，原因不明
//                if (entity.getAir_weekday() == mDate)
                if (item.getAir_weekday() == mDate)
                    _dbList.add(entity);
            }
        }

        //在储存数据之前，删除数据库中的所有数据
        mDaoSession.getBangumiItemDao().deleteAll();

        //存储从网上获取的数据
        mDaoSession.getBangumiItemDao().insertInTx(dbList);

        return _dbList;
    }

    @Override
    public List<BangumiItem> saveBiliBiliBangumi(@NonNull TimeLine_BiliBili mList, @NonNull int mDate) {
        //获取与数据库的连接
        DaoSession mDaoSession = MainActivity.mActivity.getDaoSession();

        //要存到数据库里的数据
        List<BangumiItem> dbList = new ArrayList<>();
        //要返回的数据
        List<BangumiItem> _dbList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //获取当天日期
        int k = getWeekOfDate(sdf.format(getToday()));

        for (int i = 0; i < mList.getResult().size(); ++i) {

            TimeLine_BiliBili.ResultBean item = mList.getResult().get(i);

            Date _date = new Date();
            try {
                _date = sdf.parse(item.getPub_date());
            }catch (ParseException e) {
                e.printStackTrace();
            }

            //仅获取该星期的番剧
            if ((_date.getTime() - getToday().getTime() < 0) &&
                    Math.abs(_date.getTime() - getToday().getTime()) > (k - 1)*DAY_TIME)
                continue;
            else if ((_date.getTime() - getToday().getTime() > 0) &&
                    Math.abs(_date.getTime() - getToday().getTime()) > (8 - k)*DAY_TIME)
                continue;

            BangumiItem entity = new BangumiItem();

            entity.setBangumi_id(item.getSeason_id());
            entity.setAir_weekday(getWeekOfDate(item.getPub_date()));
            entity.setName_cn(item.getTitle()+"("+item.getOntime()+")");
            entity.setLarge_image(item.getCover());

            dbList.add(entity);

            //不能用上面这个比较方法，一使用就报错，原因不明
//                if (entity.getAir_weekday() == mDate)
            if (getWeekOfDate(item.getPub_date()) == mDate)
                _dbList.add(entity);
        }

        //在储存数据之前，删除数据库中的所有数据
        mDaoSession.getBangumiItemDao().deleteAll();

        //存储从网上获取的数据
        mDaoSession.getBangumiItemDao().insertInTx(dbList);

        return _dbList;
    }

    /**
     * 获取当前日期是星期几 输入格式为"yyyy-MM-dd"
     * @param date
     * @return
     */
    private static int getWeekOfDate(String date) {

        //确定日期的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date _date = new Date();
        try {
            _date = sdf.parse(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(_date);
        int i = mCalendar.get(Calendar.DAY_OF_WEEK) - 1 ;
        return (i == 0 ? 7 : i) ;
    }

    /**
     * 获取这一天的日期
     * 为什么不直接使用{@link Date}的构造函数，然后用setHours()，setMinutes(),setSeconds()，去掉时分秒？
     * 用上述的方法获得的日期在使用getTime()之后获得的long和只用当前日期getTime()的long不一致
     * 所以用下述方法去掉日期后面的后缀之后，再重新转换回来
     * @return 当前的日期
     */
    private static Date getToday() {

        //确定日期的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //获取当天的日期
        String today_str = sdf.format(new Date());
        Date today = new Date();
        try {
            today = sdf.parse(today_str);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return today;
    }

    /**
     * 当需要强制刷新时调用，表示需要清除缓存，并重新加载
     */
    @Override
    public void refreshBangumi() {
        mCacheIsDirty = true;
    }
}
