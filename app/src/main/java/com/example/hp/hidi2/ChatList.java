package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatList extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<ChatHistorySet> chatHistorySets;
    ChatHistoryAdapter chatHistoryAdapter;
    SessionManager session;
    ChatHistorySet chatHistorySet;
    ArrayList<String> arrayListUploadUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        floatingActionButton = findViewById(R.id.addchat);
        session = new SessionManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerViewChatHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        chatHistorySets = new ArrayList<>();
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatList.this, HidiListChat.class));
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                progressDialog.dismiss();
                Log.d("message", dataSnapshot.child("users").child(String.valueOf(session.getUID())).child("threads").getChildren() + "");
                Log.d("uid", session.getUID() + "");
                for (DataSnapshot postSnapshot : dataSnapshot.child("users").child(String.valueOf(session.getUID())).child("threads").getChildren())
                {
                    Log.d("postSnampshot", postSnapshot + "");
                    String uploadurl = postSnapshot.getKey();
                    Log.d("uploadurl", uploadurl);
                    arrayListUploadUrl.add(uploadurl);
                }
                Log.d("ArrayList", arrayListUploadUrl + "");
                for (int j = 0; j < arrayListUploadUrl.size(); j++)
                {
                    final String groupUrl = arrayListUploadUrl.get(j);
                    Log.d("groupUrl", groupUrl);
                    databaseReference.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            String url = dataSnapshot.child("threads").child(groupUrl).child("groupImage").getValue() + "";
                            Log.d("url", url);
                            String groupname = dataSnapshot.child("threads").child(groupUrl).child("name").getValue() + "";
                            Log.d("group", groupname);
                            chatHistorySet = new ChatHistorySet(groupname, url);
                            chatHistorySets.add(chatHistorySet);
                            chatHistoryAdapter = new ChatHistoryAdapter(getApplicationContext(), chatHistorySets);
                            recyclerView.setAdapter(chatHistoryAdapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }
}
