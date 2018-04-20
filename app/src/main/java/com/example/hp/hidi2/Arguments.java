package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Arguments extends AppCompatActivity {

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    EditText mycmt;
    CommentGet commentGet;
    Button sendcmt;
    SessionManager session;
    String result="";
    String time_stamp="",mycomment;
    ProgressDialog progress;
    int pid;
    private List<CommentGet> commentGetList = new ArrayList<>();
    ActionBar toolbar;
    TextView actionbars;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arguments);
        session=new SessionManager(getApplicationContext());
        toolbar=getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setCustomView(R.layout.set_middle_title);
        actionbars=findViewById(R.id.actionBarTitles);
        TextView abc=findViewById(R.id.clearbtn);
        abc.setVisibility(View.GONE);
        actionbars.setText("Arguments");
        session.checkLogin();
        Bundle bundle=getIntent().getExtras();
        pid=bundle.getInt("pid");
        recyclerView = findViewById(R.id.recyclerViewcmt);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading....");
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        commentAdapter=new CommentAdapter(getApplicationContext(),commentGetList);
        new loadCmt().execute("http://hidi.org.in/hidi/comments/showcomments.php");
        progress.show();
        mycmt=findViewById(R.id.mycmt);
        sendcmt=findViewById(R.id.sendcmt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commentAdapter);
        sendcmt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date date=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time_stamp=sdf.format(date);
                mycomment=mycmt.getText().toString();
                commentGet=new CommentGet(session.getProfilepic(),session.getSecname(),mycomment,"Reply");
                commentGetList.add(commentGet);
                new sendCmt().execute("http://hidi.org.in/hidi/comments/addcomment.php");
                mycmt.setText("");
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mycmt.getWindowToken(),0);
            }
        });
    }
    private class sendCmt extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... url)
        {
            return POST1(url[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("result",result);
            try
            {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    mycmt.clearFocus();

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    private class loadCmt extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... url)
        {
            return POST(url[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result",result);
            if(result.length()!=0)
            {
                progress.dismiss();
                try
                {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject info=jsonObject.getJSONObject("info");
                    if(info.getString("status").equals("success"))
                    {
                        JSONArray records=jsonObject.getJSONArray("records");
                        if(records.length()==0)
                        {
                            Toast.makeText(Arguments.this,"No comments at this moment",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            for(int i=0;i<records.length();i++)
                            {
                                JSONObject cmtno=records.getJSONObject(i);
                                int cid_of_pid=cmtno.getInt("cid");
                                int uid_of_sender=cmtno.getInt("uid");
                                String cmtText=cmtno.getString("text");
                                String timestamp=cmtno.getString("time");
                                String sender_name=cmtno.getString("sec_name");
                                String profilepic=cmtno.getString("profilepic");
                                commentGet=new CommentGet(cid_of_pid,profilepic,sender_name,cmtText,"Reply");
                                commentGetList.add(commentGet);
                            }
                            recyclerView.scrollToPosition(commentAdapter.getItemCount()-1);
                        }
                    }
                    else
                    {
                        Toast.makeText(Arguments.this, "No comments present", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String POST(String url) {
        InputStream inputStream = null;
        String json = "";
        result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("pid", pid);
            json = jsonObject.toString();
            Log.d("json", json);
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
    public String POST1(String url) {
        InputStream inputStream = null;
        String json = "";
        result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("pid", pid);
            jsonObject.accumulate("text",mycomment);
            jsonObject.accumulate("time",time_stamp);
            json = jsonObject.toString();
            Log.d("json", json);
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
}
