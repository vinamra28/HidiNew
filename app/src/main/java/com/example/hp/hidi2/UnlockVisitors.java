package com.example.hp.hidi2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UnlockVisitors extends AppCompatActivity
{
    String result="";
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_visitors);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
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
            return url[0];
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }
}
