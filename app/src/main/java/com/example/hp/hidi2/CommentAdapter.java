package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HP on 15-Mar-18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<CommentGet> commentGets;
    public CommentGet commentGet;

    public CommentAdapter(Context context, List<CommentGet> commentGets) {
        this.context = context;
        this.commentGets = commentGets;
    }

    public CommentAdapter(List<CommentGet> commentGets) {
        this.commentGets = commentGets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        commentGet = commentGets.get(holder.getAdapterPosition());
        Picasso.with(context).load("url").into(holder.imageView);
        holder.user_name.setText(commentGet.getUser_name());
        holder.user_comment.setText(commentGet.getUser_comment());
        holder.reply_button.setText(commentGet.getReply_commnet());
    }

    @Override
    public int getItemCount()
    {
        return commentGets.size();
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
