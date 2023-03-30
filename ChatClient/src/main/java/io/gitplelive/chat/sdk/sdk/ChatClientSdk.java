/**
 * ChatClientSdk.java
 *
 */

package io.gitplelive.chat.sdk.sdk;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.gitplelive.chat.sdk.api.ChatClient;
import io.gitplelive.chat.sdk.helper.HttpRequest;
import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.interfaces.ConnectionEvent;
import io.gitplelive.chat.sdk.interfaces.GroupChannelEvent;
import io.gitplelive.chat.sdk.interfaces.OnResponse;
import io.gitplelive.chat.sdk.interfaces.UserEvent;
import io.gitplelive.chat.sdk.model.BaseUser;
import io.gitplelive.chat.sdk.model.ChannelPage;
import io.gitplelive.chat.sdk.model.DeviceInfo;
import io.gitplelive.chat.sdk.model.ErrorType;
import io.gitplelive.chat.sdk.model.GroupChannel;
import io.gitplelive.chat.sdk.model.JWT;
import io.gitplelive.chat.sdk.model.MessagePayload;
import io.gitplelive.chat.sdk.model.ResponseError;
import io.gitplelive.chat.sdk.model.TokenInfo;


public class ChatClientSdk {

    private static final String VERSION = "v1.0.0";
    private static final String topicPrefix = "mqtt/topic/gitple/live";

    private final Context context;
    private String host;
    private String appId;
    private String userId;
    private MqttClient mqttClient;
    private String serverURI;
    private boolean connected = false;

    public UsersSdk usersSdk;
    public GroupChannelSdk groupChannelSdk;
    public GroupChannelMessageSdk groupChannelMessageSdk;

    private TokenInfo tokenInfo;

    private ConnectionEvent connectionEvent = new ConnectionEvent() {
        @Override public void onError(int errorType) {}
        @Override public void onConnected(String status) {}
        @Override public void onReconnected(String status) {}
        @Override public void onDisconnected(String status) {}
    };

    private UserEvent userEvent;
    private GroupChannelEvent groupChannelEvent;

    public void setConnectionEvent(ConnectionEvent connectionEvent) {
        this.connectionEvent = connectionEvent;
    }

    public void setUserEvent(UserEvent userEvent) {
        this.userEvent = userEvent;
    }

    public void setGroupChannelEvent(GroupChannelEvent groupChannelEvent) {
        this.groupChannelEvent = groupChannelEvent;
    }

    public boolean isConnected() {
        return mqttClient != null && connected;
    }

    public boolean isNotConnected() {
        if (mqttClient == null || !connected) {
            connectionEvent.onError(ErrorType.NOT_CONNECTED);
            return true;
        }
        return false;
    }

    public void logout() {
        usersSdk.deleteDeviceToken(mqttClient.getClientId(), (response, error) -> {
            Util.debug("[ChatClientSdk] logout", response, error);
            MessagingHelper.resetToken();
        });
    }

    public ChatClientSdk(Context context, String host, String appId) {
        assert !host.contains("/");

        this.context = context;
        this.host = host;
        this.appId = appId;
        this.serverURI = String.format("wss://%s/ws", host);

        context.registerReceiver(broadcastReceiver, new IntentFilter("ChatClient"));
    }

    public void reset(String host, String appId) {
        this.host = host;
        this.appId = appId;
        this.serverURI = String.format("wss://%s/ws", host);
    }

    public void connectUser(String userId, String token) {
        if (!Util.checkNetwork(context)) {
            connectionEvent.onError(ErrorType.UNABLE_CONNECT_ERROR);
            return;
        }
        if (mqttClient != null) {
            Util.error("[ChatClientSdk] connectUser", "Already connected");
            return;
        }
        this.userId = userId;
        usersSdk = new UsersSdk(context, host, appId, userId);

        JWT.Body body = new JWT(token).getBody();
        Util.debug(body.getUserId() + " " + body.isExpired());

        usersSdk.setToken(token);
        usersSdk.refreshToken((response, error) -> {
            if (response != null) {
                try {
                    tokenInfo = new Gson().fromJson(response, TokenInfo.class);
                    setToken(tokenInfo.token);
                    connect();
                } catch (Exception e) {
                    Util.error(e.toString());
                    connectionEvent.onError(ErrorType.UNKNOWN_ERROR);
                }
            }
            else {
                try {
                    connectionEvent.onError(ResponseError.fromJson(error).code);
                }
                catch (Exception e) {
                    Util.error(e.toString());
                    connectionEvent.onError(ErrorType.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void connectUser(String userId) {
        if (!Util.checkNetwork(context)) {
            connectionEvent.onError(ErrorType.UNABLE_CONNECT_ERROR);
            return;
        }
        if (mqttClient != null) {
            Util.error("[ChatClientSdk] connectUser", "Already connected");
            return;
        }
        this.userId = userId;
        usersSdk = new UsersSdk(context, host, appId, userId);

        connect();
    }

    public void disconnectUser() {
        if (isNotConnected()) return;
        try {
            Util.error("[ChatClientSdk] disconnectUser");
            mqttClient.disconnect();
            mqttClient = null;
            tokenInfo = null;
            connected = false;
        }
        catch (MqttException e) {
            Util.error("[ChatClientSdk] disconnectUser", e.toString());
            connectionEvent.onError(ErrorType.UNKNOWN_ERROR);
        }
    }

    private void connect() {
        getHealth((response, error) -> {
            if (error != null) {
                ResponseError responseError = ResponseError.fromJson(error);
                connectionEvent.onError(responseError.code);
                return;
            }
            if (response == null) {
                connectionEvent.onError(ErrorType.UNABLE_CONNECT_ERROR);
                return;
            }

            try {
                mqttClient = new MqttClient(serverURI, getClientId(response), null);
                mqttClient.setCallback(mqttCallbackExtended);

                connect2();
            }
            catch (MqttException e) {
                Util.error("[ChatClientSdk] connect", e.toString());
                connectionEvent.onError(ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String getClientId(String address) {
        long date = System.currentTimeMillis();
        String os = "android";
        String deviceId = new DeviceInfo().getId();
        String clientId = String.format("%s::%s::%s::%d::%s::%s::%s",
                appId, userId, VERSION, date, address, os, deviceId);
        Util.debug("[ChatClientSdk] clientId:", clientId);
        return clientId;
    }

    //-----------------------------------------------------------------------
    // 내부용: 1. 서버 상태 확인 및 Client IP 조회용
    //-----------------------------------------------------------------------
    private void getHealth(OnResponse listener) {
        String url = "https://" + host + "/health";

        new HttpRequest(context, url, null, (response, error) -> {
            listener.callback(HttpRequest.address, error);
        });
    }

    private void connect2() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        options.setAutomaticReconnect(true);
        options.setMaxReconnectDelay(5 * 1000);
        options.setConnectionTimeout(3 * 1000);
        options.setUserName(userId);
        if (tokenInfo != null) {
            options.setPassword(tokenInfo.token.toCharArray());
        }
        try {
            mqttClient.connect(options);
            Util.debug(">>> connect", mqttClient.getClientId());
        }
        catch (MqttException e) {
            Util.error("[ChatClientSdk] connect", e.toString());
            try {
                mqttClient.disconnect();
            }
            catch (MqttException e2) {
                Util.debug("[ChatClientSdk] connect", e2.toString());
            }
            mqttClient = null;
            tokenInfo = null;
            connected = false;
            connectionEvent.onError(ErrorType.INVALID_TOKEN);
        }
    }

    private void getToken() {
        usersSdk.generateTokenBySession(mqttClient.getClientId(), (response, error) -> {
            if (error != null) {
                ResponseError responseError = ResponseError.fromJson(error);
                connectionEvent.onError(responseError.code);
                return;
            }
            try {
                tokenInfo = new Gson().fromJson(response, TokenInfo.class);
                setToken(tokenInfo.token);
                onConnect();
            }
            catch (Exception e) {
                Util.error("[ChatClient] getToken", e.toString());
                connectionEvent.onError(ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    private void setToken(String token) {
        usersSdk.setToken(token);
        groupChannelSdk = new GroupChannelSdk(context, host, appId, userId, token);
        groupChannelMessageSdk = new GroupChannelMessageSdk(context, host, appId, userId, token);
    }

    private void onConnect() {
        ChatClient.user().me((user, errorType) -> {
            if (errorType > 0) {
                Util.error(ErrorType.message(errorType));
                connectionEvent.onConnected("failed");
            }
            else {
                String pushToken = MessagingHelper.getToken();
                if (pushToken != null) {
                    registerDeviceToken(pushToken);
                }
                else findAllJoined();
            }
        });
    }

    private void registerDeviceToken(String pushToken) {
        usersSdk.registerDeviceToken(mqttClient.getClientId(), pushToken, (response, error) -> {
            if (error != null) {
                Util.error(error);
                connectionEvent.onConnected("failed");
            }
            else findAllJoined();
        });
    }

    private void findAllJoined() {
        groupChannelSdk.findAllJoined((response, error) -> {
            if (error != null) {
                Util.error(error);
                connectionEvent.onConnected("failed");
                return;
            }
            try {
                ChannelPage channelPage = new Gson().fromJson(response, ChannelPage.class);
                subscribe(channelPage.channels);
            }
            catch (Exception e) {
                Util.error(e.toString());
                connectionEvent.onConnected("failed");
            }
        });
    }

    private void subscribe(GroupChannel[] channels) {
        List<String> list = new ArrayList<>();

        // 사용자 이벤트 수신용 토픽
        list.add(String.format("%s/%s/user/all/%s/#", topicPrefix, appId, userId));

        for (GroupChannel channel : channels) {
            String topic = topicPrefix + "/" + appId + "/channel/" + channel.type + "/" + channel.channel_id;

            // 참가자 권한 채널 이벤트 수신용 토픽
            list.add(topic + "/all/#");

            if (Arrays.stream(channel.managers).anyMatch(x -> x.getUserId().equals(userId))) {
                // 매니저 권한 채널 이벤트 수신용 토픽
                list.add(topic + "/manager/#");
            }
        }

        // 토픽 목록 생성
        String[] topicFilter = list.toArray(new String[0]);
        try {
            Util.debug("[ChatClientSdk] subscribe " + topicFilter.length + " topics");

            // 토픽 목록 구독
            mqttClient.subscribe(topicFilter);

            // 정상 접속
            connectionEvent.onConnected("success");
        }
        catch (MqttException e) {
            Util.error("[ChatClientSdk] subscribe", e.toString());

            connectionEvent.onError(ErrorType.UNABLE_SUBSCRIBE_ERROR);
        }
    }

    MqttCallbackExtended mqttCallbackExtended = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Util.debug(">>> connectComplete" + (reconnect ? " reconnect:" : ":"), serverURI);
            connected = true;

            if (reconnect) {
                connectionEvent.onReconnected("success");
            }
            else if (tokenInfo != null) {
                onConnect();
            }
            else getToken();
        }

        @Override
        public void connectionLost(Throwable cause) {
            Util.error(">>> connectionLost:", cause.getLocalizedMessage());
            connected = false;
            connectionEvent.onDisconnected(cause.getLocalizedMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Util.debug(">>> messageArrived:", topic);

            String json = new String(message.getPayload());
            try {
                Util.debug(Util.toJson(new Gson().fromJson(json, MessagePayload.class)));
            }
            catch (Exception e) { Util.error(e.toString()); }

            context.sendBroadcast(new Intent("ChatClient").putExtra("payload", json));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Util.error(">>> deliveryComplete:", Arrays.toString(token.getTopics()));
        }
    };

    public void updateTopic(GroupChannel channel, String action, String user) {
        String topic = topicPrefix + "/" + appId + "/channel/" + channel.type + "/" + channel.channel_id;
        topic += String.format("/%s/#", user);
        String[] topicFilter = topic.split("\n");
        Util.debug("[ChatClientSdk]", action, Arrays.toString(topicFilter));
        try {
            if (action.equals("subscribe"))
                mqttClient.subscribe(topicFilter);
            else
                mqttClient.unsubscribe(topicFilter);
        }
        catch (Exception e) {
            Util.error("[ChatClientSdk]", action, e.toString());
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra("payload");
            MessagePayload payload;
            try {
                payload = new Gson().fromJson(json, MessagePayload.class);
            }
            catch (Exception e) {
                Util.error(">>> payload:", e.toString());
                return;
            }
            if (payload == null || payload.category == null) return;

            switch (payload.category) {
                case "user_update":
                    if (userEvent != null)
                        userEvent.onUpdate(payload.user);
                    break;
                case "user_delete":
                    if (userEvent != null)
                        userEvent.onDelete(payload.user);
                    if (ChatClient.user().isMe(payload.user)) {
                        disconnectUser();
                    }
                    break;
                case "user_joined_channel":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "subscribe", "all");
                    }
                    if (userEvent != null)
                        userEvent.onJoined(payload.channel, payload.user);
                    break;
                case "user_become_manager":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "subscribe", "all");
                        updateTopic(payload.channel, "subscribe", "manager");
                    }
                    if (userEvent != null)
                        userEvent.onManager(payload.channel, payload.user);
                    break;
                case "group:channel_update":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onUpdated(payload.channel);
                    break;
                case "group:channel_delete":
                    updateTopic(payload.channel, "unsubscribe", "all");
                    updateTopic(payload.channel, "unsubscribe", "manager");
                    if (groupChannelEvent != null)
                        groupChannelEvent.onDeleted(payload.channel);
                    break;
                case "group:channel_join":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "subscribe", "all");
                    }
                    if (groupChannelEvent != null)
                        groupChannelEvent.onJoined(payload.channel, payload.user);
                    break;
                case "group:channel_leave":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "unsubscribe", "all");
                        updateTopic(payload.channel, "unsubscribe", "manager");
                    }
                    if (groupChannelEvent != null)
                        groupChannelEvent.onLeft(payload.channel, payload.user);
                    break;
                case "group:channel_manager_create":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "subscribe", "all");
                        updateTopic(payload.channel, "subscribe", "manager");
                    }
                    if (groupChannelEvent != null)
                        groupChannelEvent.onManagerCreated(payload.channel, payload.user);
                    break;
                case "group:channel_manager_delete":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "unsubscribe", "manager");
                    }
                    if (groupChannelEvent != null)
                        groupChannelEvent.onManagerDeleted(payload.channel, payload.user);
                    break;
                case "group:channel_freeze":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onFrozen(payload.channel);
                    break;
                case "group:channel_unfreeze":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onUnfrozen(payload.channel);
                    break;
                case "group:channel_ban":
                    if (ChatClient.user().isMe(payload.user)) {
                        updateTopic(payload.channel, "unsubscribe", "all");
                        updateTopic(payload.channel, "unsubscribe", "manager");
                    }
                    if (groupChannelEvent != null)
                        groupChannelEvent.onUserBanned(payload.channel, payload.user, payload.banInfo);
                    break;
                case "group:channel_unban":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onUserUnbanned(payload.channel, payload.user);
                    break;
                case "group:message_send":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onMessageCreated(payload.channel, payload.message);
                    break;
                case "group:message_update":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onMessageUpdated(payload.channel, payload.message);
                    break;
                case "group:message_delete":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onMessageDeleted(payload.channel, payload.message);
                    break;
                case "group:channel_message_read_event":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onMessageRead(payload.channel);
                    break;
                case "group:channel_message_delivered_event":
                    if (groupChannelEvent != null)
                        groupChannelEvent.onMessageDelivered(payload.channel);
                    break;
            }
        }
    };

} // ChatClientSdk.java