/**
 * GroupChannelMessage.java
 *
 */

package io.gitplelive.chat.sdk.sdk;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.gitplelive.chat.sdk.helper.HttpDeleteRequest;
import io.gitplelive.chat.sdk.helper.HttpRequest;
import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.model.ResponseError;


public class GroupChannelMessageSdk {

    public Context context;
    private final String url_group_channels;
    private final Map<String, String> headers = new HashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url_group_channels;
    }

    public GroupChannelMessageSdk(Context context, String host, String appId, String userId, String token) {
        this.context = context;
        url_group_channels = "https://" + host + "/v1/sdk/group/channels/";

        headers.put("APP_ID", appId);
        headers.put("USER_ID", userId);
        headers.put("Authorization", "Bearer " + token);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
    }

    public void create(String channelId, String type, String content, Map<String, String> meta, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", type);
            jsonObject.put("content", content);
            if (meta != null) {
                jsonObject.put("meta", new JSONObject(meta));
            }
            new HttpRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void delete(String channelId, long messageId, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages/" + messageId;

        new HttpRequest(context, "DELETE", url, headers, null, listener);
    }

    public void deleteMeta(String channelId, long messageId, String[] keys, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages/" + messageId + "/meta";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("keys", new JSONArray(keys));

            new HttpDeleteRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void get(String channelId, long messageId, String mode, String type, long limit, String content, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages";
        String params = "";
        if (messageId > 0)
            params += "base_message_id=" + messageId;
        if (mode != null)
            params += (params.isEmpty() ? "" : "&") + "mode=" + mode;
        if (type != null)
            params += (params.isEmpty() ? "" : "&") + "type=" + type;
        if (limit > 0)
            params += (params.isEmpty() ? "" : "&") + "limit=" + limit;
        if (content != null)
            params += (params.isEmpty() ? "" : "&") + "content=" + content;
        if (!params.isEmpty()) url += "?" + params;

        new HttpRequest(context, url, headers, listener);
    }

    public void updateMeta(String channelId, long messageId, Map<String, String> meta, OnResponse listener) {
        String url = url_group_channels + channelId + "/messages/" + messageId + "/meta";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("meta", new JSONObject(meta));

            new HttpRequest(context, "PUT", url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

} // GroupChannelMessage.java