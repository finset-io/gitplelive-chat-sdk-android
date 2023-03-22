/**
 * HttpArrayRequest.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.model.ResponseError;


public class HttpArrayRequest {

    public Context context;
    public OnResponse listener;

    public HttpArrayRequest(final Context context, String url, Map<String, String> headers, final OnResponse listener) {
        this.context = context;
        this.listener = listener;
        Util.debug("[HttpArrayRequest] GET request", url);
        RequestQueue queue = getRequestQueue(context, url);
        queue.add(
            new ArrayRequest(url, headers, response -> {
                Util.debug2("[HttpArrayRequest] response", response.toString());
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

    public class ArrayRequest extends JsonArrayRequest {

        private Map<String, String> headers;

        public ArrayRequest(String url, Map<String, String> headers, Response.Listener<JSONArray> listener2) {
            super(Method.GET, url, null, listener2, HttpArrayRequest.this::onError);

            this.headers = headers;
        }

        @Override
        public Map<String, String> getHeaders() {
            if (headers != null) {
                Util.debug("[HttpArrayRequest] headers", headers.toString());
            }
            else headers = new HashMap<>();
            return headers;
        }
    }

    public static RetryPolicy retryPolicy = new DefaultRetryPolicy(
            60000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

} // HttpArrayRequest.java