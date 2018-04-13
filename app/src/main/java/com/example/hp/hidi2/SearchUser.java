package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

public class SearchUser extends AppCompatActivity implements SearchUserAdapter.SearchedUserListener {

    //    SearchView.SearchAutoComplete searchAutoComplete;
    SearchView searchAutoComplete;
    String result = "";
    ListView lv;
    SessionManager session;
//    HashMap<String, Integer> hashMap = new HashMap<>();
    ArrayList<String> username = new ArrayList<>();
    RecyclerView recyclerViewsearchUser;
    private ActionBar toolBar;
//    TextView ActionBarTitle;
    SearchUserAdapter searchUserAdapter;
    ChatHistorySet chatHistorySet;
    SearchView searchView;
    ProgressDialog progress;
    ArrayList<ChatHistorySet> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        recyclerViewsearchUser  = findViewById(R.id.userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewsearchUser.setLayoutManager(layoutManager);
        recyclerViewsearchUser.setItemAnimator(new DefaultItemAnimator());
        toolBar = getSupportActionBar();
        toolBar.setTitle("");
        progress=new ProgressDialog(this);
        progress.setTitle("Loading...");
        progress.setCancelable(false);
        progress.setIndeterminate(false);
//        toolBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        toolBar.setCustomView(R.layout.set_middle_title);
//        ActionBarTitle = findViewById(R.id.actionBarTitles);
//        ActionBarTitle.setText("");
        searchUserAdapter = new SearchUserAdapter(arrayList,this,this);
//        lv=findViewById(R.id.userList);
//        searchAutoComplete = findViewById(R.id.searchuser);
////        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String click_String = (String) parent.getItemAtPosition(position);
////            }
////        });
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        progress.show();
        new Posts().execute("http://hidi.org.in/hidi/account/showvisitors.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_search1,menu);
        MenuItem searchmenu = menu.findItem(R.id.app_bar_menu_search1);
        searchView = (SearchView) MenuItemCompat.getActionView(searchmenu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUserAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUserAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_menu_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    public String POST(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("request", "all");
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

    @Override
    public void onContactSelected(ChatHistorySet chatHistorySet)
    {
        Bundle bundle =new Bundle();
        bundle.putString("name",chatHistorySet.getName());
        bundle.putInt("uid", Integer.parseInt(chatHistorySet.getUid()));
        Intent intent=new Intent(SearchUser.this,NewUserProfile.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class Posts extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result", result);
            try {
                progress.dismiss();
                JSONObject jsonObject = new JSONObject(result);
                JSONObject info = jsonObject.getJSONObject("info");
                if (info.getString("status").equals("success"))
                {
                    JSONArray details = jsonObject.getJSONArray("details");
                    Log.d("Length",""+details.length());
                    for (int i = 0; i < details.length(); i++)
                    {
                        JSONObject user = details.getJSONObject(i);
                        String username=user.getString("secname");
                        String uid=""+user.getInt("uid");
                        String pic=user.getString("profilepic");
                        if(user.getInt("uid")!=session.getUID())
                        {
                            chatHistorySet=new ChatHistorySet(username,uid,pic);
                            arrayList.add(chatHistorySet);
                        }
//                            arrayList.add(chatHistorySet);
//                        hashMap.put(user.getString("secname"), user.getInt("uid"));
//                        username.add(user.getString("secname"));
                    }
                    Log.d("list",""+arrayList);
                    recyclerViewsearchUser.setAdapter(searchUserAdapter);
//                    final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(SearchUser.this,android.R.layout.simple_list_item_1,username);
//                    lv.setAdapter(arrayAdapter);
//                    searchAutoComplete.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//                    {
//                        @Override
//                        public boolean onQueryTextSubmit(String query)
//                        {
//                            if(username.contains(query))
//                            {
//                                arrayAdapter.getFilter().filter(query);
//                            }
//                            else
//                            {
//                                Toast.makeText(SearchUser.this, "No Users found", Toast.LENGTH_SHORT).show();
//                            }
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onQueryTextChange(String newText)
//                        {
//                            return false;
//                        }
//                    });
//                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                    {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                        {
//                            String name1= (String) parent.getItemAtPosition(position);
//                            String name = name1;
//                            Log.d("User selected",name);
//                            int uid=hashMap.get(name1);
//                            Bundle bundle =new Bundle();
//                            bundle.putString("name",name);
//                            bundle.putInt("uid",uid);
//                            Intent intent=new Intent(SearchUser.this,NewUserProfile.class);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    });
                }
                else
                {
                    Toast.makeText(SearchUser.this, "Unable to find User", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
