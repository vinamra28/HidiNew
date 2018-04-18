package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 27-Mar-18.
 */

public class PostActivityLocation extends AppCompatActivity {
    GPSTracker gps;
    SessionManager session;
    private MyAdapter_post myAdapter_post;

    Button km2, km4, km8;
    private RecyclerView recyclerView;
    ProgressDialog progress;

    private List<PostGet> postList = new ArrayList<>();
    String result = "";
    int dist = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        gps = new GPSTracker(this);
        session.saveLoc(gps.latitude, gps.longitude);
        km2 = findViewById(R.id.twokm);
        km4 = findViewById(R.id.fourkm);
        km8 = findViewById(R.id.eightkm);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading...");
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        recyclerView = findViewById(R.id.recyclerViewkm);
        km2.setTag("0");
        km4.setTag("0");
        km8.setTag("0");
        km2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (km4.getTag() == "1") {
                    km4.setTag("0");
                    km4.setBackgroundResource(R.drawable.buttonborder);
                }
                if (km8.getTag() == "1") {
                    km8.setTag("0");
                    km8.setBackgroundResource(R.drawable.buttonborder);
                }
                if (km2.getTag() == "0") {
                    km2.setTag("1");
                    recyclerView.setAdapter(null);
                    dist = 2;
                    progress.show();
                    new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
                    km2.setBackgroundResource(R.drawable.onclick248);
                } else {
                    km2.setTag("0");
                    km2.setBackgroundResource(R.drawable.buttonborder);
                }
            }
        });
        km4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (km8.getTag() == "1") {
                    km8.setTag("0");
                    km8.setBackgroundResource(R.drawable.buttonborder);
                }
                if (km2.getTag() == "1") {
                    km2.setTag("0");
                    km2.setBackgroundResource(R.drawable.buttonborder);
                }
                if (km4.getTag() == "0") {
                    km4.setTag("1");
                    recyclerView.setAdapter(null);
                    dist = 4;
                    progress.show();
                    new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
                    km4.setBackgroundResource(R.drawable.onclick248);
                } else {
                    km4.setTag("0");
                    km4.setBackgroundResource(R.drawable.buttonborder);
                }
            }
        });
        km8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (km2.getTag() == "1") {
                    km2.setTag("0");
                    km2.setBackgroundResource(R.drawable.buttonborder);
                }
                if (km4.getTag() == "1") {
                    km4.setTag("0");
                    km4.setBackgroundResource(R.drawable.buttonborder);
                }
                if (km8.getTag() == "0") {
                    km8.setTag("1");
                    recyclerView.setAdapter(null);
                    dist = 8;
                    progress.show();
                    new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
                    km8.setBackgroundResource(R.drawable.onclick248);
                } else {
                    km8.setTag("0");
                    km8.setBackgroundResource(R.drawable.buttonborder);
                }
            }
        });
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
            progress.dismiss();
            try {
                PostGet postGet;
                JSONObject respnse = new JSONObject(result);
                JSONObject info = respnse.getJSONObject("info");
                JSONArray records = respnse.getJSONArray("records");
                if ((info.getString("status")).equals("success")) {
                    postList.clear();
                    progress.dismiss();
                    if(records.length()==0)
                    {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "No posts available", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < records.length(); i++) {
                        JSONObject posts = records.getJSONObject(i);
                        Log.d("uid", "" + posts.getInt("uid"));
                        int uid_poster =  posts.getInt("uid");
                        Log.d("pid", "" + posts.getInt("pid"));
                        String pid = "" + posts.getInt("pid");
                        Log.d("pic", "" + posts.getString("pic"));
                        String pic = "" + posts.getString("pic");
                        Log.d("likes", "" + posts.getInt("likes"));
                        String likesc = "" + posts.getInt("likes");
                        Log.d("dislikes", "" + posts.getInt("dislikes"));
                        String dislikesc = "" + posts.getInt("dislikes");
                        Log.d("comments", "" + posts.getInt("comments"));
                        String commentsc = "" + posts.getInt("comments");
                        Log.d("time", "" + posts.getString("time"));
                        String time=posts.getString("time");
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try
                        {
                            Date date=sdf.parse(time);
                            sdf=new SimpleDateFormat("MMMM,dd");
                            time=sdf.format(date);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.d("lat", "" + posts.getDouble("lat"));
                        Log.d("long", "" + posts.getDouble("long"));
                        Log.d("location", posts.getString("location"));
                        String locations = time+" "+posts.getString("location");
                        Log.d("distance", "" + posts.getDouble("distance"));
                        Log.d("sec_name", "" + posts.getString("sec_name"));
                        String name = posts.getString("sec_name");
                        Log.d("profilepic", "" + posts.getString("profilepic"));
                        String profile = posts.getString("profilepic");
                        Log.d("like", "" + posts.getInt("like"));
                        String mlike = "" + posts.getInt("like");
                        Log.d("dislike", "" + posts.getInt("dislike"));
                        String mdisllike = "" + posts.getInt("dislike");
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

//                        postGet=new PostGet(posts.getString("sec_name"),
//                                ""+posts.getInt("like"),""+posts.getInt("comments"),
//                                posts.getString("profilepic"),posts.getString("pic"));
                        postGet = new PostGet(uid_poster,pid, profile, name, locations, pic, likesc, commentsc, dislikesc, mlike, mdisllike);
                        postList.add(postGet);
                        myAdapter_post = new MyAdapter_post(PostActivityLocation.this, postList, session.getUID(), "filter");
                        recyclerView.setAdapter(myAdapter_post);
//                        myAdapter_post.notifyDataSetChanged();
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "No posts available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
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
            jsonObject.accumulate("request", "filter");
            jsonObject.accumulate("lat", gps.latitude);
            jsonObject.accumulate("long", gps.longitude);
            jsonObject.accumulate("dist", dist);
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
