/**
 * GroupChannel.java
 *
 */

package io.gitplelive.chat.sdk.sdk;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.gitplelive.chat.sdk.helper.HttpArrayRequest;
import io.gitplelive.chat.sdk.helper.HttpDeleteRequest;
import io.gitplelive.chat.sdk.helper.HttpRequest;
import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.model.ChannelPage;
import io.gitplelive.chat.sdk.model.GroupChannel;
import io.gitplelive.chat.sdk.model.ResponseError;


public class GroupChannelSdk {

    public Context context;
    private final String url_group_channels;
    private final Map<String, String> headers = new HashMap<>();

    public GroupChannelSdk(Context context, String host, String appId, String userId) {
        this.context = context;
        url_group_channels = "https://" + host + "/v1/sdk/group/channels/";

        headers.put("APP_ID", appId);
        headers.put("USER_ID", userId);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
    }

    public void setToken(String token) {
        headers.put("Authorization", "Bearer " + token);
    }

    public void ban(String channelId, String userId, int seconds, String reason, OnResponse listener) {
        String url = url_group_channels + channelId + "/ban";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", userId);
            jsonObject.put("seconds", seconds);
            if (reason != null) {
                jsonObject.put("reason", reason);
            }
            new HttpRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void create(String channelId,
                       String name,
                       String profile,
                       String[] members,
                       boolean reuse,
                       Map<String, String> meta,
                       OnResponse listener) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channel_id", channelId);
            jsonObject.put("name", name);
            if (profile != null) {
                jsonObject.put("profile_url", profile);
            }
            jsonObject.put("members", new JSONArray(members));
            jsonObject.put("reuse", reuse);
            if (meta != null) {
                jsonObject.put("meta", new JSONObject(meta));
            }
            new HttpRequest(context, url_group_channels, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void delete(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId;

        new HttpRequest(context,"DELETE", url, headers, null, listener);
    }

    public void deleteMeta(String channelId, String[] keys, OnResponse listener) {
        String url = url_group_channels + channelId + "/meta";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("keys", new JSONArray(keys));

            new HttpDeleteRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void delivered(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages/delivered";

        new HttpRequest(context, "PUT", url, headers, null, listener);
    }

    public void findAll(OnResponse listener) {
        new HttpRequest(context, url_group_channels, headers, listener);
    }

    public void findAll(String next,
                        int limit,
                        boolean showMembers,
                        boolean showManagers,
                        boolean showReadReceipt,
                        boolean showDeliveryReceipt,
                        boolean showUnread,
                        boolean showLastMessage,
                        String name,
                        String includeMembers,
                        OnResponse listener) {
        String url = url_group_channels;

        url += "?limit=" + ((limit < 5 || limit > 30) ? 15 : limit);
        url += "&show_members=" + showMembers;
        url += "&show_managers=" + showManagers;
        url += "&show_read_receipt=" + showReadReceipt;
        url += "&show_delivery_receipt=" + showDeliveryReceipt;
        url += "&show_unread=" + showUnread;
        url += "&show_last_message=" + showLastMessage;
        if (name != null) url += "&name=" + name;
        if (includeMembers != null) url += "&include_members=" + includeMembers;
        if (next != null) url += "&next=" + next;

        new HttpRequest(context, url, headers, listener);
    }

    //-----------------------------------------------------------------------
    // 내부용: 4. mqtt topic 구독용 채널 조회
    //-----------------------------------------------------------------------
    public void findAllJoined(OnResponse listener) {
        findAllJoined(new ArrayList<>(), null, listener);
    }

    public void findAllJoined(List<GroupChannel> list, String next, OnResponse listener) {
        String url = url_group_channels + "joined/list?show_managers=true&limit=30";
        if (next != null) {
            url += "&next=" + next;
        }
        new HttpRequest(context, url, headers, (response, error) -> {
            if (error != null) {
                listener.callback(null, error);
                return;
            }
            try {
                ChannelPage channelPage = new Gson().fromJson(response, ChannelPage.class);
                list.addAll(Arrays.asList(channelPage.channels));
                if (channelPage.next != null) {
                    findAllJoined(list, channelPage.next, listener);
                }
                else {
                    channelPage.channels = list.toArray(new GroupChannel[channelPage.channels.length]);
                    listener.callback(new Gson().toJson(channelPage), null);
                }
            }
            catch (Exception e) {
                listener.callback(null, e.toString());
            }
        });
    }

    public void findAllJoined(String next,
                              int limit,
                              boolean showMembers,
                              boolean showManagers,
                              boolean showReadReceipt,
                              boolean showDeliveryReceipt,
                              boolean showUnread,
                              boolean showLastMessage,
                              String name,
                              String includeMembers,
                              OnResponse listener) {
        String url = url_group_channels + "joined/list";

        url += "?limit=" + ((limit < 5 || limit > 30) ? 15 : limit);
        url += "&show_members=" + showMembers;
        url += "&show_managers=" + showManagers;
        url += "&show_read_receipt=" + showReadReceipt;
        url += "&show_delivery_receipt=" + showDeliveryReceipt;
        url += "&show_unread=" + showUnread;
        url += "&show_last_message=" + showLastMessage;
        if (name != null) url += "&name=" + name;
        if (includeMembers != null) url += "&include_members=" + includeMembers;
        if (next != null) url += "&next=" + next;

        new HttpRequest(context, url, headers, listener);
    }

    public void findBanList(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/banned_list";

        new HttpArrayRequest(context, url, headers, listener);
    }

    public void findManagers(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/managers";

        new HttpArrayRequest(context, url, headers, listener);
    }

    public void findMembers(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/members";

        new HttpArrayRequest(context, url, headers, listener);
    }

    public void findOne(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId;

        new HttpRequest(context, url, headers, listener);
    }

    public void findOnlineMembers(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/online_members";

        new HttpArrayRequest(context, url, headers, listener);
    }

    public void freeze(String channelId, boolean freeze, OnResponse listener) {
        String url = url_group_channels + channelId + "/freeze";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("freeze", freeze);

            new HttpRequest(context, "PUT", url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void join(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/join";

        new HttpRequest(context, "PUT", url, headers, null, listener);
    }

    public void leave(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/leave";

        new HttpRequest(context, "PUT", url, headers, null, listener);
    }

    public void read(String channelId, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages/read";

        new HttpRequest(context, "PUT", url, headers, null, listener);
    }

    public void registerManager(String channelId, String userId, OnResponse listener) {
        String url = url_group_channels + channelId + "/managers";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("manager", userId);

            new HttpRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void unban(String channelId, String userId, OnResponse listener) {
        String url = url_group_channels + channelId + "/ban/" + userId;

        new HttpRequest(context, "DELETE", url, headers, null, listener);
    }

    public void unregisterManager(String channelId, String userId, OnResponse listener) {
        String url = url_group_channels + channelId + "/managers";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("manager", userId);

            new HttpDeleteRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void update(String channelId, String name, String profile, OnResponse listener) {
        String url = url_group_channels + channelId;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("profile_url", profile);

            new HttpRequest(context, "PUT", url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void updateMeta(String channelId, Map<String, String> meta, OnResponse listener) {
        String url = url_group_channels + channelId + "/meta";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("meta", new JSONObject(meta));

            new HttpRequest(context, "PUT", url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

} // GroupChannel.java