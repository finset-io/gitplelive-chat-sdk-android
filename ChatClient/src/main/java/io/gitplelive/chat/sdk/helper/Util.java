/**
 * Util.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.List;


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

    public static boolean isActivityRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        return runningTasks.size() > 0;
    }

    public static boolean isActivityRunning(Context context, Class<? extends Activity> activityClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : runningTasks) {
            ComponentName componentName = task.topActivity;
            if (componentName.getClassName().equals(activityClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String androidVersion() {
        switch (Build.VERSION.SDK_INT) {
            case 16 : return "Android 4.1, 4.1.1 (Jelly Bean)";
            case 17 : return "Android 4.2, 4.2.2 (Jelly Bean)";
            case 18 : return "Android 4.3 (Jelly Bean)";
            case 19 : return "Android 4.4 (KitKat)";
            case 20 : return "Android 4.4W (KitKat Wear)";
            case 21 : return "Android 5.0 (Lollipop)";
            case 22 : return "Android 5.1 (Lollipop)";
            case 23 : return "Android 6.0 (Marshmallow)";
            case 24 : return "Android 7.0 (Nougat)";
            case 25 : return "Android 7.1.1 (Nougat)";
            case 26 : return "Android 8.0 (Oreo)";
            case 27 : return "Android 8.1 (Oreo)";
            case 28 : return "Android 9.0 (Pie)";
            case 29 : return "Android Q";
            case 30 : return "Android 11";
            case 31 : return "Android 12";
            case 32 : return "Android 12L";
            case 33 : return "Android 13";
        }
        return "Unknown version";
    }

} // Util.java
