package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 13-04-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<NotificationSet> notificationSets;
    NotificationSet notificationSet;

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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        notificationSet=notificationSets.get(position);
        holder.textViewwhat.setText(notificationSet.getWhatdid());
        holder.textViewwho.setText(notificationSet.getWhodid());
        String times=notificationSet.getTimestamp();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {
            Date date = sdf.parse(times);
            sdf=new SimpleDateFormat("hh:mm a");
            holder.textViewtime.setText(""+sdf.format(date));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

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
            textViewwho = itemView.findViewById(R.id.whodid);
            textViewtime = itemView.findViewById(R.id.timestamp);
        }
    }
}
