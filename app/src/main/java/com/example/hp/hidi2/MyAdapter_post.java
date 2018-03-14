package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter_post extends RecyclerView.Adapter<MyAdapter_post.MyViewHolder>
{
    private Context context;
    private List<PostGet> postList;

    public MyAdapter_post(Context context, List<PostGet> postList)
    {
        this.context = context;
        this.postList = postList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_design, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        PostGet post = postList.get(position);
        Picasso.with(context).load(post.getUser_dp()).into(holder.user_dp);
        holder.user_name.setText(post.getUser_name());
        holder.post_location.setText(post.getPost_location());
        holder.total_favours.setText(post.getTotal_favours());
        holder.do_arguments.setText(post.getDo_arguments());
//        holder.image_posted.setImageDrawable(post.getUser_post_image());
        Picasso.with(context).load(post.getUser_post_image()).into(holder.image_posted);
        holder.do_like.setImageDrawable(post.getDo_like());
        holder.do_dislike.setImageDrawable(post.getDo_dislike());
        holder.image_argument.setImageDrawable(post.getImage_argument());
        holder.do_arguments.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), Arguments.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.image_argument.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), Arguments.class);
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public TextView user_name, post_location, post_content_text, total_favours, total_arguments, do_arguments;
        public ImageView user_dp, image_posted, do_like, do_dislike, image_argument;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            user_dp = itemView.findViewById(R.id.user_dp);
            user_name = itemView.findViewById(R.id.user_name);
            post_location = itemView.findViewById(R.id.post_location);
            total_favours = itemView.findViewById(R.id.favours_number);
            image_posted = itemView.findViewById(R.id.image_posted);
            do_arguments = itemView.findViewById(R.id.do_arguments);
            do_like = itemView.findViewById(R.id.like);
            do_dislike = itemView.findViewById(R.id.dislike);
            image_argument = itemView.findViewById(R.id.arguments_image);


        }
    }
}
