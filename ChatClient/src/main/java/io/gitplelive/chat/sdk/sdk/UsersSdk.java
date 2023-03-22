/**
 * Users.java
 *
 */

package io.gitplelive.chat.sdk.sdk;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.gitplelive.chat.sdk.helper.HttpDeleteRequest;
import io.gitplelive.chat.sdk.helper.HttpRequest;
import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.model.ResponseError;


public class UsersSdk {

    private final Context context;
    private final String url_users;
    private final String userId;
    private final Map<String, String> headers = new HashMap<>();

    public UsersSdk(Context context, String host, String appId, String userId) {
        this.context = context;
        this.userId = userId;

        url_users = "https://" + host + "/v1/sdk/users/";

        headers.put("APP_ID", appId);
        headers.put("USER_ID", userId);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
    }

    public void setToken(String token) {
        headers.put("Authorization", "Bearer " + token);
    }

    public void deleteMeta(String[] keys, OnResponse listener) {
        String url = url_users + userId + "/meta";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("keys", new JSONArray(keys));

            new HttpDeleteRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void find(OnResponse listener) {
        String url = url_users + "info/me";

        new HttpRequest(context, url, headers, listener);
    }

    //-----------------------------------------------------------------------
    // 내부용: 3. Client ID로 SDK 토큰 발급용
    //-----------------------------------------------------------------------
    public void generateTokenBySession(String clientId, OnResponse listener) {
        String url = url_users + "token/by_client_id";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientId", clientId);
            new HttpRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void read(String[] channels, OnResponse listener) {
        String url = url_users + "messages/read";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channels", new Gson().toJson(channels));

            new HttpRequest(context, "PUT", url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    //-----------------------------------------------------------------------
    // 내부용: 2. 입력 받은 세션 토큰으로 SDK 토큰 발급용
    //-----------------------------------------------------------------------
    public void refreshToken(OnResponse listener) {
        String url = url_users + "token";

        new HttpRequest(context, url, headers, null, listener);
    }

    //-----------------------------------------------------------------------
    // 내부용: 5. 푸시 토큰 등록용
    //-----------------------------------------------------------------------
    public void registerDeviceToken(String clientId, String pushToken, OnResponse listener) {
        String url = url_users + "device_token";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientId", clientId);
            jsonObject.put("token", pushToken);

            new HttpRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    //-----------------------------------------------------------------------
    // 내부용: 6. 푸시 토큰 삭제용
    //-----------------------------------------------------------------------
    public void deleteDeviceToken(String clientId, OnResponse listener) {
        String url = url_users + "device_token";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientId", clientId);

            new HttpDeleteRequest(context, url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void update(String name, String profile, OnResponse listener) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("profile_url", profile);

            new HttpRequest(context, "PUT", url_users, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public void updateMeta(Map<String, String> meta, OnResponse listener) {
        String url = url_users + userId + "/meta";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("meta", new JSONObject(meta));

            new HttpRequest(context, "PUT", url, headers, jsonObject, listener);
        }
        catch (JSONException e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

} // Users.java