package com.example.hp.hidi2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class SelectName extends AppCompatActivity
{
    TextView tv,tv1;
    Spinner spinner;
    Button nextt;
    String result="",hidiName="";
    int uid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_name);
        Bundle bundle=getIntent().getExtras();
        uid=bundle.getInt("UID");
        tv=findViewById(R.id.tvx);
        tv1=findViewById(R.id.textView);
        nextt = findViewById(R.id.next);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.usernames, android.R.layout.select_dialog_item);
        spinner.setAdapter(adapter);
        hidiName=spinner.getSelectedItem().toString();
        if(hidiName.length()!=0)
        {
            tv.setVisibility(View.VISIBLE);
            tv1.setText(hidiName);
        }
        nextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(SelectName.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                nextt.startAnimation(myAnim);
                new HttpAsyncTask().execute("http://hidi.org.in/hidi/account/secname.php");
            }
        });
    }
    private class HttpAsyncTask extends AsyncTask<String,Void,String>
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
            Log.d("Result",result);
            try
            {
                JSONObject res=new JSONObject(result);
                JSONObject info=res.getJSONObject("info");
                String status=info.getString("status");
                if(status.equals("success"))
                {
                    Intent intent = new Intent(SelectName.this, PostActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SelectName.this,"Incorrect",Toast.LENGTH_SHORT).show();
//                    mobile.setText("");
//                    pass.setText("");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

//            Toast.makeText(MainActivity.this,""+result,Toast.LENGTH_LONG).show();

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
            jsonObject.accumulate("UID",uid);
            jsonObject.accumulate("request","update");
            jsonObject.accumulate("secname",hidiName);
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
