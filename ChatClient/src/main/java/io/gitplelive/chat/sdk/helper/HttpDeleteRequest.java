/**
 * HttpDeleteRequest.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.model.ResponseError;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpDeleteRequest {

    public HttpDeleteRequest(Context context, String url, Map<String, String> headers, JSONObject jsonObject, final OnResponse listener) {
        Util.debug("[HttpDeleteRequest] DELETE request", url);
        Util.debug("[HttpDeleteRequest] headers", headers.toString());
        Util.debug("[HttpDeleteRequest] body", jsonObject.toString());

        Activity activity = (Activity) context;

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Headers.Builder builder = new Headers.Builder();
        for (String key : headers.keySet()) {
            builder.add(key, Objects.requireNonNull(headers.get(key)));
        }
        Request httpRequest = new Request.Builder()
                .url(url)
                .delete(body)
                .headers(builder.build())
                .build();
        Call call = client.newCall(httpRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                activity.runOnUiThread(() -> listener.callback(null, ResponseError.toJson(e.toString())));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    try {
                        String response2 = response.body().string();
                        Util.debug2("[HttpDeleteRequest] response", response2);
                        activity.runOnUiThread(() -> listener.callback(response2.isEmpty() ? "OK" : response2, null));
                    }
                    catch (Exception e) {
                        activity.runOnUiThread(() -> listener.callback(null, ResponseError.toJson(e.toString())));
                    }
                }
                else {
                    activity.runOnUiThread(() -> listener.callback(null, ResponseError.toJson(response.message())));
                    response.close();
                }
            }
        });
    }

} // HttpDeleteRequest.java