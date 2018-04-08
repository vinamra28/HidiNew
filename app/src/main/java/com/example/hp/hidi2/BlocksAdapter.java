package com.example.hp.hidi2;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 08-Apr-18.
 */

public class BlocksAdapter extends RecyclerView.Adapter<BlocksAdapter.ViewHolder>
{
    private Context context;
    private List<BlocksGet> blocksGetList=new ArrayList<>();
    BlocksGet blocksGet;


    public BlocksAdapter(Context context, List<BlocksGet> blocksGetList)
    {
        this.context = context;
        this.blocksGetList = blocksGetList;
    }
    @Override
    public BlocksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_visitor, parent, false);
        BlocksAdapter.ViewHolder viewHolder = new BlocksAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        blocksGet=blocksGetList.get(holder.getAdapterPosition());
        holder.txtname.setText(blocksGet.getSecname());
        Picasso.with(context).load(blocksGet.getUser_pic()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return blocksGetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView circleImageView;
        TextView txtname;
        public ViewHolder(View itemView)
        {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.blocked_pic);
            txtname = itemView.findViewById(R.id.blocked_name);
        }
    }
}
