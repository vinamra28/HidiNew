package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HidiListChat extends AppCompatActivity
{
    String result = "";
    SessionManager session;
    ArrayList<ChatListSet> chatListSets=new ArrayList<>();
    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    ProgressDialog progressDialog;
    CircleImageView imageViewaddpeople;
    TextView textView;
    ArrayList<String> arrayListuid = new ArrayList<>();
    ArrayList<String> arrayListprofilepic = new ArrayList<>();
    ArrayList<String> arrayListsecname = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidi_list_chat);
        recyclerView = findViewById(R.id.recyclerViewChatFriend);
        imageViewaddpeople = findViewById(R.id.addpeople);
//        imageViewaddpeople.setImageResource(R.drawable.ic_group_add_black_24dp);
//        Picasso.with(HidiListChat.this).load(R.drawable.ic_group_add_black_24dp).into(imageViewaddpeople);
        textView = findViewById(R.id.newgroup);
        session = new SessionManager(getApplicationContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HidiListChat.this,AddPeople.class));
            }
        });
        imageViewaddpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HidiListChat.this,AddPeople.class));
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        new hidiList().execute("http://hidi.org.in/hidi/account/showvisitors.php");
        progressDialog.show();
    }
    public String POST(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("request", "hidies");
            json = jsonObject.toString();
            json = jsonObject.toString();
            Log.d("Json", json);
            StringEntity se = new StringEntity(json);
            Log.d("Entity", "" + se);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            Log.d("Post", "" + httpPost);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.d("Response", httpResponse.toString());
            inputStream = httpResponse.getEntity().getContent();
            Log.d("inputStream", inputStream.toString());
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
    private class hidiList extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                ChatListSet chatListSet;
                JSONObject info =  jsonObject.getJSONObject("info");
                if (info.getString("status").equals("success")){
                    progressDialog.dismiss();
                    JSONObject details = jsonObject.getJSONObject("details");
                    JSONArray records=details.getJSONArray("records");
                    for(int i=0;i<records.length();i++)
                    {
                        JSONObject myhidi=records.getJSONObject(i);
                        String uid= String.valueOf(myhidi.getInt("uid"));
                        arrayListuid.add(uid);
                        String secname=myhidi.getString("secname");
                        arrayListsecname.add(secname);
                        String profilepic=myhidi.getString("profilepic");
                        arrayListprofilepic.add(profilepic);
                        chatListSet=new ChatListSet(profilepic,secname,uid);
                        Log.d("profilepic",profilepic);
                        chatListSets.add(chatListSet);
                    }
                    chatListAdapter = new ChatListAdapter(getApplicationContext(),chatListSets);
                    recyclerView.setAdapter(chatListAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... url) {
            return POST(url[0]);
        }
    }
}
