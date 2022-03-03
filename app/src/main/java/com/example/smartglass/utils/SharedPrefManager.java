package com.example.smartglass.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smartglass.model.User;


public class SharedPrefManager {

    private static final String KEY_ID = "keyid";
    private static final String SHARED_PREF_NAME = "generalFile";
    private static final String KEY_USER_F_NAME = "keyfname";
    private static final String KEY_USER_L_NAME = "keylname";
    private static final String KEY_SMART_GLASS_ID = "smartGlassId";


    private static SharedPrefManager mInstance;
    private static Context context;

    private SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    //customer
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putInt(KEY_SMART_GLASS_ID, user.getSmartGlassId());
        editor.putString(KEY_USER_F_NAME, user.getFname());
        editor.putString(KEY_USER_L_NAME, user.getLname());
        editor.apply();
    }


    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }


    //this method will give the logged in user id
    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1);
    }


    public int getGlassID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SMART_GLASS_ID, -1);
    }


    //this method will give the logged in user
    public User getUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_SMART_GLASS_ID, -1),
                sharedPreferences.getString(KEY_USER_F_NAME, null),
                sharedPreferences.getString(KEY_USER_L_NAME, null)
        );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }





}
