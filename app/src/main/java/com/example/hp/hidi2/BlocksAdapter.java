package com.example.hp.hidi2;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by HP on 08-Apr-18.
 */

public class BlocksAdapter extends RecyclerView.Adapter<BlocksAdapter.ViewHolder>
{
    private Context context;
    String result="";
    SessionManager session;
    private List<BlocksGet> blocksGetList=new ArrayList<>();
    BlocksGet blocksGet;
    int uid;


    public BlocksAdapter(Context context, List<BlocksGet> blocksGetList)
    {
        this.context = context;
        this.blocksGetList = blocksGetList;
        session=new SessionManager(context);
    }
    @Override
    public BlocksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_visitor, parent, false);
        BlocksAdapter.ViewHolder viewHolder = new BlocksAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        blocksGet=blocksGetList.get(holder.getAdapterPosition());
        holder.txtname.setText(blocksGet.getSecname());
        Picasso.with(context).load(blocksGet.getUser_pic()).into(holder.circleImageView);
        holder.blk_ublk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uid=blocksGetList.get(position).getBlock_uid();
                new Unblocker().execute("http://hidi.org.in/hidi/account/update.php");
                blocksGetList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return blocksGetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView circleImageView;
        TextView txtname;
        Button blk_ublk;
        public ViewHolder(View itemView)
        {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.blocked_pic);
            txtname = itemView.findViewById(R.id.blocked_name);
            blk_ublk=itemView.findViewById(R.id.unblockUser);
        }
    }
    private class Unblocker extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... url) {
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
            jsonObject.accumulate("uidVisitor",session.getUID());
            jsonObject.accumulate("request","unblock");
            jsonObject.accumulate("uidProfile",uid);
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
            {
                Log.d("if","if");
                result=convertInputStreamToString(inputStream);
            }
            else
            {
                Log.d("else","else");
                result = "Did not work!";
            }

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
        Log.d("YEs","YEs");
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
        Log.d("result",result);
        return result;
    }
}
