/**
 * Callback.java
 *
 */

package io.gitplelive.chat.sdk.interfaces;


public interface OnCallback<T> {

    void callback(T t, int errorType);

} // Callback.java