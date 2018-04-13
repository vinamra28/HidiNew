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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatWindow extends AppCompatActivity {

    String groupUid = "";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference_check,databaseReference_message;
    int position;
    EditText editText;
    ChatWindowAdapter chatWindowAdapter;
    ChatWindowOpponentAdapter chatWindowOpponentAdapter;
    ChatWindowSet chatWindowSet;
    Button button;
    ArrayList<String> arrayListAllMessages;
    ArrayList<ChatWindowSet> chatWindowSets = new ArrayList<>();
    ArrayList<ChatWindowSet> opponentList=new ArrayList<>();
    ArrayList<String> messages=new ArrayList<>();
    ArrayList<String> senderID=new ArrayList<>();
    SessionManager sessionManager;
    RecyclerView recyclerView;
    ActionBar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        sessionManager = new SessionManager(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference_check=databaseReference.child("users").child(sessionManager.getUID()+"").child("threads");
        recyclerView = findViewById(R.id.recyclerViewChatWindow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editText = findViewById(R.id.editTexttypeMessage);
        button = findViewById(R.id.buttonSend);
        toolBar = getSupportActionBar();
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            groupUid = bundle.getString("chatwindowuid");
            Log.d("groupChatWindowUid", groupUid);
            toolBar.setTitle(bundle.getString("oppname"));
            databaseReference_message=databaseReference.child("threads").child(groupUid+"").child("messages");
            Log.d("mwsg",""+databaseReference_message);
            bundle.clear();
            bundle.remove("chatwindowuid");

            databaseReference_check.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> arrayListthreads = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("users").child(sessionManager.getUID()+"").child("threads").getChildren()){
                        String threads = dataSnapshot1.getKey();
                        arrayListthreads.add(threads);
                    }

                    groupUid = addListDisplay(arrayListthreads);
                    if((!(groupUid.contains("_")))&&(!(groupUid.contains("-"))))
                    {
                        groupUid=sessionManager.getUID()+"_"+groupUid;
                    }
                    Log.d("new groupuid",groupUid);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference_message.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Log.d("Dataaa",""+dataSnapshot);
                    arrayListAllMessages = new ArrayList<>();
                    ArrayList<String> senderID1=new ArrayList<>();
                    ArrayList<String> message1=new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        Log.d("!!!!",""+dataSnapshot1);
                        String messagekey = dataSnapshot1.getKey();
                        Log.d("messagekey",messagekey);
                        String senderId = dataSnapshot.child(messagekey).child("senderId").getValue() + "";
                        Log.d("senderId",senderId);
                        String text = dataSnapshot.child(messagekey).child("text").getValue() + "";
                        Log.d("text",text);
                        message1.add(text);
                        senderID1.add(senderId);
                    }
                    Log.d("Sender id",""+senderID1);
                    Log.d("Messages",""+message1);
                    addLists(message1,senderID1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messages = editText.getText()+"";
                editText.setText("");
                if (groupUid.contains("-")){
                    String key = databaseReference.push().getKey();
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderId").setValue(""+sessionManager.getUID());
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderName").setValue(""+sessionManager.getSecname());
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("text").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("timeStamp").setValue(""+System.currentTimeMillis());
                    databaseReference.child("threads").child(groupUid).child("lastMessage").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("lastSender").setValue(""+sessionManager.getSecname());
                    databaseReference.child("threads").child(groupUid).child("lastUpdated").setValue(""+System.currentTimeMillis());
                }
                else if (!groupUid.contains("-")&&groupUid.contains("_")){
                    Log.d("Else if", "1");
                    String S[] = groupUid.split("_");
                    final String s1 = S[0];
                    final String s2 = S[1];
                    Log.d("ABC", s1 + "+" + s2);
                    String key = databaseReference.push().getKey();
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderId").setValue(""+sessionManager.getUID());
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("senderName").setValue(""+sessionManager.getSecname());
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("text").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("messages").child(key).child("timeStamp").setValue(""+System.currentTimeMillis());
                    databaseReference.child("threads").child(groupUid).child("lastMessage").setValue(messages);
                    databaseReference.child("threads").child(groupUid).child("lastSender").setValue(""+sessionManager.getSecname());
                    databaseReference.child("threads").child(groupUid).child("lastUpdated").setValue(""+System.currentTimeMillis());
                    databaseReference.child("threads").child(groupUid).child("type").setValue("Private Chat");
                    databaseReference.child("users").child(sessionManager.getUID() + "").child("threads").child(groupUid).setValue("true");
                    databaseReference.child("users").child(s2).child("threads").child(groupUid).setValue("true");
                }
                bundle.clear();
            }
        });
    }
    void addLists(ArrayList<String> messages,ArrayList<String> senderID)
    {
        this.messages.addAll(messages);
        this.senderID.addAll(senderID);
        Log.d("Sender id",""+senderID);
        Log.d("Messages",""+messages);
        for(int i=0;i<senderID.size();i++)
        {
                Log.d("Checking abc",senderID.get(i).equals(sessionManager.getUID()+"")+"");
                String message=messages.get(i);
                chatWindowSet=new ChatWindowSet(senderID.get(i),messages.get(i));
                chatWindowSets.add(chatWindowSet);
                chatWindowAdapter=new ChatWindowAdapter(getApplicationContext(),chatWindowSets);
                recyclerView.setAdapter(chatWindowAdapter);
                recyclerView.scrollToPosition(chatWindowAdapter.getItemCount()-1);
        }
    }
    String  addListDisplay(ArrayList<String> threads){
        if (threads.contains(groupUid+"_"+sessionManager.getUID())){
            return groupUid+"_"+sessionManager.getUID();
        }
        else if (threads.contains(sessionManager.getUID()+"_"+groupUid)){
            return sessionManager.getUID()+"_"+groupUid;
        }
        else {
            return groupUid;
        }
    }
}