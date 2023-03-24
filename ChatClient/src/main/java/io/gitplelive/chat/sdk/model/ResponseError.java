/**
 * ResponseError.java
 *
 */

package io.gitplelive.chat.sdk.model;

import com.google.gson.Gson;


public class ResponseError {

    public int status = 0;
    public String timestamp;
    public String path;
    public String message;
    public int code = ErrorType.UNKNOWN_ERROR;

    public ResponseError(String message) {
        this.message = message;
    }
    
    public static String toJson(String message) {
        return new Gson().toJson(new ResponseError(message));
    }


    public static ResponseError fromJson(String json) {
        return new Gson().fromJson(json, ResponseError.class);
    }

} // ResponseError.java