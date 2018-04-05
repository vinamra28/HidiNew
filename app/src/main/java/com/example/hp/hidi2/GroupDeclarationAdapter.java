package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 05-04-2018.
 */

public class GroupDeclarationAdapter extends RecyclerView.Adapter<GroupDeclarationAdapter.ViewHolder> {
    private Context context;
    private List<AddPeopleSet> addPeopleSets;
    AddPeopleSet addPeopleSet;
    ArrayList<String> arrayListpeopleaddedname = new ArrayList<>();
    ArrayList<String> arrayListpeopleaddedimage = new ArrayList<>();
    ArrayList<String> arrayListpeopleUid = new ArrayList<>();
    int index;
    int allindex[];
    int chatposition;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String groupUid;
    SessionManager session;
    String personname, personimageurl, personuid;

    public GroupDeclarationAdapter(Context context, List<AddPeopleSet> addPeopleSets) {
        this.context = context;
        this.addPeopleSets = addPeopleSets;
        allindex = new int[getItemCount()];
        for (int i = 0; i < allindex.length; i++) {
            allindex[i] = 0;
        }
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        session = new SessionManager(context);
    }


    @Override
    public GroupDeclarationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addpeoplecard, parent, false);
        GroupDeclarationAdapter.ViewHolder viewHolder = new GroupDeclarationAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GroupDeclarationAdapter.ViewHolder holder, final int position) {
        addPeopleSet = addPeopleSets.get(holder.getAdapterPosition());
        final AddPeopleSet addPeople = addPeopleSets.get(position);
        holder.txtname.setText(addPeopleSet.getName());
        Picasso.with(context).load(addPeopleSet.getUrl()).into(holder.circleImageView);
        Log.d("in", String.valueOf(index));

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

    public ArrayList<String> getAdapterArrayListName() {
        Log.d("ArrayListadapterName", String.valueOf(arrayListpeopleaddedname));
        return arrayListpeopleaddedname;
    }

    public ArrayList<String> getAdapterArrayListImage() {
        Log.d("ArrayListadapterImage", String.valueOf(arrayListpeopleaddedimage));
        return arrayListpeopleaddedimage;
    }

    public ArrayList<String> getAdapterArrayListUid() {
        Log.d("ArrayListadapter", String.valueOf(arrayListpeopleUid));
        return arrayListpeopleUid;
    }
}
