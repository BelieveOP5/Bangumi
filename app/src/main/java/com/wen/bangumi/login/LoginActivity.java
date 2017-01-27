package com.wen.bangumi.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wen.bangumi.event.Event;
import com.wen.bangumi.util.Injection;
import com.wen.bangumi.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by BelieveOP5 on 2017/1/24.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;

    private TextInputEditText mUserTextInput;
    private TextInputEditText mPwdTextInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        setupToolbar();

        mUserTextInput = (TextInputEditText) findViewById(R.id.login_email_text_input_edit_text);
        mPwdTextInput = (TextInputEditText) findViewById(R.id.login_password_text_input_edit_text);

        new LoginPresenter(
                Injection.provideLoginRepository(),
                this,
                Injection.provideSchedulerProvider()
        );

        setupButton();

    }

    @Override
    public void loginActivityFinish() {
        finish();
    }

    private void setupToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.login_act_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.sign_in_text);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupButton() {

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
                mPresenter.login(
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
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
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
