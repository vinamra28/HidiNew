package com.example.hp.hidi2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 06-Apr-18.
 */

public class UnlockedAdapter extends RecyclerView.Adapter<UnlockedAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<String> myvisitors=new ArrayList<>();
    public UnlockedAdapter(Context context, ArrayList<String> myvisitors)
    {
        this.context = context;
        this.myvisitors = myvisitors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unlocked_user,parent,false);
        return new UnlockedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.user_name.setText(myvisitors.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount()

    {
        return myvisitors.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView user_name;
        public ViewHolder(View itemView)
        {
            super(itemView);
            user_name = itemView.findViewById(R.id.viewedListUser);
        }
    }
}
