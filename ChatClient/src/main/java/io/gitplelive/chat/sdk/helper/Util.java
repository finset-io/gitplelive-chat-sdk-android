/**
 * Util.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.GsonBuilder;


public class Util {

    private static SharedPref sharedPref;

    public static void init(Context context) {
        sharedPref = new SharedPref(context);
    }

    private static void print(String tag, String... args) {
        String text = "";
        for (String arg: args) text += (text.isEmpty() ? "" : " ") + arg;
        if (tag.equals("debug"))
            Log.d( "[debug]", text);
        else
            Log.e( "[debug]", text);
    }

    private static void print(String tag, long... args) {
        String text = "";
        for (long arg: args) text += (text.isEmpty() ? "" : " ") + arg;
        if (tag.equals("debug"))
            Log.d( "[debug]", text);
        else
            Log.e( "[debug]", text);
    }

    public static void debug2(String... args) {
        Log.d( "[debug]", args[0] + " length: " + args[1].length());
    }

    public static void debug(String... args) {
        print("debug", args);
    }

    public static void debug(long... args) {
        print("debug", args);
    }

    public static void error(String... args) {
        print("error", args);
    }

    public static void error(long... args) {
        print("error", args);
    }

    public static int getInt(String value) {
        try {
            return Integer.parseInt(value);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static long getLong(String value) {
        try {
            return Long.parseLong(value);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static double getDouble(String str) {
        if (str == null || str.isEmpty()) return 0;
        try {
            return Double.parseDouble(str.trim());
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static void save(String key, String value) {
        sharedPref.putPref(key, value);
    }

    public static String load(String key) {
        String value = sharedPref.getPref(key);
        return value.equals("null") ? "" : value;
    }

    public static <T> String toJson(T object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

} // Util.java
