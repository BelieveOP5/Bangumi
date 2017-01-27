package com.wen.bangumi.login;

import com.wen.bangumi.base.BasePresenter;
import com.wen.bangumi.base.BaseView;

/**
 * Created by BelieveOP5 on 2017/1/25.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        /**
         * 登录完成，关闭登录界面
         */
        void loginActivityFinish();

        /**
         * 显示账户名不能为空的消息
         */
        void showEmailEmptyMessage();

        /**
         * 显示密码不能为空的消息
         */
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
