package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 06-04-2018.
 */

public class SingleChatWindowOpponentAdapter extends RecyclerView.Adapter<SingleChatWindowOpponentAdapter.ViewHolder> {
    private Context context;
    private List<SingleChatSet> singleChatSets;
    SingleChatSet singleChatSet;

    public SingleChatWindowOpponentAdapter(Context context, List<SingleChatSet> singleChatSets) {
        this.context = context;
        this.singleChatSets = singleChatSets;
    }

    @Override
    public SingleChatWindowOpponentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.opponentmsg, parent, false);
        SingleChatWindowOpponentAdapter.ViewHolder viewHolderopponent = new SingleChatWindowOpponentAdapter.ViewHolder(v);
        return viewHolderopponent;
    }

    @Override
    public void onBindViewHolder(SingleChatWindowOpponentAdapter.ViewHolder holder, int position) {
        singleChatSet = singleChatSets.get(position);
        holder.textView.setText(singleChatSet.getMessage());
    }

    @Override
    public int getItemCount() {
        return singleChatSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.opponentmsg);
        }
    }
}

