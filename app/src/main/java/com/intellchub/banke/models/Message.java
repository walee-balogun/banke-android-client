package com.intellchub.banke.models;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class Message {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MESSAGE_FROM = 3;
    public static final int TYPE_MESSAGE_TO = 4;

    private int mType;
    private String mMessage;
    private String mUsername;
    private boolean mIsSelf;

    private Message() {}

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public boolean isSelf() {
        return mIsSelf;
    }


    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private boolean mIsSelf;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder isSelf(boolean isSelf){
            mIsSelf = isSelf;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mIsSelf = mIsSelf;
            return message;
        }
    }
}
