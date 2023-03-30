/**
 * BaseMessage.java
 *
 */

package io.gitplelive.chat.sdk.model;

import java.util.Date;
import java.util.Map;


public class BaseMessage {

    public static class Filter {
        String origin_content;
        String[] type = { "profanity" };
    }

    public static class FileInfo {
        String type;
        String name;
        String url;
        String size;
    }

    String type;
    long message_id;
    String channel_id;
    long created_at;
    long updated_at;
    BaseUser user;
    String content;
    Filter filter;
    FileInfo file;
    Map<String, String> meta;

    public long getId() { return message_id; }

    public BaseUser getUser() {
        if (user != null) return user;
        user = new BaseUser("N/A", "N/A");
        return user;
    }

    public String getContent() {
        if (type.equals("text")) {
            return getText();
        }
        else if (isImage(getUrl())) {
            return "\uD83D\uDDBC";
        }
        else return "\uD83D\uDCCE";
    }

    public boolean isImage(String url) {
        return  url.toLowerCase().endsWith(".jpeg") ||
                url.toLowerCase().endsWith(".jpg") ||
                url.toLowerCase().endsWith(".png") ||
                url.toLowerCase().endsWith(".gif");
    }

    public String getText() {
        if (!type.equals("text")) return null;

        if (meta != null && meta.get("text") != null) {
            return meta.get("text") + " \uD83D\uDD8A";
        }
        return content;
    }

    public String getUrl() {
        if (!type.equals("file") || file == null) return null;

        return file.url;
    }

    public String getMeta(String key) {
        if (meta == null) return null;
        return meta.get(key);
    }

    public Date getDate() {
        return new Date(created_at);
    }

    public long getTimestamp() { return (long) created_at; }

} // BaseMessage.java