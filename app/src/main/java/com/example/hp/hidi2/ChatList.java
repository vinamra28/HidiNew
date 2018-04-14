package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
    int i = 0;
    public String groupUrl;
    Bundle bundle;
    ChatHistorySet chatHistorySet;
    ArrayList<String> arrayListUploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        if(isNetworkAvailable()){

        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setMessage("No internet connection on your device. Would you like to enable it?")
                    .setTitle("No Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton(" Enable Internet ",
                            new DialogInterface.OnClickListener()
                            {

                                public void onClick(DialogInterface dialog, int id)
                                {


                                    Intent in = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    startActivity(in);

                                }
                            });

            alertDialogBuilder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        floatingActionButton = findViewById(R.id.addchat);
        session = new SessionManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerViewChatHistory);
        recyclerView.setHasFixedSize(true);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            i = bundle.getInt("key");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(ChatList.this, HidiListChat.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatHistorySets = new ArrayList<>();
                Log.d("chatHistorySize", chatHistorySets.size() + "");
                String opponentname, opponentpic;
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("users").child(session.getUID() + "").child("threads").getChildren()) {
                    groupUrl = dataSnapshot1.getKey();
                    String personalurl = groupUrl;
                    Log.d("groupUrl", groupUrl);
                    if (!groupUrl.contains("-")) {
                        //checking for one to one chat
                        String S[] = groupUrl.split("_");
                        String s1 = S[0];
                        Log.d("s1", s1);
                        String s2 = S[1];
                        Log.d("s2", s2);
                        //checking for opponent name
                        if (s1.equals(session.getUID() + "")) {
                            groupUrl = s2;
                        } else {
                            groupUrl = s1;
                        }
                        opponentname = dataSnapshot.child("users").child(groupUrl).child("username").getValue() + "";
                        Log.d("opponentname", opponentname);
                        opponentpic = dataSnapshot.child("users").child(groupUrl).child("profilepic").getValue() + "";
                        Log.d("opponentpic", opponentpic);
                    }
                    //making group
                    else {
                        groupUrl = groupUrl;
                        opponentname = dataSnapshot.child("threads").child(groupUrl).child("name").getValue() + "";
                        Log.d("opponentname", opponentname);
                        opponentpic = dataSnapshot.child("threads").child(groupUrl).child("groupImage").getValue() + "";
                        Log.d("opponentpic", opponentpic);
                    }
                    chatHistorySet = new ChatHistorySet(opponentname, opponentpic);
                    chatHistorySets.add(chatHistorySet);
                    chatHistoryAdapter = new ChatHistoryAdapter(getApplicationContext(), chatHistorySets);
                    recyclerView.setAdapter(chatHistoryAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
