/**
 * JWT.java
 *
 */

package io.gitplelive.chat.sdk.model;

import android.util.Base64;

import com.google.gson.Gson;

import io.gitplelive.chat.sdk.helper.Util;


public class JWT {

    public static class Header {
        String alg;
        String typ;

        public boolean isJWT() { return typ.equals("JWT"); }
    }

    public static class Body {
        String user_id;
        long iat;
        long exp;

        public String getUserId() { return user_id; }
        public boolean isExpired() { return exp < System.currentTimeMillis(); }
    }

    private String jwt;

    public JWT(String jwt) {
        this.jwt = jwt;
    }

    public String decode() {
        StringBuilder result = new StringBuilder();

        String[] parts = jwt.split("[.]");
        try {
            int index = 0;
            for (String part: parts) {
                if (index >= 2) break;
                index++;
                byte[] decodedBytes = Base64.decode(part.getBytes("UTF-8"), Base64.URL_SAFE);
                result.append(new String(decodedBytes, "UTF-8"));
            }
        }
        catch(Exception e) {
            Util.error(e.toString());
            return null;
        }
        return result.toString();
    }

    public Header getHeader() {
        try {
            String json = decode().replace("}{", "}\n{").split("\n")[0];
            return new Gson().fromJson(json, Header.class);
        }
        catch(Exception e) {
            Util.error(e.toString());
        }
        return null;
    }

    public Body getBody() {
        try {
            String json = decode().replace("}{", "}\n{").split("\n")[1];
            return new Gson().fromJson(json, Body.class);
        } catch (Exception e) {
            Util.error(e.toString());
        }
        return null;
    }

} // JWT.java