package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 05-04-2018.
 */

public class ChatWindowOpponentAdapter extends RecyclerView.Adapter<ChatWindowOpponentAdapter.ViewHolder> {
    private Context context;
    private List<ChatWindowSet> chatWindowSets;
    ChatWindowSet chatWindowSet;

    public ChatWindowOpponentAdapter(Context context, List<ChatWindowSet> chatWindowSets) {
        this.context = context;
        this.chatWindowSets = chatWindowSets;
    }

    @Override
    public ChatWindowOpponentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.opponentmsg, parent, false);
        ChatWindowOpponentAdapter.ViewHolder viewHolderopponent = new ChatWindowOpponentAdapter.ViewHolder(v);
        return viewHolderopponent;
    }

    @Override
    public void onBindViewHolder(ChatWindowOpponentAdapter.ViewHolder holder, int position) {
        chatWindowSet = chatWindowSets.get(position);
        holder.textView.setText(chatWindowSet.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatWindowSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.opponentmsg);
        }
    }
}
