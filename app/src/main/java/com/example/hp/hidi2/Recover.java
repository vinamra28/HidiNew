package com.example.hp.hidi2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Pattern;

public class Recover extends AppCompatActivity {
    Button recovery;
    EditText earlierno,changeno,passwords;
    String result="",txtoldno,txtnewno,txtpaswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        recovery = findViewById(R.id.recovery);
        earlierno=findViewById(R.id.oldno);
        changeno=findViewById(R.id.newno);
        passwords=findViewById(R.id.oldpass);
        recovery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                final Animation myAnim = AnimationUtils.loadAnimation(Recover.this, R.anim.bounce);
//                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
//                myAnim.setInterpolator(interpolator);
//                recovery.startAnimation(myAnim);
                txtoldno=earlierno.getText().toString();
                txtnewno=changeno.getText().toString();
                txtpaswd=passwords.getText().toString();
                if(txtnewno.length()==0||txtpaswd.length()==0||txtoldno.length()==0)
                {
                    Toast.makeText(Recover.this,"Please specify mentioned fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!(Pattern.matches("[6789][0-9]{9}",txtnewno)||Pattern.matches("[6789][0-9]{9}",txtoldno)))
                    {
                        Toast.makeText(Recover.this, "Enter valid mobile no", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new RecoverAccnt().execute("http://hidi.org.in/hidi/Auth/recover.php");
                    }
                }
            }
        });
    }
    private class RecoverAccnt extends AsyncTask<String,Void,String>
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
            if(result.length()==0)
            {
                Toast.makeText(Recover.this,"Failed to connect to server",Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject info=jsonObject.getJSONObject("info");
                    if(info.getString("status").equals("success"))
                    {
                        Toast.makeText(Recover.this,"Recovered",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Recover.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Recover.this,""+info.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String POST(String url)
    {
        InputStream inputStream=null;
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);
            String json="";
            JSONObject jsonObject=new JSONObject();
            jsonObject.accumulate("old",txtoldno);
            jsonObject.accumulate("new",txtnewno);
            jsonObject.accumulate("password",txtpaswd);
            jsonObject.accumulate("request","update");
            json=jsonObject.toString();
            json=jsonObject.toString();
            Log.d("Json",json);
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
        } catch (UnsupportedEncodingException e) {
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
        String result="";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
