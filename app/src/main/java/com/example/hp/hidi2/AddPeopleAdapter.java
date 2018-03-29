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
 * Created by asus on 28-03-2018.
 */

public class AddPeopleAdapter extends RecyclerView.Adapter<AddPeopleAdapter.ViewHolder> {
    private Context context;
    private List<AddPeopleSet> addPeopleSets;
    AddPeopleSet addPeopleSet;

    public AddPeopleAdapter(Context context, List<AddPeopleSet> addPeopleSets) {
        this.context = context;
        this.addPeopleSets = addPeopleSets;
    }


    @Override
    public AddPeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addpeoplecard, parent, false);
        AddPeopleAdapter.ViewHolder viewHolder = new AddPeopleAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddPeopleAdapter.ViewHolder holder, int position) {
        addPeopleSet = addPeopleSets.get(holder.getAdapterPosition());
        holder.txtname.setText(addPeopleSet.getName());
        Picasso.with(context).load(addPeopleSet.getUrl()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return addPeopleSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView circleImageView;
        TextView txtname;
        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.friendimage);
            txtname = itemView.findViewById(R.id.friendname);
        }
    }
}
