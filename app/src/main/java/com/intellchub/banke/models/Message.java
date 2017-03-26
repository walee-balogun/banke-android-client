package com.intellchub.banke.models;

import java.util.List;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class Message {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MESSAGE_FROM = 3;
    public static final int TYPE_MESSAGE_TO = 4;

    private int type;
    private String message;
    private String username;
    private boolean isSelf;
    private String botResponse;
    private Object result;
    private String inReplyTo;
    private int module;
    private List<Object> cards;
    private String session;
    private List<QuickReply> quickReplies;
    private boolean immediatelyGoToNext;
    private int code;
    private int timeStamp;

    private Message() {}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getBotResponse() {
        return botResponse;
    }

    public void setBotResponse(String botResponse) {
        this.botResponse = botResponse;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public List<Object> getCards() {
        return cards;
    }

    public void setCards(List<Object> cards) {
        this.cards = cards;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public List<QuickReply> getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(List<QuickReply> quickReplies) {
        this.quickReplies = quickReplies;
    }

    public boolean isImmediatelyGoToNext() {
        return immediatelyGoToNext;
    }

    public void setImmediatelyGoToNext(boolean immediatelyGoToNext) {
        this.immediatelyGoToNext = immediatelyGoToNext;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }


    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private boolean mIsSelf;
        private String mBotResponse;
        private Object mResult;
        private String mInReplyTo;
        private int mModule;
        private List<Object> mCards;
        private String mSession;
        private List<QuickReply> mQuickReplies;
        private boolean mImmediatelyGoToNext;
        private int mCode;
        private int mTimeStamp;

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

        public Builder botResponse(String botResponse){
            mBotResponse = botResponse;
            return this;
        }

        public Builder result(Object result){
            mResult = result;
            return this;
        }

        public Builder inReplyTo(String inReplyTo){
            mInReplyTo = inReplyTo;
            return this;
        }

        public Builder module(int module){
            mModule = module;
            return this;
        }

        public Builder cards(List<Object> cards){
            mCards = cards;
            return this;
        }

        public Builder session(String session){
            mSession = session;
            return this;
        }

        public Builder quickReplies(List<QuickReply> quickReplies){
            mQuickReplies = quickReplies;
            return this;
        }

        public Builder immediatelyGoToNext(boolean immediatelyGoToNext){
            mImmediatelyGoToNext = immediatelyGoToNext;
            return this;
        }

        public Builder code(int code){
            mCode = code;
            return this;
        }

        public Builder timestamp(int timestamp){
            mTimeStamp = timestamp;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.type = mType;
            message.username = mUsername;
            message.message = mMessage;
            message.isSelf = mIsSelf;
            message.botResponse = mBotResponse;
            message.cards = mCards;
            message.code = mCode;
            message.quickReplies = mQuickReplies;
            message.inReplyTo = mInReplyTo;
            message.timeStamp = mTimeStamp;
            return message;
        }
    }
}
