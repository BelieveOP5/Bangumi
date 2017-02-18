package com.wen.bangumi.module.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wen.bangumi.base.BaseActivity;
import com.wen.bangumi.util.Injection;
import com.wen.bangumi.R;

import butterknife.BindView;

/**
 * Created by BelieveOP5 on 2017/1/24.
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    @BindView(R.id.login_email_text_input_edit_text)
    public TextInputEditText mUserTextInput;

    @BindView(R.id.login_password_text_input_edit_text)
    public TextInputEditText mPwdTextInput;

    @Override
    public int getLayoutId() {
        return R.layout.login_act;
    }

    @Override
    public void loginActivityFinish() {
        finish();
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        initToolbar();

        new LoginPresenter(
                Injection.provideLoginRepository(),
                this
        );

        initButton();
    }

    private void initToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.login_act_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.sign_in_text);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initButton() {

        Button mSignInBtn = (Button) findViewById(R.id.sign_up_Btn);
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/1/25 完成注册功能
            }
        });

        Button mSignUpBtn = (Button) findViewById(R.id.sign_in_Btn);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().login(
                        mUserTextInput.getText().toString(),
                        mPwdTextInput.getText().toString()
                );
            }
        });
    }

    @Override
    public void showEmailEmptyMessage() {
        // TODO: 2017/1/25  
    }

    @Override
    public void showPwdEmptyMessage() {
        // TODO: 2017/1/25  
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
