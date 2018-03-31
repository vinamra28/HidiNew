package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by HP on 10-Mar-18.
 */

public class SessionManager
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private static final String IS_LOGIN="isLoggedIn";
    private static final String PREF_NAME="Hidi_Session";
    public static final String KEY_MOBILE="mobileno";
    public static final String KEY_UID="uid";
    public static final String KEY_NAME="username";
    public static final String KEY_SECNAME="secretName";
    public static final String KEY_PROFILEPIC="profilepic";
    public static final String KEY_INDEXPATH="indexpath";
    public static final String KEY_ADMIRE="admire";
    public static final String KEY_LOVE="love";
    public static final String KEY_POPULARITY="popularity";
    public static final String KEY_VISITORS="visitors";
    public static final String KEY_HIDIES="my_hidies";
    public static final String KEY_BLOCKS="blocks";
    public static final String KEY_LAT="latitude";
    public static final String KEY_LONG="longitude";
    int PRIVATE_MODE=0;
    public SessionManager(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }
    public void createLoginSession(int uid,String mobileno)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putInt(KEY_UID,uid);
        editor.putString(KEY_MOBILE,mobileno);
        editor.commit();
    }
    public boolean isLoggedIn()
    {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
    public void checkLogin()
    {
        if(!this.isLoggedIn())
        {
            Intent i=new Intent(context,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
    void logoutUser()
    {
        editor.clear();
        editor.commit();
        Intent i=new Intent(context,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    public void accountDetails(String profilepic,String secname,int admire,int love,int visitors,double popularity,int hidies,int blocks,int indexpath)
    {
        editor.putString(KEY_PROFILEPIC,profilepic);
        editor.putString(KEY_SECNAME,secname);
        editor.putString(KEY_ADMIRE,""+admire);
        editor.putString(KEY_LOVE,""+love);
        editor.putString(KEY_VISITORS,""+visitors);
        editor.putString(KEY_HIDIES,""+hidies);
        editor.putString(KEY_BLOCKS,""+blocks);
        editor.putString(KEY_POPULARITY,""+(float)popularity);
        editor.putInt(KEY_INDEXPATH,indexpath);
        editor.commit();
    }
    public HashMap<String,String> getUserDetails()
    {
        HashMap<String,String> user=new HashMap<>();
        user.put(KEY_MOBILE,sharedPreferences.getString(KEY_MOBILE,""));
        user.put(KEY_NAME,sharedPreferences.getString(KEY_NAME,""));
        user.put(KEY_SECNAME,sharedPreferences.getString(KEY_SECNAME,""));
        user.put(KEY_ADMIRE,sharedPreferences.getString(KEY_ADMIRE,"0"));
        user.put(KEY_LOVE,sharedPreferences.getString(KEY_LOVE,"0"));
        user.put(KEY_POPULARITY,sharedPreferences.getString(KEY_POPULARITY,"0.0"));
        user.put(KEY_VISITORS,sharedPreferences.getString(KEY_VISITORS,"0"));
        user.put(KEY_HIDIES,sharedPreferences.getString(KEY_HIDIES,"0"));
        user.put(KEY_BLOCKS,sharedPreferences.getString(KEY_BLOCKS,"0"));
        return user;
    }
    public void saveLoc(double latitude,double longitude)
    {
        editor.putString(KEY_LAT,""+latitude);
        editor.putString(KEY_LONG,""+longitude);
        editor.commit();
    }
    public int getUID()
    {
        return sharedPreferences.getInt(KEY_UID,0);
    }
    public int getIndex()
    {
        return sharedPreferences.getInt(KEY_INDEXPATH,0);
    }
    public String getSecname()
    {
        return sharedPreferences.getString(KEY_SECNAME,"");
    }
    public String getProfilepic()
    {
        return sharedPreferences.getString(KEY_PROFILEPIC,"http://hidi.org.in/hidi/account/image/"+getUID()+".png");
    }
    public int getVisitors()
    {
        return Integer.parseInt(sharedPreferences.getString(KEY_VISITORS,""));
    }
    public void setSecname(String secname)
    {
        editor.putString(KEY_SECNAME,secname);
    }
}
