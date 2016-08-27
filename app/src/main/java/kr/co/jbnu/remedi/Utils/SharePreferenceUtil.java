package kr.co.jbnu.remedi.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import kr.co.jbnu.remedi.models.User;

/**
 * Created by seokhyeon on 2016-08-27.
 */
public class SharePreferenceUtil {
    private static SharePreferenceUtil sharePreferenceUtil;

    private static String usertable = "Usertable";
    private static String getuser = "user";

    public User getUser(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(usertable, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(getuser, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void storeUser(User user, Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(usertable, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(getuser, json);
        System.out.println("저장하는 제이슨 메인 : "+json);
        prefsEditor.commit();
    }

    public void deleteUser(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(usertable, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(getuser, "");
        prefsEditor.commit();
    }



    public static synchronized SharePreferenceUtil getInstance () {
        if (sharePreferenceUtil==null){
            sharePreferenceUtil = new SharePreferenceUtil();
        }
        return sharePreferenceUtil;
    }


}
