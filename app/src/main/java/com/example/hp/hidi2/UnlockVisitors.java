package com.example.hp.hidi2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class UnlockVisitors extends AppCompatActivity
{
    String result="";
    SessionManager session;
    int indexpath;
    int visitor_size;
    LinearLayout first,second,third;
    TextView nmfirst,nmsecond,nmthird;
    ImageButton unlock;
    Button showUnlocked;
    ArrayList<String> visitName=new ArrayList<String>();
    ArrayList<String> viewed=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_visitors);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        first=findViewById(R.id.first_name);
        nmfirst=findViewById(R.id.hidi_name_one);
        second=findViewById(R.id.second_name);
        nmsecond=findViewById(R.id.hidi_name_two);
        third=findViewById(R.id.third_name);
        nmthird=findViewById(R.id.hidi_name_three);
        visitor_size=session.getVisitors();
        unlock=findViewById(R.id.lock_btn);
        showUnlocked=findViewById(R.id.show_unlocked_visitors);
        indexpath=session.getIndex();
        Log.d("indexpath",""+indexpath);
        Log.d("total visitors",""+visitor_size);
        new AllVisitors().execute("http://hidi.org.in/hidi/account/showvisitors.php");
        showUnlocked.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("viewed",viewed);
                Intent intent=new Intent(UnlockVisitors.this,ShowUnlockedVisitors.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        unlock.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(indexpath<visitor_size)
                {
                    if((visitor_size-indexpath)>=3)
                    {
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        nmfirst.setText(visitName.get(indexpath+0));viewed.add(visitName.get(indexpath+0));
                        nmsecond.setText(visitName.get(indexpath+1));viewed.add(visitName.get(indexpath+1));
                        nmthird.setText(visitName.get(indexpath+2));viewed.add(visitName.get(indexpath+2));
                        indexpath=indexpath+3;
                    }
                    else if((visitor_size-indexpath)==2)
                    {
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.INVISIBLE);
                        nmfirst.setText(visitName.get(indexpath+0));viewed.add(visitName.get(indexpath+0));
                        nmsecond.setText(visitName.get(indexpath+1));viewed.add(visitName.get(indexpath+1));
                        indexpath=indexpath+2;
                    }
                    else if((visitor_size-indexpath)==1)
                    {
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.INVISIBLE);
                        third.setVisibility(View.INVISIBLE);
                        nmfirst.setText(visitName.get(indexpath+0));viewed.add(visitName.get(indexpath+0));
                        indexpath=indexpath+1;
                    }
                    session.updateIndex(indexpath);
                    Log.d("indexpath",indexpath+"");
                    Log.d("difference",""+(visitor_size-indexpath));
                    new IndexUpdate().execute("http://hidi.org.in/hidi/account/updateindex.php");
                }
                else
                {
                    first.setVisibility(View.INVISIBLE);
                    second.setVisibility(View.VISIBLE);
                    third.setVisibility(View.INVISIBLE);
                    nmsecond.setText("No visitors left to be viewed");
                    nmsecond.setTypeface(Typeface.DEFAULT_BOLD);
                    nmsecond.setTextSize(20f);
                }
            }
        });

    }
    private class AllVisitors extends AsyncTask<String,Void,String>
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
            Log.d("result",result);
            try
            {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    JSONObject details=jsonObject.getJSONObject("details");
                    JSONArray records=details.getJSONArray("records");
                    if(records.length()==0)
                    {
                        Toast.makeText(UnlockVisitors.this, "No visitors", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for(int i=0;i<records.length();i++)
                        {
                            JSONObject user = records.getJSONObject(i);
                            visitName.add(user.getString("secname"));
                        }
                        Log.d("visitors",""+visitName);
                        for(int i=0;i<indexpath;i++)
                        {
                            viewed.add(visitName.get(i));
                        }
                    }
                }
                else
                {
                    Toast.makeText(UnlockVisitors.this,""+info.getString("message"),Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
    private class IndexUpdate extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Result",result);
        }

        @Override
        protected String doInBackground(String... url) {
            return POST1(url[0]);
        }
    }
    public String POST(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("request", "visitors");
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
    public String POST1(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("indexpath", indexpath);
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
}
