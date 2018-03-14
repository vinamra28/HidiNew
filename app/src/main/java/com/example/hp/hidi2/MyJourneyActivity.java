package com.example.hp.hidi2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import java.util.List;

public class MyJourneyActivity extends AppCompatActivity
{
    String result="";
    SessionManager session;
    GPSTracker gps;
    private List<PostGet> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter_post myAdapter_post;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journey);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        myAdapter_post = new MyAdapter_post(MyJourneyActivity.this,postList,session.getUID());
        recyclerView = findViewById(R.id.recyclerView1);
        gps=new GPSTracker(this);
        new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
    }
    private class Posts extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls)
        {
            return POST(urls[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("result",result);
            try
            {
                JSONObject respnse=new JSONObject(result);
                JSONObject info=respnse.getJSONObject("info");
                JSONArray records=respnse.getJSONArray("records");
                if((info.getString("status")).equals("success"))
                {
                    for(int i=0;i<records.length();i++)
                    {
                        JSONObject posts=records.getJSONObject(i);
                        Log.d("pid",""+posts.getInt("pid"));
                        Log.d("pic",""+posts.getString("pic"));
                        Log.d("likes",""+posts.getInt("likes"));
                        Log.d("dislikes",""+posts.getInt("dislikes"));
                        Log.d("comments",""+posts.getInt("comments"));
                        Log.d("time",""+posts.getString("time"));
                        Log.d("lat",""+posts.getDouble("lat"));
                        Log.d("long",""+posts.getDouble("long"));
                        Log.d("sec_name",""+posts.getString("sec_name"));
                        Log.d("profilepic",""+posts.getString("profilepic"));
                        Log.d("like",""+posts.getInt("like"));
                        Log.d("dislike",""+posts.getInt("dislike"));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(myAdapter_post);
                        PostGet postGet=new PostGet(posts.getString("sec_name"),
                                ""+posts.getInt("like"),""+posts.getInt("comments"),
                                posts.getString("profilepic"),posts.getString("pic"));
                        postList.add(postGet);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
    public String POST(String url)
    {
        InputStream inputStream=null;
        String json="";
        result="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);
            JSONObject jsonObject=new JSONObject();
            jsonObject.accumulate("uid",session.getUID());
            jsonObject.accumulate("request","my");
            jsonObject.accumulate("lat",gps.latitude);
            jsonObject.accumulate("long",gps.longitude);
            jsonObject.accumulate("dist",2);
            json=jsonObject.toString();
            Log.d("json",json);
            StringEntity se=new StringEntity(json);
            Log.d("Entity",""+se);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            Log.d("Post",""+httpPost);
            HttpResponse httpResponse=httpClient.execute(httpPost);
            Log.d("Response",httpResponse.toString());
            inputStream=httpResponse.getEntity().getContent();
            Log.d("inputStream",inputStream.toString());
            if(inputStream!=null)
                result=convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
