/**
 * ChatClient.java
 *
 */

package io.gitplelive.chat.sdk.api;


import android.annotation.SuppressLint;
import android.content.Context;

import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.sdk.ChatClientSdk;


public class GitpleLiveChat extends ChatClientSdk {

    @SuppressLint("StaticFieldLeak")
    private static GitpleLiveChat gitpleLiveChat;

    private static UsersApi usersApi;
    private static GroupChannelApi groupChannelApi;

    public GitpleLiveChat(Context context, String host, String appId) {
        super(context, host, appId);
    }

    public static void init(Context context, String host, String appId) {
        if (gitpleLiveChat == null) {
            gitpleLiveChat = new GitpleLiveChat(context, host, appId);
        }

        Util.init(context);
    }

    public static GitpleLiveChat getInstance() {
        assert gitpleLiveChat != null;
        return gitpleLiveChat;
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