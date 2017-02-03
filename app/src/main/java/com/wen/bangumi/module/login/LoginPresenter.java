package com.wen.bangumi.module.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wen.bangumi.Bangumi;
import com.wen.bangumi.data.LoginRepository;
import com.wen.bangumi.event.Event;
import com.wen.bangumi.entity.user.Token;
import com.wen.bangumi.module.user.UserPreferences;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/1/25.
 */

public class LoginPresenter implements LoginContract.Presenter{

    @NonNull
    private final LoginRepository mLoginRepository;

    @NonNull
    private final LoginContract.View mLoginView;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(@NonNull LoginRepository loginRepository,
                          @NonNull LoginContract.View loginView) {
        this.mLoginRepository = checkNotNull(loginRepository, "loginRepository cannot be null!");
        this.mLoginView = checkNotNull(loginView, "loginView cannot be null!");

        this.mCompositeDisposable = new CompositeDisposable();
        this.mLoginView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void login(String mail, String pwd) {

        if (TextUtils.isEmpty(mail)) {
            mLoginView.showEmailEmptyMessage();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            mLoginView.showPwdEmptyMessage();
            return;
        }

        mCompositeDisposable.clear();
        Disposable disposable = mLoginRepository
                .login(mail, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Token>() {
                            @Override
                            public void accept(Token token) throws Exception {

                                //先存储Bangumi返回的个人数据
                                UserPreferences.saveToken(Bangumi.getInstance(), token);

                                //发送事件，提醒nav_view以及user_home_page读取数据
                                EventBus.getDefault().postSticky(new Event.LoginEvent());

                                //关闭当前登录界面
                                mLoginView.loginActivityFinish();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                // TODO: 2017/1/27 检验错误，对错误进行判断
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                // TODO: 2017/1/27 关闭登录中提示框
                            }
                        }
                );
        mCompositeDisposable.add(disposable);
    }

}
