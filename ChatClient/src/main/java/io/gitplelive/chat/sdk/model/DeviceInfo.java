/**
 * DeviceInfo.java
 *
 */

package io.gitplelive.chat.sdk.model;


import java.util.UUID;

import io.gitplelive.chat.sdk.helper.Util;


public class DeviceInfo {

    private String deviceId;
    private String pushToken;

    public DeviceInfo() {
        deviceId = Util.load("device_id");
        if (deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString();
            Util.save("device_id", deviceId);
        }
        pushToken = Util.load("push_token");
    }

    public String getId() {
        return deviceId;
    }

    public String getPushToken(String pushToken) {
        assert pushToken != null;
        if (pushToken.equals(this.pushToken)) return null;
        this.pushToken = pushToken;
        Util.save("push_token", pushToken);
        return pushToken;
    }

    public void resetPushToken() {
        Util.save("push_token", null);
        pushToken = null;
    }

} // DeviceInfo.java