/**
 * UploadHelper.java
 *
 */

package io.gitplelive.chat.sdk.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class UploadHelper {

    private static final String CRLF = "\r\n";
    private static final String HYPHENS = "--";
    private static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    private final String url;
    private final File file;
    private final Map<String, String> headers;
    private DataOutputStream dataStream;

    public UploadHelper(String url, File file, Map<String, String> headers) {
        this.url = url;
        this.file = file;
        this.headers = headers;
    }

    public String upload() {
        Util.debug("[UploadHelper] upload:", url, file.getPath(), headers.toString());
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            URL connectURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("APP_ID", headers.get("APP_ID"));
            conn.setRequestProperty("USER_ID", headers.get("USER_ID"));
            conn.setRequestProperty("Authorization", headers.get("Authorization"));
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            conn.connect();

            dataStream = new DataOutputStream(conn.getOutputStream());

            writeFormField("type", "file");
            writeFileField("file", file.getPath(), "*/*", fileInputStream);

            dataStream.writeBytes(HYPHENS + BOUNDARY + HYPHENS + CRLF);

            fileInputStream.close();
            dataStream.flush();
            dataStream.close();
            dataStream = null;

            return getResponse(conn);
        }
        catch (Exception e) {
            Util.error("[UploadRequest] upload:", e.toString());
        }
        return "";
    }

    private String getResponse(HttpURLConnection conn) {
        try {
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte [] data = new byte[1024];
            int len = dis.read(data, 0, 1024);
            dis.close();
            if (len > 0) return new String(data, 0, len);
        }
        catch(Exception e) {
            Util.error("[UploadRequest] getResponse", e.toString());
        }
        return "";
    }

    private void writeFormField(String fieldName, String fieldValue)  {
        try  {
            dataStream.writeBytes(HYPHENS + BOUNDARY + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            dataStream.writeBytes(fieldValue);
            dataStream.writeBytes(CRLF);
        } catch(Exception e) {
            Util.error("[UploadRequest] writeFormField", e.toString());
        }
    }

    private void writeFileField(String fieldName, String fieldValue, String type, FileInputStream fileInputStream) {
        try {
            dataStream.writeBytes(HYPHENS + BOUNDARY + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                    + fieldName
                    + "\";filename=\""
                    + fieldValue
                    + "\""
                    + CRLF);
            dataStream.writeBytes("Content-Type: " + type +  CRLF);
            dataStream.writeBytes(CRLF);

            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0)   {
                dataStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataStream.writeBytes(CRLF);
        }
        catch(Exception e)  {
            Util.error("[UploadRequest] writeFormField", type, e.toString());
        }
    }

} // UploadHelper.java