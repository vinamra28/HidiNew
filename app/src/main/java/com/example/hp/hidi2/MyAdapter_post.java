package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

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
import java.util.List;

public class MyAdapter_post extends RecyclerView.Adapter<MyAdapter_post.MyViewHolder> {
    private Context context;
    SessionManager session;
    private List<PostGet> postList;
    String result = "", request = "";
    int pid, uid, x,uidToVisit;
    PostGet post;
    String from = "";
    private List<Integer> flag = new ArrayList<>();
    private List<Integer> flagdis = new ArrayList<>();
    private List<Integer> like = new ArrayList<>(), dislike = new ArrayList<>();

    public MyAdapter_post(Context context, List<PostGet> postList, int uid, String from) {
        this.context = context;
        this.postList = postList;
        this.uid = uid;
        this.from = from;
        session=new SessionManager(context);
        Log.e("size", postList.size() + "");
        for (int x = 0; x < postList.size(); x++) {
            flagdis.add(Integer.parseInt(postList.get(x).getDislike()));
            flag.add(Integer.parseInt(postList.get(x).getLike()));
            like.add(Integer.parseInt(postList.get(x).getTotal_favours()));
            dislike.add(Integer.parseInt(postList.get(x).getTotal_dislikes()));

        }
        Log.e("dslike", flagdis.toString());
        Log.e("like", flag.toString());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_design, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        post = postList.get(holder.getAdapterPosition());
        Picasso.with(context).load(post.getUser_dp()).into(holder.user_dp);
        holder.user_name.setText(post.getUser_name());
        holder.post_location.setText(post.getPost_location());
        Picasso.with(context).load(post.getUser_post_image()).into(holder.image_posted);
        holder.total_favours.setText(like.get(position).toString());
        holder.do_like.setImageDrawable(post.getDo_like());
        holder.do_arguments.setText(post.getDo_arguments());
        holder.image_argument.setImageDrawable(post.getImage_argument());
        holder.do_dislike.setImageDrawable(post.getDo_dislike());
        holder.total_dislikes.setText(dislike.get(position).toString());
        pid = Integer.parseInt(post.getPid());
        MobileAds.initialize(context, "ca-app-pub-8097507647686370~9504269092");
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        holder.adView.loadAd(adRequest);


        holder.setIsRecyclable(false);
//        try
//        {
//            JSONObject jsonObject=new JSONObject(result);
//            JSONObject records=jsonObject.getJSONObject("records");
//            like=records.getInt("like");
//            dislike=records.getInt("dislike");
//        }
//        catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
        if (flag.get(holder.getAdapterPosition()) == 1) {
            holder.do_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
        } else if (flagdis.get(holder.getAdapterPosition()) == 1) {
            holder.do_dislike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down_blue_24dp));

        }
        holder.user_dp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uidToVisit=postList.get(holder.getAdapterPosition()).getUid_poster();
                if(uidToVisit!=session.getUID())
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("name",postList.get(holder.getAdapterPosition()).getUser_name());
                    bundle.putInt("uid", uidToVisit);
                    Intent intent=new Intent(v.getContext(),NewUserProfile.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(v.getContext(),Accounts.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
        holder.user_name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uidToVisit=postList.get(holder.getAdapterPosition()).getUid_poster();
                if(uidToVisit!=session.getUID())
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("name",postList.get(holder.getAdapterPosition()).getUser_name());
                    bundle.putInt("uid", uidToVisit);
                    Intent intent=new Intent(v.getContext(),NewUserProfile.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(v.getContext(),Accounts.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
        holder.do_arguments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pid = Integer.parseInt(postList.get(holder.getAdapterPosition()).getPid());
                Bundle bundle = new Bundle();
                bundle.putInt("pid", pid);
                Intent intent = new Intent(v.getContext(), Arguments.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
        holder.image_argument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pid = Integer.parseInt(postList.get(holder.getAdapterPosition()).getPid());
                Bundle bundle = new Bundle();
                bundle.putInt("pid", pid);
                Intent intent = new Intent(v.getContext(), Arguments.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
        holder.do_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = "like";
//                x = Integer.parseInt(postList.get(holder.getAdapterPosition()).getTotal_favours())+1;
//                postList.get(holder.getAdapterPosition()).setTotal_favours(x+"");
                // int x=Integer.parseInt(postList.get(holder.getAdapterPosition()).getTotal_favours(like+" "));

                if (flag.get(holder.getAdapterPosition()) == 0 && flagdis.get(holder.getAdapterPosition()) == 0) {
                    Log.d("likebothzero", like + "");
                    Log.d("dislikebothzero", dislike + "");
                    flag.set(holder.getAdapterPosition(), 1);
                    flagdis.set(holder.getAdapterPosition(), 0);
                    like.set(holder.getAdapterPosition(), like.get(holder.getAdapterPosition()) + 1);
                    Log.d("likebothzero1", like + "");
                    Log.d("dislikebothzero1", dislike + "");
                    holder.total_favours.setText(Integer.toString(like.get(holder.getAdapterPosition())));
                    holder.total_dislikes.setText(Integer.toString(dislike.get(holder.getAdapterPosition())));
                    postList.get(holder.getAdapterPosition()).setDo_dislike(context.getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));
                    postList.get(holder.getAdapterPosition()).setDo_like(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                } else if (flag.get(holder.getAdapterPosition()) == 0 && flagdis.get(holder.getAdapterPosition()) == 1) {
                    Log.d("likeflagzero", like + "");
                    Log.d("dislikelikezero", dislike + "");
                    flag.set(holder.getAdapterPosition(), 1);
                    flagdis.set(holder.getAdapterPosition(), 0);
                    like.set(holder.getAdapterPosition(), like.get(holder.getAdapterPosition()) + 1);
                    if (dislike.get(holder.getAdapterPosition()) > 0)
                        dislike.set(holder.getAdapterPosition(), dislike.get(holder.getAdapterPosition()) - 1);
                    Log.d("likeflagzero1", like + "");
                    Log.d("dislikelikezero1", dislike + "");
                    holder.total_favours.setText(Integer.toString(like.get(holder.getAdapterPosition())));
                    holder.total_dislikes.setText(Integer.toString(dislike.get(holder.getAdapterPosition())));
                    postList.get(holder.getAdapterPosition()).setDo_like(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                    postList.get(holder.getAdapterPosition()).setDo_dislike(context.getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));

                } else if (flag.get(holder.getAdapterPosition()) == 1 && flagdis.get(holder.getAdapterPosition()) == 0) {
                    Log.d("likefalgdiszero", like + "");
                    Log.d("dislikeflagdiszero", dislike + "");
                    flag.set(holder.getAdapterPosition(), 0);
                    ;
                    flagdis.set(holder.getAdapterPosition(), 0);
                    if (like.get(holder.getAdapterPosition()) > 0)
                        like.set(holder.getAdapterPosition(), like.get(holder.getAdapterPosition()) - 1);
                    Log.d("likefalgdiszero1", like + "");
                    Log.d("dislikeflagdiszero1", dislike + "");
                    holder.total_favours.setText(Integer.toString(like.get(holder.getAdapterPosition())));
                    holder.total_dislikes.setText(Integer.toString(dislike.get(holder.getAdapterPosition())));
                    postList.get(holder.getAdapterPosition()).setDo_dislike(context.getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));
                    postList.get(holder.getAdapterPosition()).setDo_like(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                }
//                holder.do_like.setBackground(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                //postList.get(holder.getAdapterPosition()).setTotal_favours(like+"");
                // int likes= Integer.parseInt(postList.get(holder.getAdapterPosition()).getTotal_favours());


//                postList.get(holder.getAdapterPosition()).setTotal_dislikes(dislike+"");
                Log.e(like + "", holder.getAdapterPosition() + "");
                new PostUpdate().execute("http://hidi.org.in/hidi/post/update.php");
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject records = jsonObject.getJSONObject("records");
                    JSONObject info=jsonObject.getJSONObject("info");
                    if(info.getString("status").equals("success"))
                    {
                        notifyItemChanged(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                notifyItemChanged(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.do_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request = "dislike";
                if (flagdis.get(holder.getAdapterPosition()) == 0 && flag.get(holder.getAdapterPosition()) == 0) {
                    flagdis.set(holder.getAdapterPosition(), 1);
                    flag.set(holder.getAdapterPosition(), 0);
                    dislike.set(holder.getAdapterPosition(), dislike.get(holder.getAdapterPosition()) + 1);

                    postList.get(holder.getAdapterPosition()).setDo_like(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));

                    postList.get(holder.getAdapterPosition()).setDo_dislike(context.getResources().getDrawable(R.drawable.ic_thumb_down_blue_24dp));
                } else if (flagdis.get(holder.getAdapterPosition()) == 0 && flag.get(holder.getAdapterPosition()) == 1) {
                    flagdis.set(holder.getAdapterPosition(), 1);
                    flag.set(holder.getAdapterPosition(), 0);
                    if (like.get(holder.getAdapterPosition()) > 0)
                        like.set(holder.getAdapterPosition(), like.get(holder.getAdapterPosition()) - 1);
                    dislike.set(holder.getAdapterPosition(), dislike.get(holder.getAdapterPosition()) + 1);

                    postList.get(holder.getAdapterPosition()).setDo_dislike(context.getResources().getDrawable(R.drawable.ic_thumb_down_blue_24dp));
                    postList.get(holder.getAdapterPosition()).setDo_like(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));

                } else if (flagdis.get(holder.getAdapterPosition()) == 1 && flag.get(holder.getAdapterPosition()) == 0) {
                    if (dislike.get(holder.getAdapterPosition()) > 0)
                        dislike.set(holder.getAdapterPosition(), dislike.get(holder.getAdapterPosition()) - 1);

                    flagdis.set(holder.getAdapterPosition(), 0);
                    flag.set(holder.getAdapterPosition(), 0);
                    postList.get(holder.getAdapterPosition()).setDo_like(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));

                    postList.get(holder.getAdapterPosition()).setDo_dislike(context.getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));
                }
//                holder.do_dislike.setBackground(context.getResources().getDrawable(R.drawable.ic_thumb_down_blue_24dp));
//                int z = Integer.parseInt(postList.get(holder.getAdapterPosition()).getTotal_favours())-1;
//                postList.get(holder.getAdapterPosition()).setTotal_favours(z+"");
//                int y = Integer.parseInt(postList.get(holder.getAdapterPosition()).getTotal_dislikes())+1;
//                postList.get(holder.getAdapterPosition()).setTotal_dislikes(y+"");
                holder.total_favours.setText(like.get(holder.getAdapterPosition()) + "");
                holder.total_dislikes.setText(dislike.get(holder.getAdapterPosition()) + "");
                new PostUpdate().execute("http://hidi.org.in/hidi/post/update.php");
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject records = jsonObject.getJSONObject("records");
                    JSONObject info=jsonObject.getJSONObject("info");
                    if(info.getString("status").equals("success"))
                    {
                        notifyItemChanged(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                notifyItemChanged(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, post_location, total_dislikes, do_arguments, total_favours;
        public ImageView user_dp, image_posted, do_like, do_dislike, image_argument;
        public AdView adView;

        public MyViewHolder(View itemView) {
            super(itemView);
            user_dp = itemView.findViewById(R.id.user_dp);
            user_name = itemView.findViewById(R.id.user_name);
            post_location = itemView.findViewById(R.id.post_location);
            image_posted = itemView.findViewById(R.id.image_posted);
            total_favours = itemView.findViewById(R.id.favours_number);
            do_like = itemView.findViewById(R.id.like);
            do_arguments = itemView.findViewById(R.id.do_arguments);
            image_argument = itemView.findViewById(R.id.arguments_image);
            do_dislike = itemView.findViewById(R.id.dislike);
            total_dislikes = itemView.findViewById(R.id.argument_number);
            adView = itemView.findViewById(R.id.adView);
            if (from.equals("my")) {
                do_like.setEnabled(false);
                do_dislike.setEnabled(false);
            }
        }
    }

    private class PostUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result", result);
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject records = jsonObject.getJSONObject("records");
                JSONObject info=jsonObject.getJSONObject("info");
            }
            catch (JSONException e)
            {
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
            jsonObject.accumulate("liker", uid);
            jsonObject.accumulate("request", request);
            jsonObject.accumulate("pid", pid);
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
}
