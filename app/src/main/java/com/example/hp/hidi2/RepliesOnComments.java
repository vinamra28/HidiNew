package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RepliesOnComments extends AppCompatActivity
{

    RecyclerView recyclerView;
    SessionManager session;
    EditText myreplies;
    Button sendreplies;
    String result="";
    ReplyGet replyGet;
    ReplyAdapter replyAdapter;
    ProgressDialog progress;
    String time_stamp="",myreply="";
    int cid;
    ArrayList<ReplyGet> replyGets=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies_on_comments);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        replyAdapter=new ReplyAdapter(getApplicationContext(),replyGets);
        Bundle bundle=getIntent().getExtras();
        cid=bundle.getInt("cid");
        recyclerView=findViewById(R.id.recyclerViewrply);
        myreplies=findViewById(R.id.myrply);
        progress = new ProgressDialog(this);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.setTitle("Loading....");
        sendreplies=findViewById(R.id.sendrply);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(replyAdapter);
        progress.show();
        new LoadReplies().execute("http://hidi.org.in/hidi/reply/showreplies.php");
        sendreplies.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date date=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time_stamp=sdf.format(date);
                myreply=myreplies.getText().toString();
                replyGet=new ReplyGet(myreply,session.getSecname(),session.getProfilepic());
                replyGets.add(replyGet);
                new sendReply().execute("http://hidi.org.in/hidi/reply/addreply.php");
                myreplies.setText("");
            }
        });
    }
    private class sendReply extends AsyncTask<String,Void,String>
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
                    myreplies.clearFocus();

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    private class LoadReplies extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... url)
        {
            return POST(url[0]);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("Result",result);
            progress.dismiss();
            try
            {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    JSONArray records=jsonObject.getJSONArray("records");
                    if(records.length()!=0)
                    {
                        for(int i=0;i<records.length();i++)
                        {
                            JSONObject reply=records.getJSONObject(i);
                            String text=reply.getString("text");
                            String sec_name=reply.getString("sec_name");
                            String profilepic=reply.getString("profilepic");
                            replyGet=new ReplyGet(text,sec_name,profilepic);
                            replyGets.add(replyGet);

                        }

                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
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
            jsonObject.accumulate("cid", cid);
            jsonObject.accumulate("text",myreply);
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
    public String POST(String url) {
        InputStream inputStream = null;
        String json = "";
        result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("cid", cid);
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
