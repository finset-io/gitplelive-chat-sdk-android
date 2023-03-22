/**
 * GroupChannelEvent.java
 *
 */

package io.gitplelive.chat.sdk.interfaces;


import io.gitplelive.chat.sdk.model.BanInfo;
import io.gitplelive.chat.sdk.model.BaseMessage;
import io.gitplelive.chat.sdk.model.BaseUser;
import io.gitplelive.chat.sdk.model.GroupChannel;


public interface GroupChannelEvent {

    void onUpdated(GroupChannel channel);
    void onDeleted(GroupChannel channel);
    void onJoined(GroupChannel channel, BaseUser user);
    void onLeft(GroupChannel channel, BaseUser user);
    void onManagerCreated(GroupChannel channel, BaseUser user);
    void onManagerDeleted(GroupChannel channel, BaseUser user);
    void onFrozen(GroupChannel channel);
    void onUnfrozen(GroupChannel channel);
    void onUserBanned(GroupChannel channel, BaseUser user, BanInfo banInfo);
    void onUserUnbanned(GroupChannel channel, BaseUser user);
    void onMessageCreated(GroupChannel channel, BaseMessage message);
    void onMessageUpdated(GroupChannel channel, BaseMessage message);
    void onMessageDeleted(GroupChannel channel, BaseMessage message);
    void onMessageRead(GroupChannel channel);
    void onMessageDelivered(GroupChannel channel);

} // GroupChannelEvent.java