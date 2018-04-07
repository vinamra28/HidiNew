package com.example.hp.hidi2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleChat extends AppCompatActivity {

    private ActionBar toolBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    EditText editText;
    SingleChatAdapter singleChatAdapter;
    SingleChatWindowOpponentAdapter singleChatWindowOpponentAdapter;
    SingleChatSet singleChatSet;
    ArrayList<SingleChatSet> singleChatSets = new ArrayList<>();
    Button button;
    String messages;
    SessionManager sessionManager;
    RecyclerView recyclerView;
    String opponent_name,opponent_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        opponent_name = bundle.getString("opponentname");
        opponent_uid = bundle.getString("opponentuid");
        editText = findViewById(R.id.editTexttypeMessage);
        button = findViewById(R.id.buttonSend);
        sessionManager = new SessionManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerViewChatWindow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolBar = getSupportActionBar();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        toolBar.setTitle(opponent_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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
                databaseReference.child("users").child(sessionManager.getUID()+"").child("threads").child(sessionManager.getUID()+"_"+opponent_uid).setValue(true);
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("lastMessage").setValue("");
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("lastSender").setValue("");
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("lastUpdated").setValue("");
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("type").setValue("Private Chat");
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("messages").child(key).child("senderID").setValue(sessionManager.getUID());
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("messages").child(key).child("senderName").setValue(sessionManager.getSecname());
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("messages").child(key).child("text").setValue(messages);
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("messages").child(key).child("timestamp").setValue(System.currentTimeMillis());
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("participants").child(sessionManager.getUID()+"").setValue(sessionManager.getSecname());
                databaseReference.child("threads").child(sessionManager.getUID()+"_"+opponent_uid).child("participants").child(opponent_uid).setValue(opponent_name);
            }
        });
    }
}
