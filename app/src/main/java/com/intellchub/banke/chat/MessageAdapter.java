package com.intellchub.banke.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MESSAGE_FROM = 3;
    public static final int TYPE_MESSAGE_TO = 4;

    private List<Message> mMessages;
    private int[] mUsernameColors;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        /*if(viewType == TYPE_MESSAGE){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);

        }else*/ if(viewType == Message.TYPE_MESSAGE_FROM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_right, parent, false);
        }else if(viewType == Message.TYPE_MESSAGE_TO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_left, parent, false);
        } else if(viewType == Message.TYPE_LOG){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_log, parent, false);
        }else if(viewType == Message.TYPE_ACTION){
            view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent, false);
        }


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolder(View itemView) {
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
}
