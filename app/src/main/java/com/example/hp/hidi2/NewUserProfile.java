package com.example.hp.hidi2;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView useradmire,userlove,uservisitors,userhidies,actionbars;
    Button following,blocking;
    DonutProgress userpopular;
    ImageView doadmire,dolove,visitorimg;
    String result="",result1="";
    int uid;
    String request="visit";
    private ActionBar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_profile);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        toolBar = getSupportActionBar();
        toolBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolBar.setCustomView(R.layout.set_middle_title);
        actionbars=findViewById(R.id.actionBarTitles);
        useradmire=findViewById(R.id.admireCount1);
        userlove=findViewById(R.id.loveCount1);
        userpopular=findViewById(R.id.popularProgress1);
        uservisitors=findViewById(R.id.visitorsCount1);
        userhidies=findViewById(R.id.hidiCount1);
        following=findViewById(R.id.followuser);
        visitorimg=findViewById(R.id.visitorpic);
        blocking=findViewById(R.id.blockuser);
        doadmire=findViewById(R.id.doadmire);
        dolove=findViewById(R.id.dolove);
        dolove.setTag("0");
        doadmire.setTag("0");
        Bundle bundle=getIntent().getExtras();
        String name=bundle.getString("name");
        actionbars.setText(name);
        uid=bundle.getInt("uid");
        following.setTag("0");
        blocking.setTag("0");
        new HttpAsyncTask().execute("http://hidi.org.in/hidi/account/visit.php");
        dolove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                request="love";
                if(dolove.getTag()=="0")
                {
                    dolove.setTag("1");
                    dolove.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
                else
                {
                    dolove.setTag("0");
                    dolove.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
                new ProfileReq().execute("http://hidi.org.in/hidi/account/update.php");
            }
        });
        doadmire.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                request="admire";
                if(doadmire.getTag()=="0")
                {
                    doadmire.setTag("1");
                    doadmire.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_florist_red_24dp));
                }
                else
                {
                    doadmire.setTag("0");
                    doadmire.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_florist_black_24dp));
                }
                new ProfileReq().execute("http://hidi.org.in/hidi/account/update.php");
            }
        });
        following.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(following.getTag()=="0")
                {
                    following.setTag("1");
                    following.setText("Unfollow");
                    result="";
                    request="follow";
                }
                else
                {
                    following.setTag("0");
                    following.setText("Follow");
                    result="";
                    request="unfollow";
                }
                new ProfileReq().execute("http://hidi.org.in/hidi/account/update.php");
            }
        });
        blocking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(blocking.getTag()=="0")
                {
                    blocking.setTag("1");
                    blocking.setText("Unblock");
                    request="block";
                }
                else
                {
                    blocking.setTag("0");
                    blocking.setText("Block");
                    request="unblock";
                }
                new ProfileReq().execute("http://hidi.org.in/hidi/account/update.php");
            }
        });
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
                if(result.length()==0)
                {
                    Toast.makeText(NewUserProfile.this,"Unable to load",Toast.LENGTH_SHORT).show();
                }
                JSONObject jsonObject=new JSONObject(result);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    JSONObject records = jsonObject.getJSONObject("records");
                    Picasso.with(getApplicationContext()).load(records.getString("profilepic")).into(visitorimg);
                    userpopular.setProgress((float) records.getDouble("popularity"));
                    uservisitors.setText(""+records.getInt("visitors"));
                    userhidies.setText(""+records.getInt("hidies"));
                    if(records.getInt("admires")==1)
                    {
                        doadmire.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_florist_red_24dp));
                    }
                    if(records.getInt("love")==1)
                    {
                        dolove.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    }
                    if(records.getInt("follow")==1)
                    {
                        following.setText("Unfollow");
                    }
                    if(records.getInt("block")==1)
                    {
                        blocking.setText("Unblock");
                    }
                }
                else
                {
                    Toast.makeText(NewUserProfile.this,""+info.getString("message"),Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


    }
    private class ProfileReq extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... url)
        {
            String abc=POST(url[0]);
            Log.d("Post data",abc);
            return abc;

        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("Result",result);

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
            jsonObject.accumulate("request",request);
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
            {
                Log.d("if","if");
                result=convertInputStreamToString(inputStream);
            }
            else
            {
                Log.d("else","else");
                result = "Did not work!";
            }

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
        Log.d("YEs","YEs");
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
        Log.d("result",result);
        return result;
    }
}
