package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

public class AddPeople extends AppCompatActivity {

    String result = "";
    SessionManager session;
    ArrayList<AddPeopleSet> AddPeopleSets=new ArrayList<>();
    RecyclerView recyclerView;
    AddPeopleAdapter chatListAdapter;
    ArrayList<String> arrayListpeople;
    ProgressDialog progressDialog;
    ArrayList<String> arrayListuid = new ArrayList<>();
    ArrayList<String> arrayListprofilepic = new ArrayList<>();
    ArrayList<String> arrayListsecname = new ArrayList<>();
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        session = new SessionManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerViewAddPeople);
        floatingActionButton = findViewById(R.id.proceedgroup);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayListpeople = chatListAdapter.getAdapterArrayList();
                Log.d("arraylistfinal", String.valueOf(arrayListpeople));
                startActivity(new Intent(AddPeople.this,GroupDeclaration.class));
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
    private class hidiList extends AsyncTask<String,Void,String> {
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
                AddPeopleSet AddPeopleSet;
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
                        AddPeopleSet=new AddPeopleSet(profilepic,uid,secname);
                        Log.d("profilepic",profilepic);
                        AddPeopleSets.add(AddPeopleSet);
                    }
                    chatListAdapter = new AddPeopleAdapter(getApplicationContext(),AddPeopleSets);
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
