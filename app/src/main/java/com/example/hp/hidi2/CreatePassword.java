package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class CreatePassword extends AppCompatActivity
{
    ProgressDialog progress;
    SessionManager session;
    int uid;
    String result="",request="get";
    String oldps="",newps="",cnfrmps="";
    Button hit;
    EditText old,newp,cnfrm;
    TextInputLayout oness,twoss,threess;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        session=new SessionManager(getApplicationContext());
        uid=session.getUID();
        old=findViewById(R.id.prevps);
        newp=findViewById(R.id.newpswd);
        cnfrm=findViewById(R.id.cnfnewpassword);
        oness=findViewById(R.id.ones);
        twoss=findViewById(R.id.twos);
        threess=findViewById(R.id.threes);
        hit=findViewById(R.id.changepassword);
        old.setVisibility(View.INVISIBLE);
        newp.setVisibility(View.INVISIBLE);
        cnfrm.setVisibility(View.INVISIBLE);
        hit.setVisibility(View.INVISIBLE);
        progress=new ProgressDialog(this);
        progress.setTitle("Loading....");
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        progress.show();
        new Create().execute("http://hidi.org.in/hidi/Auth/pass.php");
    }
    private class Create extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... url)
        {
            return POST(url[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("Result",s);
            progress.dismiss();
            try
            {
                JSONObject jsonObject=new JSONObject(s);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    String olddd=jsonObject.getJSONObject("records").getString("password");
                    if(olddd.length()!=0)
                    {
                        old.setVisibility(View.VISIBLE);
                        newp.setVisibility(View.VISIBLE);
                        cnfrm.setVisibility(View.VISIBLE);
                        hit.setText("Update");
                        hit.setVisibility(View.VISIBLE);
                        hit.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                oldps=old.getText().toString();
                                newps=newp.getText().toString();
                                cnfrmps=cnfrm.getText().toString();
                                if(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}", oldps))
                                {
                                    if(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}", newps))
                                    {
                                        if(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}", cnfrmps)&&cnfrmps.equals(newps))
                                        {
                                            request="update";
                                            new Hitter().execute("http://hidi.org.in/hidi/Auth/pass.php");
                                            old.setText("");
                                            newp.setText("");
                                            cnfrm.setText("");

                                        }
                                        else
                                        {
                                            twoss.setError("Invalid");
                                            threess.setError("Invalid");
                                            Toast.makeText(CreatePassword.this, "Password invalid", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        twoss.setError("Invalid");
                                        threess.setError("Invalid");
                                        Toast.makeText(CreatePassword.this, "Password invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    oness.setError("Incorrect");
                                    Toast.makeText(CreatePassword.this, "Old password invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        old.setVisibility(View.INVISIBLE);
                        newp.setVisibility(View.VISIBLE);
                        cnfrm.setVisibility(View.VISIBLE);
                        hit.setText("Create");
                        hit.setVisibility(View.VISIBLE);
                        hit.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                oldps="";
                                newps=newp.getText().toString();
                                cnfrmps=cnfrm.getText().toString();
                                if(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}", newps))
                                {
                                    if(Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*])(?=\\S+$).{8,16}", cnfrmps)&&cnfrmps.equals(newps))
                                    {
                                        request="add";
                                        new Hitter().execute("http://hidi.org.in/hidi/Auth/pass.php");
                                        old.setText("");
                                        newp.setText("");
                                        cnfrm.setText("");

                                    }
                                    else
                                    {
                                        twoss.setError("Invalid");
                                        threess.setError("Invalid");
                                        Toast.makeText(CreatePassword.this, "Password invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    twoss.setError("Invalid");
                                    threess.setError("Invalid");
                                    Toast.makeText(CreatePassword.this, "Password invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Intent intent=new Intent(CreatePassword.this,Accounts.class);
                    startActivity(intent);
                    finish();

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }
    private class Hitter extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("Result",s);
            try
            {
                JSONObject jsonObject=new JSONObject(s);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    Intent intent=new Intent(CreatePassword.this,Accounts.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    oness.setError("Invalid");
                    twoss.setError("Invalid");
                    threess.setError("Invalid");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... url)
        {
            return POST(url[0]);
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
            jsonObject.accumulate("request",request);
            jsonObject.accumulate("newpassword",newps);
            jsonObject.accumulate("oldpassword",oldps);
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
