package com.intellchub.banke.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellchub.banke.BuildConfig;
import com.intellchub.banke.ChatterApplication;
import com.intellchub.banke.R;
import com.intellchub.banke.R2;
import com.intellchub.banke.login.LoginActivity;
import com.intellchub.banke.models.Message;
import com.intellchub.banke.models.QuickReply;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class ChatActivity extends AppCompatActivity implements QuickReplyAdapter.OnItemClickListener {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.messages)
    RecyclerView mMessagesView;
    @BindView(R2.id.message_input)
    EditText mInputMessageView;
    @BindView(R2.id.send_button)
    ImageButton sendButton;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername;
    private boolean isConnected = true;
    private Socket mSocket;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //startSignIn();
        mMessagesView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this, mMessages, this);
        //mAdapter.setOnItemClickListener(this);

        mMessagesView.setAdapter(mAdapter);
        mInputMessageView = (EditText) findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == mUsername) return;
                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit("typing");
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //sendButton = (ImageButton) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "-- onClick() sendButton ---");
                attemptSend();
            }
        });

        ChatterApplication chatterApplication = (ChatterApplication) getApplication();
        mSocket = chatterApplication.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("welcome", onNewMessage);
        mSocket.on("reply", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("welcome", onNewMessage);
        mSocket.off("reply", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
    }

    @OnClick(R.id.send_button)
    void onSendButtonClick(){
        Log.d(TAG, "--- onSendButtonClick() ---");
        attemptSend();
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void addMessage(Message message) {
        if(message.isSelf()){
            mMessages.add(message);
        }else {
            mMessages.add(message);
        }

        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void attemptSend() {
        if (mUsername == null){
            Log.d(TAG, "mUsername == null: "+String.valueOf(mUsername == null));
            //return;
        }else{
            Log.d(TAG, "mUsername == null: "+String.valueOf(mUsername == null));
        }
        if (!mSocket.connected()){
            Log.d(TAG, "!mSocket.connected(): "+String.valueOf(!mSocket.connected()));
            return;
        }else {
            Log.d(TAG, "!mSocket.connected(): "+String.valueOf(!mSocket.connected()));
        }

        mTyping = false;

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Log.d(TAG, "TextUtils.isEmpty(message): "+String.valueOf(TextUtils.isEmpty(message)));
            mInputMessageView.requestFocus();
            return;
        }else {
            Log.d(TAG, "TextUtils.isEmpty(message): "+String.valueOf(TextUtils.isEmpty(message)));
        }

        mInputMessageView.setText("");

        addMessage(new Message.Builder(Message.TYPE_MESSAGE_FROM)
                //.username(username)
                .message(message)
                //.quickReplies(quickReplies)
                //.timestamp(data.getInt("timeStamp"))
                .isSelf(true)
                .build());
        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put("message", message);

        }catch (JSONException e){
            Log.d(TAG, "JSON Exception: "+e.getMessage());
        }

        Log.d(TAG, "jsonObject.toString(): "+jsonObject.toString());
        mSocket.emit("talk", jsonObject);
    }

    private void startSignIn() {
        mUsername = null;
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        //startActivityForResult(intent, REQUEST_LOGIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void leave() {
        mUsername = null;
        mSocket.disconnect();
        mSocket.connect();
        startSignIn();
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

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
                            mSocket.emit("add user", mUsername);
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

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG, "-- ON NEW MESSAGE ---");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username = "Banke.ai";
                    //String message;
                    Message message;
                    Log.d(TAG, "-- Socket Message Data Object: "+data.toString());
                    try {
                        //username = data.getString("username");

                        //message = data.getString("botResponse");

                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<List<QuickReply>>(){}.getType();
                        Type typeToken = new TypeToken<Message>(){}.getType();
                        List<QuickReply> quickReplies = gson.fromJson(data.get("quickReplies").toString(), collectionType);
                        //message = gson.fromJson(data.toString(), typeToken);
                        message = new Message.Builder(Message.TYPE_MESSAGE_TO)
                                .username(username)
                                .message(data.getString("botResponse"))
                                .quickReplies(quickReplies)
                                .timestamp(data.getInt("timeStamp"))
                                .isSelf(false)
                                .build();
                    } catch (Exception e) {
                        Log.d(TAG, "Exception: "+e.getMessage());
                        return;
                    }

                    removeTyping(username);


                    addMessage(message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };

    @Override
    public void onItemClick(View view, QuickReply quickReply) {
        Log.d(TAG, "-- on Quick reply onItemClick --");
        mInputMessageView.setText("");

        addMessage(new Message.Builder(Message.TYPE_MESSAGE_FROM)
                //.username(username)
                .message(quickReply.getTitle())
                //.quickReplies(quickReplies)
                //.timestamp(data.getInt("timeStamp"))
                .isSelf(true)
                .build());
        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put("message", quickReply.getTitle());

        }catch (JSONException e){
            Log.d(TAG, "JSON Exception: "+e.getMessage());
        }

        Log.d(TAG, "jsonObject.toString(): "+jsonObject.toString());
        mSocket.emit("talk", jsonObject);
    }
}
