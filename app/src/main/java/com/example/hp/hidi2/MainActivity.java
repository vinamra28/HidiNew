package com.example.hp.hidi2;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    GPSTracker gps;
    TextView registering, recover;
    Button get;
    EditText phone;
    String mobile = "";
    String result = "";
    ImageButton google, fb;
    SessionManager session;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission= Manifest.permission.READ_SMS;
    String mPermission1= Manifest.permission.ACCESS_FINE_LOCATION;
    String mPermission2=Manifest.permission.INTERNET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps=new GPSTracker(this);
        session=new SessionManager(getApplicationContext());
        try
        {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != MockPackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(this, mPermission1) != MockPackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{mPermission,mPermission1,mPermission2},REQUEST_CODE_PERMISSION);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if(ActivityCompat.checkSelfPermission(this, mPermission2) != MockPackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(this, mPermission2) != MockPackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{mPermission2},REQUEST_CODE_PERMISSION);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if (ActivityCompat.checkSelfPermission(this, mPermission1) != MockPackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{mPermission1},REQUEST_CODE_PERMISSION);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.saveLoc(gps.latitude,gps.longitude);
        registering = findViewById(R.id.creating);
        recover = findViewById(R.id.recovering);
        get = findViewById(R.id.getOtp);
        google = findViewById(R.id.gplusButton);
        fb = findViewById(R.id.fbButton);
        phone = findViewById(R.id.phnumber);
        session.saveLoc(gps.latitude,gps.longitude);
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.google);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), myBitmap);
        roundedBitmapDrawable.setCornerRadius(125f);
        fb.setImageDrawable(roundedBitmapDrawable);
        Bitmap myBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.googlesignin);
        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), myBitmap1);
        roundedBitmapDrawable1.setCornerRadius(125f);
        google.setImageDrawable(roundedBitmapDrawable1);
        registering.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        recover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Recover.class);
                startActivity(intent);
                finish();
            }
        });
        get.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mobile = phone.getText().toString();
               /* final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                get.startAnimation(myAnim);
               */ if(Pattern.matches("[6789][0-9]{9}",mobile))
                {
                     new Verification().execute("http://hidi.org.in/hidi1/Auth/getotp.php");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter valid no.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        fb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                fb.startAnimation(myAnim);
            }
        });
        google.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                google.startAnimation(myAnim);
            }
        });
    }
    private class Verification extends AsyncTask<String, Void, String>
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
            Log.d("Result", result);
            //Toast.makeText(GenOtp.this,""+result,Toast.LENGTH_LONG).show();
            try
            {
                JSONObject ff = new JSONObject(result);
                JSONObject info = ff.getJSONObject("info");
                String ss = info.getString("status");
                Log.d("verifi", ss);
                if (ss.equalsIgnoreCase("success"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile",mobile);
                    bundle.putInt("request",1);
                    Intent intent = new Intent(MainActivity.this, VerifyOtp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),info.getString("message"),Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String POST(String url)
    {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("mobileno", mobile);
            jsonObject.accumulate("request","login");
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}