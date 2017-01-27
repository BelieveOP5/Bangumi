package com.wen.bangumi.login;

import com.wen.bangumi.base.BasePresenter;
import com.wen.bangumi.base.BaseView;

/**
 * Created by BelieveOP5 on 2017/1/25.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void loginActivityFinish();

        void showEmailEmptyMessage();

        void showPwdEmptyMessage();

    }

    interface Presenter extends BasePresenter {

        /**
         * 登录账户
         * @param mail 账户名
         * @param pwd 密码
         */
        void login(String mail, String pwd);

    }

}
