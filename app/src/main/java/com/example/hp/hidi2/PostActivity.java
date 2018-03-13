package com.example.hp.hidi2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

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

public class PostActivity extends AppCompatActivity {
    private List<PostGet> postList = new ArrayList<>();
    View.OnTouchListener gestureListener;
    private RecyclerView recyclerView;
    SessionManager session;
    GPSTracker gps;
    boolean bool ;
    private MyAdapter_post myAdapter_post;
    private GestureDetector gestureDetector;
    String result="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        gps=new GPSTracker(this);
        session.saveLoc(gps.latitude,gps.longitude);
        myAdapter_post = new MyAdapter_post(PostActivity.this,postList);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        BottomNavigationView navigation = findViewById(R.id.navigation);
        recyclerView = findViewById(R.id.recyclerView);
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
        recyclerView.setOnTouchListener(gestureListener);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(myAdapter_post);
        new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
        //Post post = new Post("Aditya Bajpai");
//        PostGet post = new PostGet("Goku", "February 22 Ghaziabad", "200", "1");
//        postList.add(post);
//        // post = new Post("Aditya Bajpai");
//        post = new PostGet("Goku", "February 22 Ghaziabad", "200", "1");
//        postList.add(post);
    }

    private void onLeftSwipe() {

//        Intent intent=new Intent(PostActivity.this,HidiChatActivity.class);
//        startActivity(intent);
//        finish();
    }

    private void onRightSwipe()
    {

        Intent intent = new Intent(PostActivity.this, Accounts.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      //  finish();

    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {

                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PostActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PostActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("Home", "Error on gestures");
            }
            return false;
        }
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
            jsonObject.accumulate("request","all");
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
