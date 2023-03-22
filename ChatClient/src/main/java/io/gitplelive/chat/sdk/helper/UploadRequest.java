/**
 * UploadRequest.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;

import java.io.File;
import java.util.Map;

import io.gitplelive.chat.sdk.interfaces.OnResponse;


public class UploadRequest {

    private final View loading;
    private final String url;
    private final File file;
    private final Map<String, String> headers;
    private final OnResponse listener;


    public UploadRequest(View loading, String url, File file, Map<String, String> headers, final OnResponse listener) {
        this.loading = loading;
        this.url = url;
        this.file = file;
        this.headers = headers;
        this.listener = listener;

        new UploadTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class UploadTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            if (loading != null) loading.setVisibility(VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return new UploadHelper(url, file, headers).upload();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (loading != null) loading.setVisibility(GONE);
            listener.callback(result, null);
        }
    }

} // UploadRequest.java