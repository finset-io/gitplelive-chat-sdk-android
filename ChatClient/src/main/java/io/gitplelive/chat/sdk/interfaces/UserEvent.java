/**
 * UserEvent.java
 *
 */

package io.gitplelive.chat.sdk.interfaces;


import io.gitplelive.chat.sdk.model.BaseUser;
import io.gitplelive.chat.sdk.model.GroupChannel;


public interface UserEvent {

    void onUpdate(BaseUser user);
    void onDelete(BaseUser user);
    void onJoined(GroupChannel channel, BaseUser user);
    void onManager(GroupChannel channel, BaseUser user);

} // UserEvent.java