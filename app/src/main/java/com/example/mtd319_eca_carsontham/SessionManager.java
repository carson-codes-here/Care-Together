package com.example.mtd319_eca_carsontham;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey",0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(boolean login){
        editor.putBoolean("LOGIN_KEY", login);
        editor.commit();
    }

    public Boolean getLogin(){ return sharedPreferences.getBoolean("LOGIN_KEY", false);}


    public void setUser(String username, int userId){
        editor.putString("USERNAME_KEY", username);
        editor.putInt("USER_ID", userId);
        editor.commit();
    }

    public String getUsername() {
        return sharedPreferences.getString("USERNAME_KEY","");
    }

    public int getUserId(){
        return sharedPreferences.getInt("USER_ID", -1);
    }
}
