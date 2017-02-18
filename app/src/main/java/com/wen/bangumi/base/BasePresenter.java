package com.wen.bangumi.base;

/**
 * Created by BelieveOP5 on 2017/2/18.
 */

public abstract class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter {

    private T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public abstract void unsubscribe();

}

