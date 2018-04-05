package com.example.hp.hidi2;

import android.*;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private List<PostGet> postList = new ArrayList<>();
    View.OnTouchListener gestureListener;
    private RecyclerView recyclerView;
    SessionManager session;
    GPSTracker gps;
    CoordinatorLayout constraintLayout;
    boolean bool;
    ProgressDialog progress;
    private MyAdapter_post myAdapter_post;
    private GestureDetector gestureDetector;
    String result = "";
    SwipeRefreshLayout swiper;
    private PopupWindow popupWindow;
    private ActionBar toolbar;
    TextView actionbars;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        swiper = findViewById(R.id.refresh);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading....");
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        toolbar=getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setCustomView(R.layout.set_middle_title);
        actionbars=findViewById(R.id.actionBarTitles);
        gps = new GPSTracker(this);
        session.saveLoc(gps.latitude, gps.longitude);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        BottomNavigationView navigation = findViewById(R.id.navigation);
        recyclerView = findViewById(R.id.recyclerView);
        constraintLayout = findViewById(R.id.post_layout);
//        new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
        progress.show();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("dcd","cde");
                new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
            }
        });
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        swiper.setRefreshing(true);
//        try
//        {
//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
//            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        }
//        catch (GooglePlayServicesRepairableException e)
//        {
//            // TODO: Handle the error.
//        }
//        catch (GooglePlayServicesNotAvailableException e)
//        {
//            // TODO: Handle the error.
//        }
        new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
        layoutParams.setBehavior(new BottomNavigationBehavior());
        recyclerView.setOnTouchListener(gestureListener);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.my_hidies:
                        startActivity(new Intent(PostActivity.this,MyHidiesPosts.class));
                        return true;
                    case R.id.filter:
                        initiatepopupWindow();
                        constraintLayout.setAlpha(.2f);
//                            constraintLayout.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    constraintLayout.setAlpha(1);
//                                }
//                            });
//                            recyclerView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    constraintLayout.setAlpha(1);
//                                }
//                            });
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                constraintLayout.setAlpha(1);
                            }
                        });
//                        Intent intent=new Intent(PostActivity.this,PostActivityLocation.class);
//                        startActivity(intent);
                        return true;

                    case R.id.person:
                        startActivity(new Intent(PostActivity.this, SearchUser.class));
                }
                return false;
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.d( "Place: ","" + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("result", ""+status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            // Tapped outside so we finish the activity
            this.finish();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initiatepopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) PostActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.pop_up_on_plus, (ViewGroup) findViewById(R.id.pop_up_on_plus));
            popupWindow = new PopupWindow(layout, 420, 350, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 473);
            ImageButton location = layout.findViewById(R.id.text_img);
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PostActivity.this, PostActivityLocation.class);
                    startActivity(intent);
                }
            });
            ImageButton tags = layout.findViewById(R.id.camera_img);
            tags.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PostActivity.this, PostActivityTag.class);
                    startActivity(intent);
                }
            });
            TextView textView_Loc = layout.findViewById(R.id.text_loc);
            textView_Loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PostActivity.this,PostActivityLocation.class));
                }
            });
            TextView textView_Tags = layout.findViewById(R.id.text_tag);
            textView_Tags.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PostActivity.this,PostActivityTag.class));
                }
            });
            ImageButton imageButton_close = layout.findViewById(R.id.close_img);
            imageButton_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PostActivity.this,PostActivity.class));
                }
            });
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));


            Dialog dialog = new Dialog(this);
            dialog.setCanceledOnTouchOutside(true);
            popupWindow.setFocusable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
//            TextView textView = layout.findViewById(R.id.new_post_header);
//            imageView1 = layout.findViewById(R.id.imageView1);
//            EditText editText = layout.findViewById(R.id.post_text);
//            Button button_mood = layout.findViewById(R.id.button_mood);
//            Button button_send = layout.findViewById(R.id.button_send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onRefresh() {

        // Fetching data from server
        new Posts().execute("http://hidi.org.in/hidi/post/showposts.php");
        progress.show();
    }*/

    private void onLeftSwipe() {

        Intent intent=new Intent(PostActivity.this,ChatList.class);
        startActivity(intent);
//        finish();
    }

    private void onRightSwipe() {

        Intent intent = new Intent(PostActivity.this, Accounts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();
                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;
                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PostActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PostActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("Home", "Error on gestures");
            }
            return false;
        }
    }

    private class Posts extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result", result);
            try {
                PostGet postGet;
                JSONObject respnse = new JSONObject(result);
                JSONObject info = respnse.getJSONObject("info");
                JSONArray records = respnse.getJSONArray("records");
                if ((info.getString("status")).equals("success")) {
                    progress.dismiss();
                    swiper.setRefreshing(false);

                    for (int i = 0; i < records.length(); i++) {
                        JSONObject posts = records.getJSONObject(i);
                        Log.d("pid", "" + posts.getInt("pid"));
                        String pid = "" + posts.getInt("pid");
                        Log.d("pic", "" + posts.getString("pic"));
                        String pic = "" + posts.getString("pic");
                        Log.d("likes", "" + posts.getInt("likes"));
                        String likesc = "" + posts.getInt("likes");
                        Log.d("dislikes", "" + posts.getInt("dislikes"));
                        String dislikesc = "" + posts.getInt("dislikes");
                        Log.d("comments", "" + posts.getInt("comments"));
                        String commentsc = "" + posts.getInt("comments");
                        Log.d("time", "" + posts.getString("time"));
                        Log.d("lat", "" + posts.getDouble("lat"));
                        Log.d("long", "" + posts.getDouble("long"));
                        Log.d("location", posts.getString("location"));
                        String locations = posts.getString("location");
                        Log.d("distance", "" + posts.getDouble("distance"));
                        Log.d("sec_name", "" + posts.getString("sec_name"));
                        String name = posts.getString("sec_name");
                        Log.d("profilepic", "" + posts.getString("profilepic"));
                        String profile = posts.getString("profilepic");
                        Log.d("like", "" + posts.getInt("like"));
                        String mlike = "" + posts.getInt("like");
                        Log.d("dislike", "" + posts.getInt("dislike"));
                        String mdisllike = "" + posts.getInt("dislike");
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

//                        postGet=new PostGet(posts.getString("sec_name"),
//                                ""+posts.getInt("like"),""+posts.getInt("comments"),
//                                posts.getString("profilepic"),posts.getString("pic"));
                        postGet = new PostGet(pid, profile, name, locations, pic, likesc, commentsc, dislikesc, mlike, mdisllike);
                        postList.add(postGet);
                    }
                    myAdapter_post = new MyAdapter_post(PostActivity.this, postList, session.getUID(), "all");
                    recyclerView.setAdapter(myAdapter_post);
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error getting records", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String json = "";
        result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uid", session.getUID());
            jsonObject.accumulate("request", "all");
            jsonObject.accumulate("lat", gps.latitude);
            jsonObject.accumulate("long", gps.longitude);
            jsonObject.accumulate("dist", 2);
            json = jsonObject.toString();
            Log.d("json", json);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_search, menu);

        // Get the search menu.
//        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Get SearchView autocomplete object.
//        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchAutoComplete.setBackgroundColor(Color.argb(01,79,80,81));
//        searchAutoComplete.setTextColor(Color.WHITE);
//        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
//        String dataArr[] = {"Apple", "Amazon", "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
//        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
//        searchAutoComplete.setAdapter(newsAdapter);
//        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
//                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
//                searchAutoComplete.setText("" + queryString);
//                Toast.makeText(PostActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                AlertDialog alertDialog = new AlertDialog.Builder(PostActivity.this).create();
//                alertDialog.setMessage("Search keyword is " + query);
//                alertDialog.show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.app_bar_menu_search)
        {
            try
            {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(PostActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
            catch (GooglePlayServicesRepairableException e)
            {
                // TODO: Handle the error.
            }
            catch (GooglePlayServicesNotAvailableException e)
            {
                // TODO: Handle the error.
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
