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
import android.widget.Toast;

import com.intellchub.banke.BuildConfig;
import com.intellchub.banke.ChatterApiInterface;
import com.intellchub.banke.ChatterApplication;
import com.intellchub.banke.R;
import com.intellchub.banke.R2;
import com.intellchub.banke.chat.ChatActivity;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit.Retrofit;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginView{

    public static final String TAG = LoginActivity.class.getSimpleName();
    /*@BindView(R.id.toolbar)
    Toolbar toolbar;*/
    @BindView(R2.id.progress)
    ProgressBar progressBar;
    @BindView(R2.id.btn_join)
    Button btnJoin;
    @BindView(R2.id.et_firstname)
    EditText etFirstname;
    @BindView(R2.id.et_lastname)
    EditText etLastname;
    @BindView(R2.id.et_username)
    EditText etUsername;
    @BindView(R2.id.et_repeat_password)
    EditText etRepeatPassword;
    /*@BindView(R2.id.et_email)
    EditText etEmail;*/
    @BindView(R2.id.et_password)
    EditText etPassword;
    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    ChatterApiInterface chatterApiInterface;
    private LoginPresenter presenter;
    private Socket mSocket;
    private boolean isConnected = true;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
       ((ChatterApplication) getApplication()).getmChatterComponent().inject(this);

        ChatterApplication chatterApplication = (ChatterApplication) getApplication();
        mSocket = chatterApplication.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("success", onSuccess);
        mSocket.on("error", onError);
        mSocket.connect();

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
        //presenter.validateCredentials("", etUsername.getText().toString(), "");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("firstname", "Adewale");
            jsonObject.put("lastname", "Balogun");
            jsonObject.put("username", "whaleswallace");
            jsonObject.put("password", "password");

        }catch (Exception e){
            Log.d(TAG, "Exception: "+e.getMessage());
        }
        Log.d(TAG, "-- jsonObject.toString(): "+jsonObject.toString());
        mSocket.emit("signup", jsonObject);
        //mSocket.on("login", onLogin);
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

    private Object mUsername;
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            if(BuildConfig.DEBUG){
                Log.d(TAG, " -- Socket.EVENT_CONNECT -- ");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        if(null != mUsername)
                            mSocket.emit("login", mUsername);
                        Toast.makeText(getApplicationContext(),
                                R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(BuildConfig.DEBUG){
                Log.d(TAG, " -- Socket.EVENT_DISCONNECT -- ");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(BuildConfig.DEBUG){
                Log.d(TAG, " -- Socket.EVENT_CONNECT_ERROR -- ");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG, "-- SOCKET.EVENT_ON_SUCCESS ---");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d(TAG, "-- Socket Message Data Object: "+data.toString());

                }
            });
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG, "-- SOCKET.EVENT_ON_ERROR ---");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    Log.d(TAG, "-- Socket Message Data Object: "+data.toString());
                }
            });
        }
    };
}
