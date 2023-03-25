/**
 * MessagingHelper.java
 *
 */

package io.gitplelive.chat.sdk.sdk;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.Map;

import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.model.MessagePayload;


public class MessagingHelper {

    public static MessagingHelper messagingHelper;
    private static final String channelId = "GitLive Chat";
    private static final String channelName = "GitLive Chat";

    public static MessagingHelper getInstance() {
        return messagingHelper;
    }

    public static void init(Context context) {
        if (messagingHelper == null)
            messagingHelper = new MessagingHelper();

        createNotificationChannel(context);
    }

    private static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.setSound(null, null);
        channel.enableVibration(false);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.cancelAll();
    }

    public static void setToken(String token) {
        Util.save("new_push_token", token);
        Util.debug(">>> pushToken:", Util.load("new_push_token"));
    }

    public static void resetToken() {
        Util.save("push_token", null);
    }

    public static String getToken() {
        String newToken = Util.load("new_push_token");
        if (Util.load("push_token").equals(newToken)) return null;
        Util.save("push_token", newToken);
        return newToken;
    }

    public void onMessageReceived(Context context, Map<String, String> receivedData, Class<? extends Activity> activityClass, int smallIcon) {
        if (Util.isActivityRunning(context, activityClass)) {
            Util.error(">>> onMessageReceived", "Activity is running");
            return;
        }
        if (receivedData.size() == 0) {
            Util.error(">>> onMessageReceived", "Data size is 0");
            return;
        }
        String title = receivedData.get("title");
        String body = receivedData.get("body");
        String data = receivedData.get("data");
        Util.debug(">>> onMessageReceived", title, body);
        if (data != null) {
            try {
                MessagePayload payload = new Gson().fromJson(data, MessagePayload.class);
                Util.debug(Util.toJson(payload));
            }
            catch (Exception e) {
                Util.debug(data);
                Util.error(e.toString());
            }
        }

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        NotificationCompat.Builder builder = getNotificationBuilder(context, data, activityClass);
        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSilent(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @NonNull
    private NotificationCompat.Builder getNotificationBuilder(Context context, String data, Class<? extends Activity> activityClass) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.setSound(null, null);
        channel.enableVibration(false);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Intent notificationIntent = new Intent(context, activityClass);
        notificationIntent.putExtra("data", data);

        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flag |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, flag);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setColor(Color.WHITE);
        builder.setContentIntent(pendingIntent);
        builder.setLocalOnly(true);
        builder.setAutoCancel(true);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);

        return builder;
    }

} // MessagingHelper.java