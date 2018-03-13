package com.example.hp.hidi2;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends AppCompatActivity
{
    GPSTracker gps;
    String time_stamp="",result="";
    RelativeLayout backgroundLayout;
    ImageView colorBack,sending;
    EditText status;
    SessionManager session;
    double lat=0.0,lng=0.0;
    Uri URI;
    Uri selectedMediaUri;
    String[] FILE;
    String ImageDecode;
    static int PICK_IMAGE_REQUEST = 1;
    int t=1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        final int[] backColors = getResources().getIntArray(R.array.back);
        backgroundLayout=(RelativeLayout)findViewById(R.id.background);
        status=findViewById(R.id.text);
        sending=findViewById(R.id.send);
        backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        colorBack=(ImageView)findViewById(R.id.color);
        gps=new GPSTracker(this);
        if(gps.canGetLocation())
        {
            lat=gps.latitude;
            lng=gps.longitude;
            session.saveLoc(gps.latitude,gps.longitude);
            Log.d("Latitude",""+lat);
            Log.d("Longitude",""+lng);
        }
        else
        {
            gps.showSettingsAlert();
        }
        colorBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                backgroundLayout.setBackgroundColor(backColors[t]);
                status.setBackgroundColor(backColors[t]);
                t++;
                if(t==backColors.length)t=0;
            }
        });
        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showFileChooser();
                status.buildDrawingCache();
                Bitmap image=status.getDrawingCache();
                ContextWrapper wrapper=new ContextWrapper(getApplicationContext());
                File file=wrapper.getDir("Hidi",MODE_PRIVATE);
                file=new File(file,"Post"+".jpg");
                try
                {
                    OutputStream outputStream=null;
                    outputStream=new FileOutputStream(file);
                    image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri savedImage=Uri.parse(file.getAbsolutePath());
//                FILE = new String[]{MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(savedImage, FILE, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(FILE[0]);
//                ImageDecode = cursor.getString(columnIndex);
                ImageDecode=file.getAbsolutePath();
                Log.d("paths",ImageDecode);
//                cursor.close();
                new PostUpdate().execute("http://hidi.org.in/hidi/post/mypost.php");
            }
        });
    }
    private void showFileChooser()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        Log.d("Intent",""+intent);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK) {
            selectedMediaUri = data.getData();
            Log.e("uri", selectedMediaUri + "");
            String type = this.getContentResolver().getType(selectedMediaUri);
            Log.e(type + " ddd", selectedMediaUri.getEncodedPath());
            URI = data.getData();
            FILE = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(FILE[0]);
            ImageDecode = cursor.getString(columnIndex);
            Log.d("paths",ImageDecode);
            cursor.close();
//            imageUpload(selectedMediaUri);
//            new PostUpdate().execute("http://hidi.org.in/hidi/post/mypost.php");
        }
    }
    private void imageUpload(final Uri fileUri, int r)
    {
        FileUploadService service = ServiceGenerator.createService1(FileUploadService.class);

        File file = new File(ImageDecode);
        RequestBody requestFile =RequestBody.create(MediaType.parse("*/*"), file);
//        RequestBody requestFile =RequestBody.create(MediaType.parse("*/*"), String.valueOf(image));
        Log.d("Request1",""+requestFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", file.getName(), requestFile);
        Log.d("Multipart",""+body);
        String descriptionString = "hello, this is description speaking";
        RequestBody id=RequestBody.create(MediaType.parse("text/plain"),""+r);
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
        Log.d("Description",""+description);
        Call<ResponseBody> call = service.upload1(description, body,id);
        Log.d("Call service",""+call);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                Log.v("Upload", "success");
                Intent intent=new Intent(StatusActivity.this,PostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
//                post.setImageURI(fileUri);
//                new HttpAsyncTask().execute("http://hidi.org.in/hidi/account/myaccount.php");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
    private class PostUpdate extends AsyncTask<String,Void,String>
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
                    imageUpload(selectedMediaUri,res.getInt("records"));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
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
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        time_stamp=sdf.format(date);
        Geocoder geocoder;
        List<Address> addresses;
        String city="";
        geocoder=new Geocoder(this, Locale.getDefault());
        try
        {
            addresses=geocoder.getFromLocation(gps.latitude,gps.longitude,1);
            city=addresses.get(0).getLocality();
            Log.d("city",city);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("lat",gps.latitude);//
            jsonObject.accumulate("long",gps.longitude);//
            jsonObject.accumulate("time",time_stamp);
            jsonObject.accumulate("uid",session.getUID());
            jsonObject.accumulate("location",city);
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
}
