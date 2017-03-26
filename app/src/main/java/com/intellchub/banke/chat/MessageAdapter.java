package com.intellchub.banke.chat;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intellchub.banke.R;
import com.intellchub.banke.models.Message;

import java.util.List;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = MessageAdapter.class.getSimpleName();
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MESSAGE_FROM = 3;
    public static final int TYPE_MESSAGE_TO = 4;
    private final Context context;
    private QuickReplyAdapter.OnItemClickListener onItemClickListener;

    private List<Message> mMessages;
    private int[] mUsernameColors;

    public MessageAdapter(Context context, List<Message> messages, QuickReplyAdapter.OnItemClickListener setOnItemClickListener) {
        this.context = context;
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        /*if(viewType == TYPE_MESSAGE){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);

        }else*/ if(viewType == Message.TYPE_MESSAGE_FROM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_right, parent, false);
            return new RightViewHolder(view);
        }else if(viewType == Message.TYPE_MESSAGE_TO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_left, parent, false);
            return new LeftViewHolder(view);
        } /*else if(viewType == Message.TYPE_LOG){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_log, parent, false);
        }else if(viewType == Message.TYPE_ACTION){
            view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent, false);
        }


        return new ViewHolder(view);*/

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof LeftViewHolder){
            Log.d(TAG, "-- viewHolder instanceof LeftViewHolder --");
            LeftViewHolder holder = (LeftViewHolder) viewHolder;
            Message message = mMessages.get(position);
            Log.d(TAG, "message.getUsername(): "+message.getUsername());
            Log.d(TAG, "message.getBotResponse(): "+message.getBotResponse());
            Log.d(TAG, "message.getMessage(): "+message.getMessage());
            Log.d(TAG, "mMessages.get(position).getmQuickReplies().get(0).getTitle(): "+mMessages.get(position).getQuickReplies().get(0).getTitle());
            if(message.isSelf()){
                Log.d(TAG, "message.isSelf(): "+String.valueOf(message.isSelf()));
            }else {
                Log.d(TAG, "message.isSelf(): "+String.valueOf(message.isSelf()));
            }
            holder.mUsernameView.setText(message.getUsername());
            holder.mMessageView.setText(message.getMessage());
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            QuickReplyAdapter quickReplyAdapter = new QuickReplyAdapter(context, message.getQuickReplies());
            quickReplyAdapter.setOnItemClickListener(onItemClickListener);
            holder.recyclerView.setAdapter(quickReplyAdapter);

        }else if(viewHolder instanceof RightViewHolder){
            Log.d(TAG, "-- viewHolder instanceof RightViewHolder --");
            RightViewHolder holder = (RightViewHolder) viewHolder;
            Message message = mMessages.get(position);
            Log.d(TAG, "message.getUsername(): "+message.getUsername());
            Log.d(TAG, "message.getBotResponse(): "+message.getBotResponse());
            Log.d(TAG, "message.getMessage(): "+message.getMessage());
            holder.mUsernameView.setText(message.getUsername());
            holder.mMessageView.setText(message.getMessage());
            Log.d(TAG, "mMessages.get(position).getmQuickReplies().get(0).getTitle(): "+mMessages.get(position).getQuickReplies().get(0).getTitle());
            if(message.isSelf()){
                Log.d(TAG, "message.isSelf(): "+String.valueOf(message.isSelf()));
            }else {
                Log.d(TAG, "message.isSelf(): "+String.valueOf(message.isSelf()));
            }
        }
        Message message = mMessages.get(position);
        Log.d(TAG, "mMessages.get(position).getmQuickReplies().get(0).getTitle(): "+mMessages.get(position).getQuickReplies().get(0).getTitle());
        if(message.isSelf()){
            Log.d(TAG, "message.isSelf(): "+String.valueOf(message.isSelf()));
        }else {
            Log.d(TAG, "message.isSelf(): "+String.valueOf(message.isSelf()));
        }

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        /*if(isMessageFrom(position)){
            return TYPE_MESSAGE_FROM;
        }else if(isMessageTo(position)){
            return TYPE_MESSAGE_TO;
        }else{

        }*/
        return mMessages.get(position).getType();
    }

    public Message getItem(int positon){
       return mMessages.get(positon);
    }

    public boolean isMessageFrom(int position){
        return getItem(position).isSelf();
    }

    public boolean isMessageTo(int position){
        return getItem(position).isSelf();
    }

    public QuickReplyAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(QuickReplyAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;

        public RightViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.tv_username);
            mMessageView = (TextView) itemView.findViewById(R.id.tv_message);
        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
            //mUsernameView.setTextColor(getUsernameColor(username));
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        public TextView mUsernameView;
        public TextView mMessageView;
        public RecyclerView recyclerView;

        public LeftViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.tv_username);
            mMessageView = (TextView) itemView.findViewById(R.id.tv_message);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_quick_reply);
        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
            //mUsernameView.setTextColor(getUsernameColor(username));
        }


    }
}
