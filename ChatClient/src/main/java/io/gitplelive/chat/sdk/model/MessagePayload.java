/**
 * MessagePayload.java
 *
 */

package io.gitplelive.chat.sdk.model;


public class MessagePayload {

    public String category;
    public String app_id;
    public BaseUser user;
    public GroupChannel channel;
    public BaseMessage message;
    public BanInfo banInfo;

} // MessagePayload.java