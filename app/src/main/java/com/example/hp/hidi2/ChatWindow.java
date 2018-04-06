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
    EditText editText;
    ChatWindowAdapter chatWindowAdapter;
    ChatWindowOpponentAdapter chatWindowOpponentAdapter;
    ChatWindowSet chatWindowSet;
    Button button;
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
        Bundle bundle = intent.getExtras();
        groupUid = bundle.getString("chatwindowuid");
        Log.d("groupChatWindowUid", groupUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                groupName = dataSnapshot.child("threads").child(groupUid).child("messages").child(String.valueOf(databaseReference.push())).child()
                groupName = dataSnapshot.child("threads").child(groupUid).child("name").getValue() + "";
                Log.d("groupname", groupName);
                toolbar.setTitle(groupName);
//                 dataSnapshot.child("threads").child(groupUid).child("messages").getChildren()
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("threads").child(groupUid).child("messages").getChildren()) {
                    personUid = dataSnapshot1.getKey();
                    arrayListuid.add(personUid);
                }
                Log.d("arrayListUid", String.valueOf(arrayListuid));
                for (int j = 0; j < arrayListuid.size(); j++) {
                    String setuid = arrayListuid.get(j);
                    Log.d("setuid",setuid);
                    if (setuid.equals(sessionManager.getUID()+"")){
                        String message = dataSnapshot.child("threads").child(groupUid).child("messages").child(setuid).child("text").getValue()+"";
                        Log.d("message",message);
                        chatWindowSet = new ChatWindowSet(message);
                        chatWindowSets.add(chatWindowSet);
                        chatWindowAdapter = new ChatWindowAdapter(getApplicationContext(),chatWindowSets);
                        recyclerView.setAdapter(chatWindowAdapter);
                    }
                    else {
                        String message = dataSnapshot.child("threads").child(groupUid).child("messages").child(setuid).child("text").getValue()+"";
                        Log.d("message",message);
                        chatWindowSet = new ChatWindowSet(message);
                        chatWindowSets.add(chatWindowSet);
                        chatWindowOpponentAdapter = new ChatWindowOpponentAdapter(getApplicationContext(),chatWindowSets);
                        recyclerView.setAdapter(chatWindowOpponentAdapter);
                    }
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages = editText.getText() + "";
                String uid = sessionManager.getUID() + "";
                String secname = sessionManager.getSecname();
                String timeStamp = "timeStamp";
                String key = databaseReference.push().getKey();
                databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderId").setValue(sessionManager.getUID());
                databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
                databaseReference.child("threads").child(groupUid).child("messages").child(key).child("text").setValue(messages);
                databaseReference.child("threads").child(groupUid).child("messages").child(key).child("timeStamp").setValue(System.currentTimeMillis());
//                databaseReference.child("threads").child(groupUid).child("messages").child(databaseReference.push().getKey()).child("senderId").setValue(sessionManager.getUID());
//                databaseReference.child("threads").child(groupUid).child("messages").child(databaseReference.push().getKey()).child("senderId").setValue(messageSet);
            }
        });
    }
}
