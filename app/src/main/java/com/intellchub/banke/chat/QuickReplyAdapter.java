package com.intellchub.banke.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intellchub.banke.R;
import com.intellchub.banke.R2;
import com.intellchub.banke.models.QuickReply;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adewale_MAC on 26/03/2017.
 */

public class QuickReplyAdapter extends RecyclerView.Adapter<QuickReplyAdapter.ContentFilterViewHolder> {

    private final List<QuickReply> quickReplies;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public QuickReplyAdapter(Context context, List<QuickReply> quickReplies) {
        this.context = context;
        this.quickReplies = quickReplies;
    }

    @Override
    public ContentFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quick_reply_item, parent, false);
        return new ContentFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentFilterViewHolder holder, int position) {
        final QuickReply quickReply = quickReplies.get(position);
        holder.tvFilter.setText(quickReply.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(view, quickReply);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return quickReplies.size();
    }

    public QuickReply getItem(int postion){
        return quickReplies.get(postion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ContentFilterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_filter)
        TextView tvFilter;
        public ContentFilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(View view, QuickReply quickReply);
    }
}

