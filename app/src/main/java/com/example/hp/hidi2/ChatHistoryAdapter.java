package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 31-03-2018.
 */

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {
    private Context context;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<ChatHistorySet> chatHistorySets;
    private ChatHistorySet chatHistorySet;
    SessionManager session;
    ArrayList<String> arrayListUid = new ArrayList<>();
    int chatposition;
    String groupUid;

    public ChatHistoryAdapter(Context context, List<ChatHistorySet> chatHistorySets) {
        this.context = context;
        this.chatHistorySets = chatHistorySets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chathistorycard, parent, false);
        ChatHistoryAdapter.ViewHolder viewHolder = new ChatHistoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        session = new SessionManager(context);
        chatHistorySet = chatHistorySets.get(holder.getAdapterPosition());
        holder.textView.setText(chatHistorySet.getName());
        holder.txtlastmsgname.setText(chatHistorySet.getLastSender());
        holder.txtlastmsg.setText(chatHistorySet.getLastmsg());
        holder.lasttime.setText(chatHistorySet.getLasttime());
        Picasso.with(context).load(chatHistorySet.getProfileurl()).into(holder.circleImageView);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("users").child(session.getUID() + "").child("threads").getChildren()) {
                            String uid = dataSnapshot1.getKey();
                            Log.d("uid", uid);
                            arrayListUid.add(uid);
                        }
                        Log.d("ArrayList", arrayListUid + "");
                        chatposition = position;
                        groupUid = arrayListUid.get(chatposition);
                        String name = "";
                        if (!(groupUid.contains("-"))){
                            String S[] = groupUid.split("_");
                            String s1 = S[0];
                            String s2 = S[1];
                            if (s1.equals(session.getUID()+"")){
                                name = dataSnapshot.child("users").child(s2).child("username").getValue()+"";
                                Log.d("s2name",name);
                            }
                            else {
                                name = dataSnapshot.child("users").child(s1).child("username").getValue()+"";
                                Log.d("s1name",s1);
                            }
                        }
                        else {
                            name = dataSnapshot.child("threads").child(groupUid).child("name").getValue()+"";
                        }
                        Log.d("chat position", chatposition + "");
                        Log.d("oppname", name);
                        Log.d("groupUid", groupUid);
                        Bundle bundle = new Bundle();
                        bundle.putString("oppname", name);
                        bundle.putString("chatwindowuid", groupUid);
                        Intent intent = new Intent(context, ChatWindow.class);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatHistorySets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView;
        TextView txtlastmsgname, txtlastmsg,lasttime;
        RelativeLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.chathistoryimage);
            textView = itemView.findViewById(R.id.chathistoryname);
            constraintLayout = itemView.findViewById(R.id.chatHistoryCard);
            txtlastmsgname = itemView.findViewById(R.id.lastmsgname);
            txtlastmsg = itemView.findViewById(R.id.lastmsg);
            lasttime = itemView.findViewById(R.id.timeStampUpdated);
        }
    }
}
