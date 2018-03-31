package com.example.hp.hidi2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UnlockVisitors extends AppCompatActivity
{
    String result="";
    SessionManager session;
    int indexpath;
    int visitor_size;
    ArrayList<String> visitName=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_visitors);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        visitor_size=session.getVisitors();
        indexpath=session.getIndex();
        Log.d("indexpath",""+indexpath);
        Log.d("total visitors",""+visitor_size);
        new myAccount().execute("http://hidi.org.in/hidi/account/showvisitors.php");

    }
    private class myAccount extends AsyncTask<String,Void,String>
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
