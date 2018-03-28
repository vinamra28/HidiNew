package com.example.hp.hidi2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.ArrayList;

public class SelectName extends AppCompatActivity
{
    ProgressDialog dialog;
    TextView tv,tv1;
    Spinner spinner;
    Button nextt;
    String result="",hidiName="";
    int uid=0;
    ArrayList<String> names=new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private static final String TAG = "AnonymousAuth";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_name);
        Bundle bundle=getIntent().getExtras();
        uid=bundle.getInt("UID");
        dialog=new ProgressDialog(this);
        dialog.setMessage("Registering...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        tv=findViewById(R.id.tvx);
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv1=findViewById(R.id.textView);
        nextt = findViewById(R.id.next);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.select_dialog_item, names);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, names, android.R.layout.select_dialog_item);
        spinner.setAdapter(adapter);
//        hidiName=spinner.getSelectedItem().toString();
//        if(hidiName.length()!=0)
//        {
//            tv.setVisibility(View.VISIBLE);
//            tv1.setText(hidiName);
//        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                hidiName= (String) parent.getItemAtPosition(position);
                if(hidiName.length()!=0)
                {
                    tv.setVisibility(View.VISIBLE);
                    tv1.setText(hidiName);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        nextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                final Animation myAnim = AnimationUtils.loadAnimation(SelectName.this, R.anim.bounce);
//                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
//                myAnim.setInterpolator(interpolator);
//                nextt.startAnimation(myAnim);
                dialog.show();
                new HttpAsyncTask().execute("http://hidi.org.in/hidi/account/secname.php");
                //Registering user on Firebase

                firebaseAuth = FirebaseAuth.getInstance();
                final Task<AuthResult> resultTask = firebaseAuth.signInAnonymously();
                resultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (resultTask.isSuccessful()){
                            Toast.makeText(SelectName.this,"Success",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SelectName.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Saving data of user on firebase
                databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child("users").child(uid+"").child("profilepic:").setValue("havsfua");
                databaseReference.child("users").child(uid+"").child("username").setValue(hidiName);
            }
        });
    }



//    @Override
//    protected void onStart() {
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
////        updateUI(currentUser);
//        super.onStart();
//    }

    public void read()throws IOException
    {
        String str="";
        StringBuffer buf=new StringBuffer();
        InputStream in=this.getResources().openRawResource(R.raw.hidi_names);
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        if(in!=null)
        {
            while((str=reader.readLine())!=null)
            {
                buf.append(str+"\n");
                names.add(str);
            }
        }
        in.close();
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
                    dialog.dismiss();
                    Intent intent = new Intent(SelectName.this, PostActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    dialog.dismiss();
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
