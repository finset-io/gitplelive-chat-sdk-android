/**
 * ChatClient.java
 *
 */

package io.gitplelive.chat.sdk.api;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.sdk.ChatClientSdk;
import io.gitplelive.chat.sdk.service.ChatMessagingService;


public class ChatClient extends ChatClientSdk {

    @SuppressLint("StaticFieldLeak")
    private static ChatClient chatClient;

    private static UsersApi usersApi;
    private static GroupChannelApi groupChannelApi;

    public ChatClient(Context context, String host, String appId) {
        super(context, host, appId);
    }

    public static void init(Activity activity, String host, String appId, int resIcon) {
        if (chatClient == null) {
            chatClient = new ChatClient(activity, host, appId);
        }

        Util.init(activity);
        ChatMessagingService.init(activity, resIcon);
        ChatMessagingService.resetNotification();
    }

    public static ChatClient getInstance() {
        assert chatClient != null;
        return chatClient;
    }

    public static UsersApi user() {
        if (usersApi == null) {
            usersApi = new UsersApi();
        }
        return usersApi;
    }

    public static GroupChannelApi groupChannel() {
        if (groupChannelApi == null) {
            groupChannelApi = new GroupChannelApi();
        }
        return groupChannelApi;
    }

} // ChatClient.java