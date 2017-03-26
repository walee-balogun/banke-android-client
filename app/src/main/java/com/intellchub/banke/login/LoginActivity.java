package com.intellchub.banke.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.intellchub.banke.ChatterApiInterface;
import com.intellchub.banke.ChatterApplication;
import com.intellchub.banke.R;
import com.intellchub.banke.chat.ChatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Retrofit;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginView{

    public static final String TAG = LoginActivity.class.getSimpleName();
    /*@BindView(R.id.toolbar)
    Toolbar toolbar;*/
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.btn_join)
    Button btnJoin;
    /*@BindView(R.id.et_email)
    EditText etEmail;*/
    @BindView(R.id.et_password)
    EditText etPassword;
    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    ChatterApiInterface chatterApiInterface;
    private LoginPresenter presenter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
       ((ChatterApplication) getApplication()).getmChatterComponent().inject(this);

        /*setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        presenter = new LoginPresenterImpl(this, chatterApiInterface);

        /*btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "--- onClick() ---");
            }
        });*/
    }


    @OnClick(R.id.btn_join)
    void onJoinButtonClick(){
        Log.d(TAG, "--- onJoinButtonClick() ---");
        presenter.validateCredentials("", etUsername.getText().toString(), "");
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }


    @Override public void setUsernameError() {
        etUsername.setError(getString(R.string.label_username_error));
    }

    @Override public void setPasswordError() {
        etPassword.setError(getString(R.string.label_password_error));
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showOffline() {

    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideError() {

    }

    @Override
    public void hideOffline() {

    }

    @Override
    public void setEmailError() {
        //etEmail.setError(getResources().getString(R.string.label_email_error));
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, ChatActivity.class));
        finish();
    }
}
