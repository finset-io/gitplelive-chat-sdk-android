/**
 * SharedPref.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {

    static SharedPreferences mPref;

    public SharedPref(Context context) {
        String file = "pref";
        mPref = context.getSharedPreferences(file, Activity.MODE_PRIVATE);
    }

    public String getPref(String key) {
        return mPref.getString(key, "");
    }

    public void putPref(String key, String value) {
        SharedPreferences.Editor editor = mPref.edit();
        if (value == null)
            editor.remove(key);
        else
            editor.putString(key, value);
        editor.apply();
    }

} // SharedPref.java