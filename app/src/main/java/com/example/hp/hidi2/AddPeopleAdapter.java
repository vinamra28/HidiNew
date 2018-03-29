package com.example.hp.hidi2;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 28-03-2018.
 */

public class AddPeopleAdapter extends RecyclerView.Adapter<AddPeopleAdapter.ViewHolder> {
    private Context context;
    private List<AddPeopleSet> addPeopleSets;
    AddPeopleSet addPeopleSet;
    ArrayList<String> arrayListpeopleadded = new ArrayList<>();
    int index ;
    int allindex[];
    String personname;

    public AddPeopleAdapter(Context context, List<AddPeopleSet> addPeopleSets) {
        this.context = context;
        this.addPeopleSets = addPeopleSets;
        allindex = new int[getItemCount()];
        for (int i = 0; i< allindex.length; i++){
            allindex[i] = 0;
        }
    }


    @Override
    public AddPeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addpeoplecard, parent, false);
        AddPeopleAdapter.ViewHolder viewHolder = new AddPeopleAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddPeopleAdapter.ViewHolder holder, int position) {
        addPeopleSet = addPeopleSets.get(holder.getAdapterPosition());
        final AddPeopleSet addPeople = addPeopleSets.get(position);
        holder.txtname.setText(addPeopleSet.getName());
        Picasso.with(context).load(addPeopleSet.getUrl()).into(holder.circleImageView);
        Log.d("in", String.valueOf(index));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color;
                if (allindex[holder.getAdapterPosition()] == 0) {
                    color = R.color.colorAccent;
                    allindex[holder.getAdapterPosition()] = 1;
                    Log.d("blue", String.valueOf(allindex[holder.getAdapterPosition()]));
                    personname = addPeople.getName();
                    Log.d("addpersonname", personname);
                    arrayListpeopleadded.add(personname);
                } else {
                    color = R.color.back_color;
                    allindex[holder.getAdapterPosition()] = 0;
                    Log.d("white", String.valueOf(allindex[holder.getAdapterPosition()]));
                    personname = addPeople.getName();
                    Log.d("removepersonname", personname);
                    arrayListpeopleadded.remove(personname);
                }
                holder.constraintLayout.setBackgroundColor(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addPeopleSets.size();
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

    public ArrayList<String> getAdapterArrayList(){
        Log.d("ArrayListadapter", String.valueOf(arrayListpeopleadded));
        return arrayListpeopleadded;
    }
}
