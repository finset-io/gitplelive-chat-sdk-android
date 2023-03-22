/**
 * HttpRequest.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.model.ResponseError;


public class HttpRequest {

    public Context context;
    public OnResponse listener;
    public static String cookie;
    public static String address;

    public HttpRequest(final Context context, String url, Map<String, String> headers, final OnResponse listener) {
        this.context = context;
        this.listener = listener;
        Util.debug("[HttpRequest] GET request", url);
        RequestQueue queue = getRequestQueue(context, url);
        queue.add(
            new JsonRequest(url, headers, response -> {
                Util.debug2("[HttpRequest] response", response.toString());
                listener.callback(response.toString(), null);
            })
        ).setRetryPolicy(retryPolicy);
    }

    public HttpRequest(final Context context, String url, Map<String, String> headers, JSONObject jsonObject, final OnResponse listener) {
        this.context = context;
        this.listener = listener;
        if (jsonObject == null) jsonObject = new JSONObject();
        Util.debug("[HttpRequest] POST request", url);
        RequestQueue queue = getRequestQueue(context, url);
        queue.add(
                new JsonRequest(url, headers, jsonObject, response -> {
                    Util.debug2("[HttpRequest] response", response.toString());
                    listener.callback(response.toString(), null);
                })
        ).setRetryPolicy(retryPolicy);
    }

    public HttpRequest(final Context context, String method, String url, Map<String, String> headers, JSONObject jsonObject, final OnResponse listener) {
        this.context = context;
        this.listener = listener;
        if (jsonObject == null) jsonObject = new JSONObject();
        Util.debug("[HttpRequest]", method, "request", url);
        RequestQueue queue = getRequestQueue(context, url);
        queue.add(
                new JsonRequest(method, url, headers, jsonObject, response -> {
                    Util.debug2("[HttpRequest] response", response.toString());
                    listener.callback(response.toString(), null);
                })
        ).setRetryPolicy(retryPolicy);
    }

    public RequestQueue getRequestQueue(Context context, String url) {
        if (url.startsWith("https://")) {
            HttpsTrustManager.allowAllSSL();
        }
        return Volley.newRequestQueue(context);
    }

    @SuppressLint("DefaultLocale")
    public void onError(VolleyError error) {
        if (error.networkResponse == null) {
            Util.debug("[HttpRequest] response", "OK");
            listener.callback("OK", null);
            return;
        }
        try {
            listener.callback(null, new String(error.networkResponse.data));
        }
        catch (Exception e) {
            listener.callback(null, ResponseError.toJson(e.toString()));
        }
    }

    public class JsonRequest extends JsonObjectRequest {

        private Map<String, String> headers;
        private JSONObject jsonObject;

        public JsonRequest(String url, Map<String, String> headers, Response.Listener<JSONObject> listener2) {
            super(Method.GET, url, null, listener2, HttpRequest.this::onError);

            this.headers = headers;
        }

        public JsonRequest(String url, Map<String, String> headers, JSONObject jsonObject, Response.Listener<JSONObject> listener2) {
            super(Method.POST, url, jsonObject, listener2, HttpRequest.this::onError);

            this.headers = headers;
            this.jsonObject = jsonObject;
        }

        public JsonRequest(String method, String url, Map<String, String> headers, JSONObject jsonObject, Response.Listener<JSONObject> listener2) {
            super(method.equals("PUT") ? Method.PUT : Method.DELETE, url, jsonObject, listener2, HttpRequest.this::onError);

            this.headers = headers;
            this.jsonObject = jsonObject;
        }

        @Override
        public Map<String, String> getHeaders() {
            if (headers != null) {
                Util.debug("[HttpRequest] headers", headers.toString());
            }
            else headers = new HashMap<>();
            return headers;
        }

        @Override
        public byte[] getBody() {
            String json = jsonObject.toString();
            Util.debug("[HttpRequest] body", json);
            return json.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            if (response == null || response.headers == null) {
                return super.parseNetworkResponse(response);
            }
            cookie = response.headers.get("Set-Cookie");
            address = response.headers.get("x-forwarded-for");
            if (cookie != null && !cookie.isEmpty()) {
                Util.debug("[HttpRequest] Set-Cookie", cookie);
            }
            if (address != null && !address.isEmpty()) {
                Util.debug("[HttpRequest] x-forwarded-for", address);
            }
            return super.parseNetworkResponse(response);
        }
    }

    public static RetryPolicy retryPolicy = new DefaultRetryPolicy(
            60000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

} // HttpRequest.java