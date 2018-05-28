package es.otherperspectiv.myapplication;


import android.content.Context;
import android.content.SharedPreferences;


public class User {
    private static User mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_RESTAURANT_ID = "restaurantId";
    private static final String KEY_IS_MANAGER = "manager";
    private static final String KEY_ORDER = "order";
    private static final String KEY_ORDER_PRICE = "orderPrice";
    private static final String KEY_TOKEN = "token";

    private User(Context context) {
        mCtx = context;

    }

    public static synchronized User getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new User(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String name, int restaurantId, int isManager, int adminLevel, String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putInt(KEY_RESTAURANT_ID, restaurantId);
        editor.putInt(KEY_IS_MANAGER, isManager);
        editor.putString(KEY_TOKEN, token);
        editor.apply();

        return true;
    }

    public boolean addItemToOrder(String itemName, int itemPrice){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ORDER, sharedPreferences.getString(KEY_ORDER, "") + " " + itemName );
        editor.putInt(KEY_ORDER_PRICE, sharedPreferences.getInt(KEY_ORDER_PRICE, 0) + itemPrice);
        editor.apply();
        return true;
    }

    public boolean deleteOrders(){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ORDER, "");
        editor.putInt(KEY_ORDER_PRICE, 0);
        editor.apply();
        return true;
    }

    public String getOrderDescription(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ORDER, "");
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public int getOrderPrice(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ORDER_PRICE, -1);
    }

    public int getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "Default");
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, "Default");
    }

    public int getUserRestaurantId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_RESTAURANT_ID, -1);
    }

    public boolean setManager(int restaurantId){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_RESTAURANT_ID, restaurantId);
        editor.putInt(KEY_IS_MANAGER, 1);
        editor.apply();
        return true;
    }

    public boolean setName(String name){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.apply();
        return true;
    }

    public boolean setUsername(String username){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
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
