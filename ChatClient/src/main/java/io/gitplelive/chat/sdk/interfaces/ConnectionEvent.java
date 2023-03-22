/**
 * ConnectionEvent.java
 *
 */

package io.gitplelive.chat.sdk.interfaces;


public interface ConnectionEvent {

    void onError(int errorType);
    void onConnected(String status);
    void onReconnected(String status);
    void onDisconnected(String status);

} // ConnectionEvent.java