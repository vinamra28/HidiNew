package com.example.hp.hidi2;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class NewUserProfile extends AppCompatActivity
{
    SessionManager session;
    TextView useradmire,userlove,uservisitors,userblocks,userhidies;
    Button following,blocking;
    DonutProgress userpopular;
    ImageView doadmire,dolove,visitorimg;
    String result="";
    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_profile);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        useradmire=findViewById(R.id.admireCount1);
        userlove=findViewById(R.id.loveCount1);
        userpopular=findViewById(R.id.popularProgress1);
        uservisitors=findViewById(R.id.visitorsCount1);
        userblocks =findViewById(R.id.blockCount1);
        userhidies=findViewById(R.id.hidiCount1);
        following=findViewById(R.id.follow);
        visitorimg=findViewById(R.id.visitorpic);
        blocking=findViewById(R.id.blockuser);
        doadmire=findViewById(R.id.doadmire);
        dolove=findViewById(R.id.dolove);
        Bundle bundle=getIntent().getExtras();
        String name=bundle.getString("name");
        uid=bundle.getInt("uid");
        new HttpAsyncTask().execute("http://hidi.org.in/hidi/account/visit.php");
    }

    private class HttpAsyncTask extends AsyncTask<String,Void,String>
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
                    JSONObject records = jsonObject.getJSONObject("records");
                    Picasso.with(getApplicationContext()).load(records.getString("profilepic")).into(visitorimg);
//                    session.accountDetails(records.getInt("admire"),records.getInt("love"),records.getInt("visitors"),
//                            records.getDouble("popularity"),records.getInt("hidies"),records.getInt("blocks"));
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
            jsonObject.accumulate("uidVisitor",session.getUID());
            jsonObject.accumulate("request","visit");
            jsonObject.accumulate("uidProfile",uid);
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
