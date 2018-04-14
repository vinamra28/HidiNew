package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 13-04-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<NotificationSet> notificationSets;

    public NotificationAdapter(Context context, List<NotificationSet> notificationSets) {
        this.context = context;
        this.notificationSets = notificationSets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notificationSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewwhat,textViewwho,textViewtime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewwhat = itemView.findViewById(R.id.whatdid);
            textViewwho = itemView.findViewById(R.id.whatdid);
            textViewtime = itemView.findViewById(R.id.timestamp);
        }
    }
}
