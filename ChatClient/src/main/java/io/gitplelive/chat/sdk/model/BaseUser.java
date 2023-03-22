/**
 * BaseUser.java
 *
 */

package io.gitplelive.chat.sdk.model;

import java.util.Map;


public class BaseUser {

    private final String user_id;
    private final String name;
    private long created_at;
    private long updated_at;
    private String profile_url;
    private Map<String, String> meta;
    private long joined_at;

    public BaseUser(String userId, String userName) {
        user_id = userId;
        name = userName;
    }

    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile_url;
    }

    public String getMeta(String key) {
        return meta == null || meta.get(key) == null ? "" : meta.get(key);
    }

} // BaseUser.java