package com.example.hp.hidi2;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Accounts extends AppCompatActivity
{
    SessionManager session;
    float x1, x2, y1, y2;
//    CircleProgress progress;
    Uri URI;
    String[] FILE;
    String ImageDecode;
    ImageView imageView_plus,userdp;
    TextView admire,love,visitors,hidies,blocks;
    Button see_notifications,my_journey;
    static int PICK_IMAGE_REQUEST = 1;
    String mPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        userdp = findViewById(R.id.profilepic);
//        progress=findViewById(R.id.popularProgress);
        admire=findViewById(R.id.admireCount);
        love=findViewById(R.id.loveCount);
        visitors=findViewById(R.id.visitorsCount);
        hidies=findViewById(R.id.hidiCount);
        blocks=findViewById(R.id.blockCount);
        see_notifications=findViewById(R.id.notificationButton);
        imageView_plus = findViewById(R.id.statusButton);
        my_journey=findViewById(R.id.journeyButton);
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.index);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), myBitmap);
        roundedBitmapDrawable.setCornerRadius(55f);
        userdp.setImageDrawable(roundedBitmapDrawable);
        imageView_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Accounts.this,StatusActivity.class);
                startActivity(intent);
            }
        });
        userdp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFileChooser();
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
            Uri selectedMediaUri = data.getData();
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
            imageUpload(selectedMediaUri);
        }
    }
    private void imageUpload(final Uri fileUri)
    {
        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);
        File file = new File(ImageDecode);
        RequestBody requestFile =RequestBody.create(MediaType.parse("*/*"), file);
        Log.d("Request1",""+requestFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile", file.getName(), requestFile);
        Log.d("Multipart",""+body);
        String descriptionString = "hello, this is description speaking";
        RequestBody id=RequestBody.create(MediaType.parse("text/plain"),""+session.getUID());
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
        Log.d("Description",""+description);
        Call<ResponseBody> call = service.upload(description, body,id);
        Log.d("Call service",""+call);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                Log.v("Upload", "success");
                userdp.setImageURI(fileUri);
                new HttpAsyncTask().execute("http://hidi.org.in/hidi/account/myaccount.php");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.notification)
        {
        }
        if (id == R.id.night)
        {
        }
        if (id == R.id.logout)
        {
            session.logoutUser();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if (x1 > x2)
                {
                    Intent intent = new Intent(Accounts.this, PostActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            default:System.out.println();
        }
        return false;
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
            Log.d("Result", result);
            try
            {
                JSONObject res = new JSONObject(result);
                JSONObject records = res.getJSONObject("records");
                admire.setText(""+records.getInt("admire"));
                visitors.setText(""+records.getInt("visitors"));
                hidies.setText(""+records.getInt("hidies"));
                blocks.setText(""+records.getInt("blocks"));
                love.setText(""+records.getInt("love"));
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
            jsonObject.accumulate("uid",session.getUID());
            jsonObject.accumulate("request","get");
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



//for later use

//    private PopupWindow popupWindow;
//
//    private void initiatepopupWindow() {
//        try {
//            //We need to get the instance of the LayoutInflater, use the context of this activity
//            LayoutInflater inflater = (LayoutInflater) Accounts.this
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            //Inflate the view from a predefined XML layout
//            View layout = inflater.inflate(R.layout.pop_up_on_plus, (ViewGroup) findViewById(R.id.pop_up_on_plus));
//            // create a 300px width and 470px height PopupWindow
//            popupWindow = new PopupWindow(layout, 400, 300, true);
//            // display the popup in the center
//            popupWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 50, 580);
//
//            TextView textView_text = (TextView) layout.findViewById(R.id.text_text);
//            ImageButton imageButton_text = (ImageButton) layout.findViewById(R.id.text_img);
//            TextView textView_camera = (TextView) layout.findViewById(R.id.text_camera);
//            ImageButton imageButton_camera = (ImageButton) layout.findViewById(R.id.text_camera);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
