package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 05-Apr-18.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<ReplyGet> replyGets=new ArrayList<>();
    private ReplyGet replyGet;

    public ReplyAdapter(Context context, ArrayList<ReplyGet> replyGets)
    {
        this.context = context;
        this.replyGets = replyGets;
    }
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_design,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        replyGet = replyGets.get(holder.getAdapterPosition());
        Picasso.with(context).load(replyGet.getProfilepic()).into(holder.imageView);
        holder.user_name.setText(replyGet.getSec_name());
        holder.user_comment.setText(replyGet.getText());
        holder.reply_button.setText("");
    }
    @Override
    public int getItemCount()

    {
        return replyGets.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView user_name,user_comment,reply_button;
        public ImageView imageView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            user_name = itemView.findViewById(R.id.comment_person_name);
            user_comment = itemView.findViewById(R.id.comment_by_user);
            reply_button = itemView.findViewById(R.id.reply_button);
            imageView = itemView.findViewById(R.id.comment_person_dp);
        }
    }
}
