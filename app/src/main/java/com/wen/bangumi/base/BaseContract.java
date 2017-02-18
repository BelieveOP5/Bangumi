package com.wen.bangumi.base;

/**
 * 基础的连接接口
 * Created by BelieveOP5 on 2017/2/18.
 */

public interface BaseContract {

    interface BaseView<T> {

        void setPresenter(T presenter);

        T getPresenter();

    }

    interface BasePresenter {

        void subscribe();

        void unsubscribe();

    }

}
