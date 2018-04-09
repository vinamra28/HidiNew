package com.example.hp.hidi2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    String groupUid = "";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<String> arrayListuid = new ArrayList<>();
    int position;
    private ActionBar toolbar;
    String groupChatWindowUid;
    String groupName = "";
    String messages;
    int flag;
    EditText editText;
    ChatWindowAdapter chatWindowAdapter;
    ChatWindowOpponentAdapter chatWindowOpponentAdapter;
    ChatWindowSet chatWindowSet;
    Button button;
    ArrayList<String> arrayListAllthreads;
    ArrayList<ChatWindowSet> chatWindowSets = new ArrayList<>();
    String personUid;
    SessionManager sessionManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        editText = findViewById(R.id.editTexttypeMessage);
        button = findViewById(R.id.buttonSend);
        sessionManager = new SessionManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerViewChatWindow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = getSupportActionBar();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            groupUid = bundle.getString("chatwindowuid");
            Log.d("groupChatWindowUid", groupUid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    groupName = dataSnapshot.child("threads").child(groupUid).child("name").getValue() + "";
                    Log.d("groupname", groupName);
                    Log.d("oppname", bundle.getString("oppname"));
                    toolbar.setTitle(bundle.getString("oppname"));
//                toolbar.setTitle(groupName);
                    arrayListuid = new ArrayList<>();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("threads").child(groupUid).child("messages").getChildren()) {
                        personUid = dataSnapshot1.getKey();
                        arrayListuid.add(personUid);
                    }
                    chatWindowSets = new ArrayList<>();
                    Log.d("arrayListUid", String.valueOf(arrayListuid));
                    for (int j = 0; j < arrayListuid.size(); j++) {
                        String message;
                        String setuid = arrayListuid.get(j);
                        Log.d("setuid", setuid);
                        String msguid = dataSnapshot.child("threads").child(groupUid).child("messages").child(setuid).child("senderId").getValue() + "";
                        Log.d("msguid", msguid);
                        Log.d("cominguid", sessionManager.getUID() + "");
                        if (msguid.equals(sessionManager.getUID() + "")) {
                            message = dataSnapshot.child("threads").child(groupUid).child("messages").child(setuid).child("text").getValue() + "";
                            Log.d("message", message);
                            chatWindowSet = new ChatWindowSet(message);
                            chatWindowSets.add(chatWindowSet);
                            chatWindowAdapter = new ChatWindowAdapter(getApplicationContext(), chatWindowSets);
                            recyclerView.setAdapter(chatWindowAdapter);
                        } else {
                            message = dataSnapshot.child("threads").child(groupUid).child("messages").child(setuid).child("text").getValue() + "";
                            Log.d("message", message);
                            chatWindowSet = new ChatWindowSet(message);
                            chatWindowSets.add(chatWindowSet);
                            chatWindowOpponentAdapter = new ChatWindowOpponentAdapter(getApplicationContext(), chatWindowSets);
                            recyclerView.setAdapter(chatWindowOpponentAdapter);
                        }
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages = editText.getText() + "";
                editText.setText("");
                Log.d("messages", messages);
                String uid = sessionManager.getUID() + "";
                Log.d("uid", uid);
                String secname = sessionManager.getSecname();
                Log.d("secname", secname);
                String timeStamp = "timeStamp";
                Log.d("timeStamp", timeStamp);
                String key = databaseReference.push().getKey();
                Log.d("aman", databaseReference.push().getKey());
                if (groupUid.contains("-")) {
                    Log.d("if", "1");
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderId").setValue(uid);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("text").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
                } else if (!groupUid.contains("-") && groupUid.contains("_")) {
                    Log.d("Else if", "1");
                    String S[] = groupUid.split("_");
                    final String s1 = S[0];
                    final String s2 = S[1];
                    Log.d("ABC", s1 + "+" + s2);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderId").setValue(uid);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderName").setValue(secname);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("text").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
                    databaseReference.child("threads").child(groupUid).child("lastMessage").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("lastSender").setValue("");
                    databaseReference.child("threads").child(groupUid).child("lastUpdated").setValue(System.currentTimeMillis());
                    databaseReference.child("threads").child(groupUid).child("type").setValue("Private Chat");
                    databaseReference.child("users").child(sessionManager.getUID() + "").child("threads").child(groupUid).setValue("true");
                    databaseReference.child("users").child(s2).child("threads").child(groupUid).setValue("true");
                } else {
                    Log.d("Else", "1");
                    arrayListAllthreads = new ArrayList<>();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Void", "Void");
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("users").child(sessionManager.getUID() + "").child("threads").getChildren()) {
                                String allThread = dataSnapshot1.getKey();
                                if (!allThread.contains("-") && allThread.contains("_"))
                                    arrayListAllthreads.add(allThread);
                            }
                            Log.d("All threads", arrayListAllthreads + "");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    Log.d("grp", groupUid + "_" + sessionManager.getUID());
                    for (int i = 0; i < arrayListAllthreads.size(); i++) {
                        String checkuid = arrayListAllthreads.get(i);


                        if (checkuid.equals(groupUid + "_" + sessionManager.getUID())) {
                            flag = 0;
                            break;
                        } else if (checkuid.equals(sessionManager.getUID() + "_" + groupUid)) {
                            flag = 1;
                            break;
                        } else {
                            flag = 2;
                        }
                    }
                    if (flag == 0) {
                        Log.d("condition", "false");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").getChildren()) {
                                    personUid = dataSnapshot1.getKey();
                                    arrayListuid.add(personUid);
                                }
                                chatWindowSets = new ArrayList<>();
                                Log.d("arrayListUid", String.valueOf(arrayListuid));
                                for (int j = 0; j < arrayListuid.size(); j++) {
                                    String message;
                                    String setuid = arrayListuid.get(j);
                                    Log.d("setuid", setuid);
                                    String msguid = dataSnapshot.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(setuid).child("senderId").getValue() + "";
                                    Log.d("msguid", msguid);
                                    Log.d("cominguid", sessionManager.getUID() + "");
                                    if (msguid.equals(sessionManager.getUID() + "")) {
                                        message = dataSnapshot.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(setuid).child("text").getValue() + "";
                                        Log.d("message", message);
                                        chatWindowSet = new ChatWindowSet(message);
                                        chatWindowSets.add(chatWindowSet);
                                        chatWindowAdapter = new ChatWindowAdapter(getApplicationContext(), chatWindowSets);
                                        recyclerView.setAdapter(chatWindowAdapter);
                                    } else {
                                        message = dataSnapshot.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(setuid).child("text").getValue() + "";
                                        Log.d("message", message);
                                        chatWindowSet = new ChatWindowSet(message);
                                        chatWindowSets.add(chatWindowSet);
                                        chatWindowOpponentAdapter = new ChatWindowOpponentAdapter(getApplicationContext(), chatWindowSets);
                                        recyclerView.setAdapter(chatWindowOpponentAdapter);
                                    }
                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("lastMessage").setValue(messages);
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("lastSender").setValue(sessionManager.getSecname());
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("lastUpdated").setValue(System.currentTimeMillis());
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(key).child("senderId").setValue(sessionManager.getUID());
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(key).child("text").setValue(messages);
                        databaseReference.child("threads").child(groupUid + "_" + sessionManager.getUID()).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
                    } else if (flag == 1) {
                        Log.d("condition", "false");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").getChildren()) {
                                    personUid = dataSnapshot1.getKey();
                                    arrayListuid.add(personUid);
                                }
                                chatWindowSets = new ArrayList<>();
                                Log.d("arrayListUid", String.valueOf(arrayListuid));
                                for (int j = 0; j < arrayListuid.size(); j++) {
                                    String message;
                                    String setuid = arrayListuid.get(j);
                                    Log.d("setuid", setuid);
                                    String msguid = dataSnapshot.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(setuid).child("senderId").getValue() + "";
                                    Log.d("msguid", msguid);
                                    Log.d("cominguid", sessionManager.getUID() + "");
                                    if (msguid.equals(sessionManager.getUID() + "")) {
                                        message = dataSnapshot.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(setuid).child("text").getValue() + "";
                                        Log.d("message", message);
                                        chatWindowSet = new ChatWindowSet(message);
                                        chatWindowSets.add(chatWindowSet);
                                        chatWindowAdapter = new ChatWindowAdapter(getApplicationContext(), chatWindowSets);
                                        recyclerView.setAdapter(chatWindowAdapter);
                                    } else {
                                        message = dataSnapshot.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(setuid).child("text").getValue() + "";
                                        Log.d("message", message);
                                        chatWindowSet = new ChatWindowSet(message);
                                        chatWindowSets.add(chatWindowSet);
                                        chatWindowOpponentAdapter = new ChatWindowOpponentAdapter(getApplicationContext(), chatWindowSets);
                                        recyclerView.setAdapter(chatWindowOpponentAdapter);
                                    }
                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("lastMessage").setValue(messages);
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("lastSender").setValue(sessionManager.getSecname());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("lastUpdated").setValue(System.currentTimeMillis());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("senderId").setValue(sessionManager.getUID());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("text").setValue(messages);
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
                    } else {
                        Log.d("condition", "" + arrayListAllthreads.indexOf(groupUid + "_" + sessionManager.getUID()));
                        Log.d("grp", groupUid + "_" + sessionManager.getUID());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("lastMessage").setValue(messages);
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("lastSender").setValue(sessionManager.getSecname());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("lastUpdated").setValue(System.currentTimeMillis());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("senderId").setValue(sessionManager.getUID());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("text").setValue(messages);
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
                        databaseReference.child("threads").child(sessionManager.getUID() + "_" + groupUid).child("type").setValue("Private Chat");
                    }

//                    if(!(arrayListAllthreads.contains(groupUid+"_"+sessionManager.getUID())))
//                    {
//
//                    }
//                    else if (arrayListAllthreads.indexOf(sessionManager.getUID()+"_"+groupUid)==-1)
//                    {
//                        Log.d("condition",""+arrayListAllthreads.indexOf(sessionManager.getUID()+"_"+groupUid));
//                        Log.d("grp",sessionManager.getUID()+"_"+groupUid);
//                        Log.d("condition","trueelseif");
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("lastMessage").setValue(messages);
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("lastSender").setValue(sessionManager.getSecname());
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("lastUpdated").setValue(System.currentTimeMillis());
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("messages").child(key).child("senderId").setValue(sessionManager.getUID());
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("messages").child(key).child("text").setValue(messages);
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
//                        databaseReference.child("threads").child(sessionManager.getUID()+"_"+groupUid).child("type").setValue("Private Chat");
//                    }
//                    else
//                    {
//
//                    }
                }
//                databaseReference.child("threads").child(groupUid).child("messages").child(databaseReference.push().getKey()).child("senderId").setValue(sessionManager.getUID());
//                databaseReference.child("threads").child(groupUid).child("messages").child(databaseReference.push().getKey()).child("senderId").setValue(messageSet);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
