package es.otherperspectiv.myapplication;


import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_RESTAURANT_ID = "restaurantId";
    private static final String KEY_IS_MANAGER = "manager";
    private static final String KEY_ADMIN_LEVEL = "admin";

    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String name, int restaurantId, int isManager, int adminLevel){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putInt(KEY_RESTAURANT_ID, restaurantId);
        editor.putInt(KEY_IS_MANAGER, isManager);
        editor.putInt(KEY_ADMIN_LEVEL, adminLevel);
        editor.apply();

        return true;
    }

    public int getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public boolean setManager(int restaurantId){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_RESTAURANT_ID, restaurantId);
        editor.putInt(KEY_IS_MANAGER, 1);
        editor.apply();
        return true;
    }

    public boolean isManager(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getInt(KEY_IS_MANAGER, -1) != 0){
            return true;
        }
        return false;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME, null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
