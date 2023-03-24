/**
 * ChatClient.java
 *
 */

package io.gitplelive.chat.sdk.api;


import android.annotation.SuppressLint;
import android.content.Context;

import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.sdk.ChatClientSdk;


public class ChatClient extends ChatClientSdk {

    @SuppressLint("StaticFieldLeak")
    private static ChatClient chatClient;

    private static UsersApi usersApi;
    private static GroupChannelApi groupChannelApi;

    public ChatClient(Context context, String host, String appId) {
        super(context, host, appId);
    }

    public static void init(Context context, String host, String appId) {
        if (chatClient == null) {
            chatClient = new ChatClient(context, host, appId);
        }

        Util.init(context);
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