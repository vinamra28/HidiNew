package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by asus on 28-03-2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private Context context;
    private List<ChatListSet> chatListSets;
    ChatListSet chatListSet;
    String uid;
    SessionManager session;
    String opponentname;

    public ChatListAdapter(Context context, List<ChatListSet> chatListSets) {
        this.context = context;
        this.chatListSets = chatListSets;
    }

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatfriendcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatListAdapter.ViewHolder holder, int position) {
        chatListSet = chatListSets.get(holder.getAdapterPosition());
        session = new SessionManager(context);
        holder.txtname.setText(chatListSet.getName());
        Picasso.with(context).load(chatListSet.getUrl()).into(holder.circleImageView);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatWindow.class);
                uid = chatListSets.get(holder.getAdapterPosition()).getUid();
                Log.d("opponentuid",uid);
                opponentname = chatListSets.get(holder.getAdapterPosition()).getName();
                Log.d("opponentname",opponentname);
                Bundle bundle = new Bundle();
                bundle.putString("oppname",opponentname);
                bundle.putString("chatwindowuid",uid);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView circleImageView;
        TextView txtname;
        ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.friendimage);
            txtname = itemView.findViewById(R.id.friendname);
            constraintLayout = itemView.findViewById(R.id.constraintlayout);
        }
    }
}
