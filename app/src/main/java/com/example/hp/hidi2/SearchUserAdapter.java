package com.example.hp.hidi2;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 11-04-2018.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> implements Filterable {
    private List<ChatHistorySet> searchUserSets;
    private List<ChatHistorySet> searchUserSetsFiltered;
    private Context context;
    private ChatHistorySet chatHistorySet;
    private SearchedUserListener listener;

    public SearchUserAdapter(List<ChatHistorySet> chatHistorySets, Context context,SearchedUserListener listener) {
        this.searchUserSets = chatHistorySets;
        this.listener=listener;
        this.context = context;
        this.searchUserSetsFiltered = chatHistorySets;
    }

    public SearchUserAdapter(List<ChatHistorySet> searchUserSets, List<ChatHistorySet> searchUserSetsFiltered, Context context, ChatHistorySet chatHistorySet) {
        this.searchUserSets = searchUserSets;
        this.searchUserSetsFiltered = searchUserSetsFiltered;
        this.context = context;
        this.chatHistorySet = chatHistorySet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chathistorycard,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        chatHistorySet = searchUserSetsFiltered.get(holder.getAdapterPosition());
        holder.textView.setText(chatHistorySet.getName());
        Picasso.with(context).load(chatHistorySet.getProfileurl()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return searchUserSetsFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.chathistoryimage);
            textView = itemView.findViewById(R.id.chathistoryname);
            constraintLayout = itemView.findViewById(R.id.chatHistoryCard);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onContactSelected(searchUserSetsFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    searchUserSetsFiltered = searchUserSets;
                }
                else {
                    List<ChatHistorySet> filteredList = new ArrayList<>();
                    for (ChatHistorySet row : searchUserSets){
                        if (row.getName().contains(charString))
                            filteredList.add(row);
                    }
                    searchUserSetsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = searchUserSetsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchUserSetsFiltered = (List<ChatHistorySet>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface SearchedUserListener
    {
        void onContactSelected(ChatHistorySet chatHistorySet);
    }
}
