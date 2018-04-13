package com.example.hp.hidi2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

public class SelectTags extends AppCompatActivity
{
    Button button_arts,button_asia,button_australia,button_beauty,button_books,button_bussiness,button_carrees,button_cars,button_edu,button_europe;
    Button button_fitness,button_food,button_fun,button_games,button_health,button_it,button_math,button_add,button_law;

    int f_arts=0,f_asia=0,f_australia=0,f_beauty=0,f_books=0,f_business=0,f_carrees=0,f_cars=0;
    int f_edu=0,f_europe=0,f_fitness=0,f_food=0,f_fun=0,f_games=0,f_health=0,f_it=0,f_math=0,f_add=0,f_law=0;

    ImageView tagging;
    SessionManager session;
    String result="";
    String tags="";
    int pid;
    ArrayList<String> tagnames=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tags);
        Bundle bundle=getIntent().getExtras();
        session=new SessionManager(getApplicationContext());
        session.checkLogin();
        pid=bundle.getInt("pid");
        button_arts = findViewById(R.id.arts);
        button_asia = findViewById(R.id.asia);
        button_australia = findViewById(R.id.australia);
        button_beauty = findViewById(R.id.beauty);
        button_books = findViewById(R.id.books);
        button_bussiness = findViewById(R.id.bussinesss);
        button_carrees = findViewById(R.id.carreers);
        button_cars = findViewById(R.id.cartransport);
        button_edu = findViewById(R.id.education);
        button_europe = findViewById(R.id.europe);
        button_fitness = findViewById(R.id.fitness);
        button_food = findViewById(R.id.fooddrink);
        button_fun = findViewById(R.id.fun);
        button_games = findViewById(R.id.games);
        button_health = findViewById(R.id.health);
        button_it = findViewById(R.id.it);
        button_math = findViewById(R.id.mathematics);
        button_add = findViewById(R.id.adventure);
        button_law =findViewById(R.id.law);
        tagging=findViewById(R.id.addTags);
        tagging.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int i=0;i<tagnames.size();i++)
                {
                    if(i==0)
                        tags=tags+tagnames.get(i);
                    else
                        tags=tags+","+tagnames.get(i);
                }
                Log.e("Tags",tags);
                if(tags.length()!=0)
                {
                    new UploadTags().execute("http://hidi.org.in/hidi/tags/addtags.php");
                }
                else
                {
                    Toast.makeText(SelectTags.this, "Please select tags", Toast.LENGTH_SHORT).show();
                }
//                Intent intent=new Intent(SelectTags.this,PostActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }
        });
        button_arts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_arts==0)
                {
                    button_arts.setBackgroundResource(R.drawable.circular_round_click);
                    f_arts=1;
                    tagnames.add("Arts");
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    button_arts.setBackgroundResource(R.drawable.undo_button);
                    f_arts=0;
                    tagnames.remove("Arts");
                    Log.e("tags",tagnames+"");
                }

            }
        });
        button_asia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_asia==0)
                {
                    f_asia=1;
                    button_asia.setBackgroundResource(R.drawable.circular_round_click);
                    tagnames.add("Asia");
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    f_asia=0;
                    tagnames.remove("Asia");
                    button_asia.setBackgroundResource(R.drawable.undo_button);
                    Log.e("tags",tagnames+"");
                }
            }
        });
        button_australia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_australia==0)
                {
                    tagnames.add("Australia");
                    f_australia=1;
                    button_australia.setBackgroundResource(R.drawable.circular_round_click);
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    tagnames.remove("Australia");
                    f_australia=0;
                    button_australia.setBackgroundResource(R.drawable.undo_button);
                    Log.e("tags",tagnames+"");
                }
            }
        });
        button_beauty.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_beauty==0)
                {
                    tagnames.add("Beauty&Style");
                    f_beauty=1;
                    button_beauty.setBackgroundResource(R.drawable.circular_round_click);
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    tagnames.remove("Beauty&Style");
                    f_beauty=0;
                    button_beauty.setBackgroundResource(R.drawable.undo_button);
                    Log.e("tags",tagnames+"");
                }
            }
        });
        button_books.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_books==0)
                {
                    tagnames.add("Books");
                    f_books=1;
                    button_books.setBackgroundResource(R.drawable.circular_round_click);
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    tagnames.remove("Books");
                    f_books=0;
                    button_books.setBackgroundResource(R.drawable.undo_button);
                    Log.e("tags",tagnames+"");
                }
            }
        });
        button_bussiness.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_business==0)
                {
                    f_business=1;
                    button_bussiness.setBackgroundResource(R.drawable.circular_round_click);
                    tagnames.add("Business");
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    f_business=0;
                    button_bussiness.setBackgroundResource(R.drawable.undo_button);
                    tagnames.remove("Business");
                    Log.e("tags",tagnames+"");
                }
            }
        });
        button_carrees.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_carrees==0)
                {
                    f_carrees=1;
                    button_bussiness.setBackgroundResource(R.drawable.circular_round_click);
                    tagnames.add("Carrers");
                    Log.e("tags",tagnames+"");
                }
                else
                {
                    f_carrees=0;
                    button_bussiness.setBackgroundResource(R.drawable.undo_button);
                    tagnames.remove("Carrers");
                    Log.e("tags",tagnames+"");
                }
            }
        });
        button_cars.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_cars==0)
                {
                    f_cars=1;
                    tagnames.add("Cars&Transportation");
                    Log.e("tags",tagnames+"");
                    button_cars.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_cars=0;
                    tagnames.remove("Cars&Transportation");
                    Log.e("tags",tagnames+"");
                    button_cars.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_edu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_edu==0)
                {
                    f_edu=1;
                    tagnames.add("Education");
                    Log.e("tags",tagnames+"");
                    button_edu.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_edu=0;
                    tagnames.remove("Education");
                    Log.e("tags",tagnames+"");
                    button_edu.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_europe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_europe==0)
                {
                    f_europe=1;
                    tagnames.add("Europe");
                    Log.e("tags",tagnames+"");
                    button_europe.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_europe=0;
                    tagnames.remove("Europe");
                    Log.e("tags",tagnames+"");
                    button_europe.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_fitness.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_fitness==0)
                {
                    f_fitness=1;
                    tagnames.add("Fitness");
                    Log.e("tags",tagnames+"");
                    button_fitness.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_fitness=0;
                    tagnames.remove("Fitness");
                    Log.e("tags",tagnames+"");
                    button_fitness.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_food.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_food==0)
                {
                    f_food=1;
                    tagnames.add("Food&Drink");
                    Log.e("tags",tagnames+"");
                    button_food.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_food=0;
                    tagnames.remove("Food&Drink");
                    Log.e("tags",tagnames+"");
                    button_food.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_fun.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_fun==0)
                {
                    f_fun=1;
                    tagnames.add("Fun&Humor");
                    Log.e("tags",tagnames+"");
                    button_fun.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_fun=0;
                    tagnames.remove("Fun&Humor");
                    Log.e("tags",tagnames+"");
                    button_fun.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_games.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_games==0)
                {
                    f_games=1;
                    tagnames.add("Games");
                    Log.e("tags",tagnames+"");
                    button_games.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_games=0;
                    tagnames.remove("Games");
                    Log.e("tags",tagnames+"");
                    button_games.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_health.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_health==0)
                {
                    f_health=1;
                    tagnames.add("Healthcare");
                    Log.e("tags",tagnames+"");
                    button_health.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_health=0;
                    tagnames.remove("Healthcare");
                    Log.e("tags",tagnames+"");
                    button_health.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_it.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_it==0)
                {
                    f_it=1;
                    tagnames.add("IT&Programming");
                    Log.e("tags",tagnames+"");
                    button_it.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_it=0;
                    tagnames.remove("IT&Programming");
                    Log.e("tags",tagnames+"");
                    button_it.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_math.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_math==0)
                {
                    f_math=1;
                    tagnames.add("Mathematics");
                    Log.e("tags",tagnames+"");
                    button_math.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_math=0;
                    tagnames.remove("Mathematics");
                    Log.e("tags",tagnames+"");
                    button_math.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_add==0)
                {
                    tagnames.add("Adventure");
                    Log.e("tags",tagnames+"");
                    f_add=1;
                    button_add.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    tagnames.remove("Adventure");
                    Log.e("tags",tagnames+"");
                    f_add=0;
                    button_add.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
        button_law.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f_law==0)
                {
                    f_law=1;
                    tagnames.add("Law&Legal");
                    Log.e("tags",tagnames+"");
                    button_law.setBackgroundResource(R.drawable.circular_round_click);
                }
                else
                {
                    f_law=0;
                    tagnames.remove("Law&Legal");
                    Log.e("tags",tagnames+"");
                    button_law.setBackgroundResource(R.drawable.undo_button);
                }
            }
        });
    }
    private class UploadTags extends AsyncTask<String,Void,String>
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
            try
            {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject info=jsonObject.getJSONObject("info");
                if(info.getString("status").equals("success"))
                {
                    Intent intent=new Intent(SelectTags.this,PostActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(SelectTags.this,"Error uploading tags",Toast.LENGTH_SHORT).show();
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

        InputStream inputStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("pid",pid);
            jsonObject.accumulate("tags",tags);
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
