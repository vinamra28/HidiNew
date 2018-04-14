package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class AllNotificationsView extends AppCompatActivity
{
    String result="";
    SessionManager session;
    ProgressDialog progress;
    NotificationSet notificationSet;
    ArrayList<NotificationSet> notificationSets=new ArrayList<>();
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notifications_view);
        session=new SessionManager(getApplicationContext());
        recyclerView=findViewById(R.id.notificRecycler);
        progress=new ProgressDialog(this);
        progress.setTitle("Loading....");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progress.show();
        new ShowNotifi().execute("http://hidi.org.in/hidi/notification/shownotification.php");
    }
    private class ShowNotifi extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... url)
        {
            return POST(url[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("Result",s);
            progress.dismiss();
            try
            {
                JSONObject jsonObject=new JSONObject(s);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    JSONArray records=jsonObject.getJSONArray("records");
                    if(records.length()!=0)
                    {
                        for(int i=0;i<records.length();i++)
                        {
                            JSONObject obj=records.getJSONObject(i);
                            String msg=obj.getString("message");
                            String typ=obj.getString("type");
                            String time=obj.getString("time");
                            notificationSet=new NotificationSet(typ,msg,time);
                            notificationSets.add(notificationSet);
                        }
                        notificationAdapter=new NotificationAdapter(AllNotificationsView.this,notificationSets);
                        recyclerView.setAdapter(notificationAdapter);
                    }
                }
                else
                {
                    Toast.makeText(AllNotificationsView.this,""+info.getString("message"),Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
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
