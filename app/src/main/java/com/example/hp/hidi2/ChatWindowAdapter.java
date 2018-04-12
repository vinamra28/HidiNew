package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by asus on 05-04-2018.
 */

public class ChatWindowAdapter extends RecyclerView.Adapter<ChatWindowAdapter.ViewHolder> {
    private Context context;
    private List<ChatWindowSet> chatWindowSets;
    ChatWindowSet chatWindowSet;
    HashMap<String,String > hashMapadapter = new HashMap<>();
    SessionManager sessionManager;

    public ChatWindowAdapter(Context context, List<ChatWindowSet> chatWindowSets) {
        this.context = context;
        this.chatWindowSets = chatWindowSets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymsg, parent, false);
        ChatWindowAdapter.ViewHolder viewHoldermine = new ChatWindowAdapter.ViewHolder(v);
        return viewHoldermine;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        chatWindowSet = chatWindowSets.get(position);
        sessionManager = new SessionManager(context);
        Boolean test = chatWindowSet.getUid().equals(sessionManager.getUID()+"");
        Log.d("test",test+"");
        Log.d("chatwindowuid",chatWindowSet.getUid());
        if (test){
            holder.textViewopp.setText(chatWindowSet.getMessage());
            Log.d("check","enter1");
            holder.textViewmy.setVisibility(View.INVISIBLE);
        }
        else {
            holder.textViewmy.setText(chatWindowSet.getMessage());
            Log.d("check","enter2");
            holder.textViewopp.setVisibility(View.INVISIBLE);
        }
        holder.setIsRecyclable(false);
        Log.d("size", chatWindowSets.size() + "");

    }

    @Override
    public int getItemCount() {
        return chatWindowSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewmy , textViewopp;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewmy = itemView.findViewById(R.id.mymsg);
            textViewopp = itemView.findViewById(R.id.opponentmsg);
        }
    }
}

