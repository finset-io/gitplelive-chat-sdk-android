# gitplelive-chat-sdk-android

### GitpleLive Android SDK 1.0.1

[![](https://jitpack.io/v/finset-io/gitplelive-chat-sdk-android.svg)](https://jitpack.io/#finset-io/gitplelive-chat-sdk-android)

## ChatClient SDK

- Sigleton Object: ChatClient.getInstance()

### 초기화: 1-1. init
    public static void init(Context context, String host, String appId)

### 초기화: 1-2. reset
    public static void reset(String host, String appId)

### 연결: 2-1. connectUser
    public void connectUser(String userId, String token)

### 연결: 2-2. connectUser
    public void connectUser(String userId)

### 연결해제: 3. disconnectUser
    public void disconnectUser()

### 연결조회: 4. isConnected
    public boolean isConnected()

### 로그아웃: 5. logout (푸시 알림 해제)
    public void logout()

### 연결 이벤트 수신 설정: 6. setConnectionEvent
    public void setConnectionEvent(ConnectionEvent connectionEvent) 

### 사용자 이벤트 수신 설정: 7. setUserEvent
    public void setUserEvent(UserEvent userEvent)

### 그룹 채널 이벤트 수신 설정: 8. setGroupChannelEvent
    public void setGroupChannelEvent(GroupChannelEvent groupChannelEvent)



## 사용자 SDK

- Signleton Access object: ChatClient.user()

### 조회: 1. me
    public void me(OnCallback<BaseUser> listener)

### 수정: 2. updateUser
    public void updateUser(String name, String profile, OnCallback<BaseUser> listener)

### 메타 데이터 수정: 3. updateMeta
    public void updateMeta(Map<String, String> meta, OnCallback<BaseUser> listener)

### 메타 데이터 삭제: 4. deleteMeta
    public void deleteMeta(String[] keys, OnCallback<BaseUser> listener)



## 그룹 채널  SDK

- Signleton Access object: ChatClient.groupChannel()

### 전체 목록: 1-1. getChannelList
    public void getChannelList(OnCallback<ChannelPage> listener)

### 전체 목록: 1-2. getChannelList (filtered)
    public void getChannelList( int limit,
                                boolean showMembers,
                                boolean showManagers,
                                boolean showReadReceipt,
                                boolean showDeliveryReceipt,
                                boolean showUnread,
                                boolean showLastMessage,
                                String name,
                                String include_members,
                                String next,
                                OnCallback<ChannelPage> listener)

### 참가한 목록: 2. getJoinedChannelList
    public void getJoinedChannelList(   int limit,
                                        boolean showMembers,
                                        boolean showManagers,
                                        boolean showReadReceipt,
                                        boolean showDeliveryReceipt,
                                        boolean showUnread,
                                        boolean showLastMessage,
                                        String name,
                                        String include_members,
                                        String next,
                                        OnCallback<ChannelPage> listener)

### 정보: 3. getChannel
    public void getChannel(String channelId, OnCallback<GroupChannel> listener)

### 생성: 4-1. createChannel
    public void createChannel(  String channelId,
                                String name,
                                String profile,
                                String[] members,
                                boolean reuse,
                                Map<String, String> meta,
                                OnCallback<GroupChannel> listener)

### 생성: 4-2. createChannel (아이디 자동 생성)
    public void createChannel(  String name,
                                String profile,
                                String[] members,
                                boolean reuse,
                                Map<String, String> meta,
                                OnCallback<GroupChannel> listener)

### 수정: 5. updateChannel
    public void updateChannel(String channelId,
                              String name,
                              String profile,
                              OnCallback<GroupChannel> listener)

### 삭제: 6. deleteChannel
    public void deleteChannel(String channelId, OnCallback<Boolean> listener)

### 참가자 목록: 7. getMemberList
    public void getMemberList(String channelId, OnCallback<BaseUser[]> listener)

### 채널 입장: 8. joinChannel
    public void joinChannel(String channelId, OnCallback<Boolean> listener)

### 채널 퇴장: 9. leaveChannel
    public void leaveChannel(String channelId, OnCallback<Boolean> listener)

### 매니저 목록: 10. getManagerList
    public void getManagerList(String channelId, OnCallback<BaseUser[]> listener)

### 매니저 등재: 11. registerManager
    public void registerManager(String channelId, String userId, OnCallback<Boolean> listener)

### 매니저 삭제: 12. deleteManager
    public void deleteManager(String channelId, String userId, OnCallback<Boolean> listener)

### 메시지 읽음 확인 처리(특정 채널): 13. readMessage
    public void readMessage(String channelId, OnCallback<Boolean> listener)

### 메시지 읽음 확인 처리(다수 채널 일괄 처리): 14. readMessage
    public void readMessage(String[] channelIds, OnCallback<Boolean> listener)

### 메시지 수식 확인 처리: 15. deliveredMessage
    public void deliveredMessage(String channelId, OnCallback<Boolean> listener)

### 메타 데이터 수정: 16. updateMeta
    public void updateMeta(String channelId, Map<String, String> meta, OnCallback<GroupChannel> listener)

### 메타 데이터 삭제: 17. deleteMeta
    public void deleteMeta(String channelId, String[] keys, OnCallback<GroupChannel> listener)

### 활성 조회: 18. getOnlineMemberList
    public void getOnlineMemberList(String channelId, OnCallback<String[]> listener)

### 중재(채널 동결 / 해제): 19. freezeChannel
    public void freezeChannel(String channelId, boolean freeze, OnCallback<GroupChannel> listener)

### 중재(금지): 20. ban
    public void ban(String channelId, String userId, int seconds, String reason, OnCallback<BanInfo> listener)

### 중재(금지 해제): 21. unban
    public void unban(String channelId, String userId, OnCallback<Boolean> listener)

### 중재(금지 목록 조회): 22. getBannedList
    public void getBannedList(String channelId, OnCallback<BanInfo[]> listener)



## 그룹 채널 메시지 SDK

- Signleton Access object: ChatClient.groupChannel()

### 메시지 목록 조회: 1. getMessageList
    public void getMessageList(String channelId,
                               int limit,
                               String mode,
                               String type,
                               String content,
                               long messageId,
                               OnCallback<BaseMessage[]> listener)

### 생성: 2-1. sendMessage (텍스트)
    public void sendMessage(String channelId, String text, Map<String, String> meta, OnCallback<BaseMessage> listener)

### 생성: 2-2. sendMessage (파일)
    public void sendMessage(String channelId, File file, Map<String, String> meta, OnCallback<BaseMessage> listener)

### 삭제: 3. deleteMessage
    public void deleteMessage(String channelId, long messageId, OnCallback<Boolean> listener)

### 메타 데이터 수정: 4. updateMessageMeta
    public void updateMessageMeta(String channelId, long messageId, Map<String, String> meta, OnCallback<BaseMessage> listener)

### 메타 데이터 삭제: 5. deleteMessageMeta
    public void deleteMessageMeta(String channelId, long messageId, String[] keys, OnCallback<BaseMessage> listener)


## 연결 이벤트 인터페이스

    void onError(int errorType);
    void onConnected(String status);
    void onReconnected(String status);
    void onDisconnected(String status);


## 사용자 이벤트 인터페이스

    void onUpdate(BaseUser user);
    void onDelete(BaseUser user);
    void onJoined(GroupChannel channel, BaseUser user);
    void onManager(GroupChannel channel, BaseUser user);


## 그룹 채널 이벤트 인터페이스

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


## 에러 타입 클래스

    public static int INVALID_PARAMETERS = 60101;
    public static int INVALID_TOKEN = 60102;
    public static int EXPIRED_TOKEN = 60103;
    public static int INVALID_CHANNEL_ID = 60104;
    public static int SERVER_NOT_RESPONDING = 60901;
    public static int UNABLE_CONNECT_ERROR = 60902;
    public static int UNABLE_SUBSCRIBE_ERROR = 60903;
    public static int NOT_CONNECTED = 60904;
    public static int UNKNOWN_ERROR = 60999;
