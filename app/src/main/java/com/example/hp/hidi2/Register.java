package com.example.hp.hidi2;

import android.app.ProgressDialog;
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

public class Register extends AppCompatActivity
{
    ProgressDialog dialog;
    EditText username,passwords,mobileNo;
    Button register;
    int REQUEST_CODE=0;
    String name="",mobino="",passwd="",result="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=findViewById(R.id.registry);
        username=findViewById(R.id.namess);
        passwords=findViewById(R.id.passwordss);
        mobileNo=findViewById(R.id.mobilenoss);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Registering....");
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name=username.getText().toString();
                mobino=mobileNo.getText().toString();
                passwd=passwords.getText().toString();
                if(Pattern.matches("[6789][0-9]{9}",mobino)&&name.length()!=0)
                {
                    if(!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}",passwd))
                    {
                        Toast.makeText(Register.this, "Enter valid paswword", Toast.LENGTH_SHORT).show();
                    }
                    else if(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}",passwd))
                        REQUEST_CODE=22;
                    else
                        REQUEST_CODE=21;
                    dialog.show();
                    new Verification().execute("http://hidi.org.in/hidi/Auth/getotp.php");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter correct entries",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class Verification extends AsyncTask<String,Void,String>
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
            Log.d("Result",result);
            //Toast.makeText(GenOtp.this,""+result,Toast.LENGTH_LONG).show();
            try
            {
                JSONObject ff=new JSONObject(result);
                JSONObject info=ff.getJSONObject("info");
                String ss=info.getString("status");
                Log.d("verifi",ss);
                if(ss.equalsIgnoreCase("success"))
                {
                    dialog.dismiss();
                    Bundle bundle=new Bundle();
                    bundle.putString("mobile",mobino);
                    bundle.putString("username",name);
                    if(REQUEST_CODE==22)
                    {
                        bundle.putString("password",passwd);
                    }
                    bundle.putInt("request",REQUEST_CODE);
                    Intent intent=new Intent(Register.this,VerifyOtp.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),info.getString("message"),Toast.LENGTH_SHORT).show();
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
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);
            String json="";
            JSONObject jsonObject=new JSONObject();
            jsonObject.accumulate("mobileno",mobino);
            jsonObject.accumulate("request","register");
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
