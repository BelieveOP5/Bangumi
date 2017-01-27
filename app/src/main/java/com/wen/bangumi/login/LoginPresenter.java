package com.wen.bangumi.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wen.bangumi.Bangumi;
import com.wen.bangumi.data.LoginRepository;
import com.wen.bangumi.event.Event;
import com.wen.bangumi.responseentity.Token;
import com.wen.bangumi.user.UserPreferences;
import com.wen.bangumi.util.scheduler.BaseSchedulerProvider;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(@NonNull LoginRepository loginRepository,
                          @NonNull LoginContract.View loginView,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        this.mLoginRepository = checkNotNull(loginRepository, "loginRepository cannot be null!");
        this.mLoginView = checkNotNull(loginView, "loginView cannot be null!");
        this.mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null!");

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
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        new Consumer<Token>() {
                            @Override
                            public void accept(Token token) throws Exception {
                                // TODO: 2017/1/25 存储返回的Token
                                UserPreferences.saveToken(Bangumi.getInstance(), token);
                                EventBus.getDefault().postSticky(new Event.LoginEvent());
                                mLoginView.loginActivityFinish();
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
                                // TODO: 2017/1/25 打开新的Activity
                            }
                        }
                );
        mCompositeDisposable.add(disposable);
    }

}
