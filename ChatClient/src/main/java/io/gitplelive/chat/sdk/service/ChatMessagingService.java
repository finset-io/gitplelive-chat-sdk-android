/**
 * ChatMessagingService.java
 *
 */

package io.gitplelive.chat.sdk.service;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.model.MessagePayload;


public class ChatMessagingService extends FirebaseMessagingService {

    public static String pushToken;

    private static final long[] VIBRATE_PATTERN = {0, 500};
    private static long timestamp = System.currentTimeMillis();
    private static String channelId = "GitLiveChat";
    private static String channelName = "GitLiveChat";

    private static Context context;
    private static int resIcon;
    private static Class<? extends Activity> activityClass;


    public static void init(Context activity, int resId, Class<? extends Activity> mainClass) {
        context = activity;
        resIcon = resId;
        activityClass = mainClass;

        createNotificationChannel();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            pushToken = task.getResult();
            Util.debug(">>> pushToken:", pushToken);
        });
    }

    private static void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setVibrationPattern(VIBRATE_PATTERN);
        channel.enableVibration(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        pushToken = s;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (Util.isActivityRunning(context, activityClass)) {
            Util.error(">>> onMessageReceived");
            return;
        }
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String data = remoteMessage.getData().get("data");
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

            long current = System.currentTimeMillis();
            long elapsed = current - timestamp;
            timestamp = current;
            if (elapsed < 1000) return;

            sendNotification((int) timestamp, title, body, data);
        }
    }

    public static void sendNotification(int id, String title, String body, String data) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();

        NotificationCompat.Builder builder = getNotificationBuilder(data);
        builder.setSmallIcon(resIcon);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSilent(true);

        getNotificationManager().notify(id, builder.build());
    }

    @NonNull
    private static NotificationCompat.Builder getNotificationBuilder(String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setSound(null, null);
            channel.enableVibration(false);
            getNotificationManager().createNotificationChannel(channel);
        }

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

    private static NotificationManager getNotificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void resetNotification() {
        getNotificationManager().cancelAll();
    }

} // ChatMessagingService.java