/**
 * GroupChannel.java
 *
 */

package io.gitplelive.chat.sdk.model;

import java.util.Map;

import io.gitplelive.chat.sdk.helper.Util;


public class GroupChannel {

    public String channel_id;
    public String type = "group";
    public String name;
    public boolean freeze;
    public int total_message_count;
    public int total_file_count;
    public long created_at;
    public long updated_at;
    public String profile_url;
    public Map<String, String> meta;
    public BaseUser[] members;
    public BaseUser[] managers;
    public Map<String, String> unread;
    public Map<String, String> read_receipt;
    public Map<String, String> delivery_receipt;
    public BaseMessage last_message;

    public int getUnread(String userId) {
        return unread == null ? 0 : Util.getInt(unread.get(userId));
    }

    public String getReadReceipt() {
        return read_receipt == null ? "" : read_receipt.toString();
    }

    public String getDeliveryReceipt() {
        return delivery_receipt == null ? "" : delivery_receipt.toString();
    }

    public long getReadReceipt(String userId) {
        return read_receipt == null ? 0 : Util.getLong(read_receipt.get(userId));
    }

    public long getDeliveryReceipt(String userId) {
        return delivery_receipt == null ? 0 : Util.getLong(delivery_receipt.get(userId));
    }

    public long getTimestamp() {
        return last_message == null ? 1000000000000L : last_message.getTimestamp();
    }

} // GroupChannel.java