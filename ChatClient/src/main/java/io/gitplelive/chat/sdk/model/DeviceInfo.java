/**
 * DeviceInfo.java
 *
 */

package io.gitplelive.chat.sdk.model;


import java.util.UUID;

import io.gitplelive.chat.sdk.helper.Util;


public class DeviceInfo {

    private String deviceId;

    public DeviceInfo() {
        deviceId = Util.load("device_id");
        if (deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString();
            Util.save("device_id", deviceId);
        }
    }

    public String getId() {
        return deviceId;
    }

} // DeviceInfo.java