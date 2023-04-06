# gitplelive-chat-sdk-android

### GitpleLive Android SDK 1.0.1

[![](https://jitpack.io/v/finset-io/gitplelive-chat-sdk-android.svg)](https://jitpack.io/#finset-io/gitplelive-chat-sdk-android)

## 라이브러리 설치 및 사용 방법

- build.gradle(Module:app) 파일에 마지막 라인 추가

        dependencies {
            ...  
            implementation 'com.github.finset-io:gitplelive-chat-sdk-android:1.0.1'
        }

- settings.gradle 파일에 마지막 라인 추가

        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                google()
                mavenCentral()
                maven { url "https://jitpack.io" }
            }
        }


## ChatClient SDK

- Singleton Access object: ChatClient.getInstance()

        ChatClient.getInstance().init(...);

### 초기화: 1-1. init
    public static void init(Context context, String host, String appId)
 - MainActivity 에서 SDK 초기화를 진행합니다.

### 초기화: 1-2. reset
    public static void reset(String host, String appId)
- 앱 실행 중에 host 와 appId 변경할 필요가 있을 경우 사용합니다.

### 연결: 2-1. connectUser
    public void connectUser(String userId, String token)
- 안전한 접속을 위해 사용자 ID와 사용자 세션 토큰을 추가하여 접속할 수 있습니다.

### 연결: 2-2. connectUser
    public void connectUser(String userId)
- 사용자 ID를 사용하여 접속할 수 있습니다. 없는 사용자라면 자동으로 생성됩니다.

### 연결해제: 3. disconnectUser
    public void disconnectUser()
- 앱 종료 시 사용합니다. 연결해제 시 신규 메시지는 푸시알림을 통해 수신됩니다.

### 연결조회: 4. isConnected
    public boolean isConnected()
- 연결 상태를 수시로 확인할 수 있습니다.

### 로그아웃: 5. logout (푸시 알림 해제)
    public void logout()
- 앱 종료 및 연결해제 전 로그아웃을 실행하면 푸시알림을 받지 않을 수 있습니다.

### 연결 이벤트 수신 설정: 6. setConnectionEvent
    public void setConnectionEvent(ConnectionEvent connectionEvent) 
- 커넥션 이벤트 인터페이스를 연결합니다.

### 사용자 이벤트 수신 설정: 7. setUserEvent
    public void setUserEvent(UserEvent userEvent)
- 사용자 이벤트 인터페이스를 연결합니다.

### 그룹 채널 이벤트 수신 설정: 8. setGroupChannelEvent
    public void setGroupChannelEvent(GroupChannelEvent groupChannelEvent)
- 그룹 채널 이벤트 인터페이스를 연결합니다.



## 사용자 SDK

- Singleton Access object: ChatClient.user()

        ChatClient.user().me((user, errorType) -> { ... });

### 조회: 1. me
    public void me(OnCallback<BaseUser> listener)
- 사용자 자신의 정보를 조회할 수 있습니다.

### 수정: 2. updateUser
    public void updateUser(String name, String profile, OnCallback<BaseUser> listener)
- 사용자 자신의 정보를 수정할 수 있습니다.

### 메타 데이터 수정: 3. updateMeta
    public void updateMeta(Map<String, String> meta, OnCallback<BaseUser> listener)
- 사용자 자신의 메타 데이터를 수정할 수 있습니다.

### 메타 데이터 삭제: 4. deleteMeta
    public void deleteMeta(String[] keys, OnCallback<BaseUser> listener)
- 사용자 자신의 메타 데이터를 삭제할 수 있습니다.


## 그룹 채널  SDK

- Singleton Access object: ChatClient.groupChannel()

        ChatClient.groupChannel().getChannelList((page, errorType) -> { ... });

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
- 그룹 채널 목록을 페이징 조회 할 수 있습니다. 조회 정렬 기준은 생성일 내림차순입니다. 다음 페이지가 있을 경우 응답 데이터에 'next' 정보가 전달됩니다. 응답에서 받은 'next' 값을 사용하여 다음 목록을 조회 할 수 있습니다. 다음 페이지 조회 시, 'next' 값만 전달하면 됩니다.

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
- 사용자가 참가한 그룹 채널 목록을 페이징 조회 할 수 있습니다. 조회 정렬 기준은 생성일 내림차순입니다. 다음 페이지가 있을 경우 응답 데이터에 'next' 정보가 전달됩니다. 응답에서 받은 'next' 값을 사용하여 다음 목록을 조회 할 수 있습니다. 다음 페이지 조회 시, 'next' 값만 전달하면 됩니다.

### 정보: 3. getChannel
    public void getChannel(String channelId, OnCallback<GroupChannel> listener)
- 사용자가 참가한 채널을 조회할 수 있습니다.

### 생성: 4-1. createChannel
    public void createChannel(  String channelId,
                                String name,
                                String profile,
                                String[] members,
                                boolean reuse,
                                Map<String, String> meta,
                                OnCallback<GroupChannel> listener)
- 1:1 채널을 만들 경우, reuse: true로 설정하여 요청을 보내면 기존에 동일한 참가자로 구성된 채널이 있다면 새로운 채널을 생성하지 않고 기존 채널 정보를 응답하기 때문에 대화를 이어갈 수 있습니다. 기존 채널이 없다면 새 채널이 생성됩니다.
- 기존 채널을 판단하는 기준은 채널 참가자가 동일하고, 해당 참가자들 외에 다른 참가자가 참가한 이력이 없으며, 매니저 등록 이력이 없는 경우입니다.
- 생성한 채널에는 자동 참가됩니다.

### 생성: 4-2. createChannel (아이디 자동 생성)
    public void createChannel(  String name,
                                String profile,
                                String[] members,
                                boolean reuse,
                                Map<String, String> meta,
                                OnCallback<GroupChannel> listener)
- 채널 생성 시 유효한 채널 아이디가 자동 생성됩니다.

### 수정: 5. updateChannel
    public void updateChannel(String channelId,
                              String name,
                              String profile,
                              OnCallback<GroupChannel> listener)
- 채널 매니저가 채널 정보(이름, 프로필)를 수정할 수 있습니다.

### 삭제: 6. deleteChannel
    public void deleteChannel(String channelId, OnCallback<Boolean> listener)
- 채널 매니저가 채널 채널을 삭제할 수 있습니다.

### 참가자 목록: 7. getMemberList
    public void getMemberList(String channelId, OnCallback<BaseUser[]> listener)
- 그룹 채널 참가자 목록 조회

### 채널 입장: 8. joinChannel
    public void joinChannel(String channelId, OnCallback<Boolean> listener)
- 그룹 채널 참가

### 채널 퇴장: 9. leaveChannel
    public void leaveChannel(String channelId, OnCallback<Boolean> listener)
- 그룹 채널 나가기

### 매니저 목록: 10. getManagerList
    public void getManagerList(String channelId, OnCallback<BaseUser[]> listener)
- 채널 매니저는 채널의 매니저들을 조회할 수 있습니다.

### 매니저 등재: 11. registerManager
    public void registerManager(String channelId, String userId, OnCallback<Boolean> listener)
- 그룹 채널 매니저 등록

### 매니저 삭제: 12. deleteManager
    public void deleteManager(String channelId, String userId, OnCallback<Boolean> listener)
- 그룹 채널 매니저 삭제

### 메시지 읽음 확인 처리(특정 채널): 13. readMessage
    public void readMessage(String channelId, OnCallback<Boolean> listener)
- 사용자가 그룹 채널에서 받은 메시지를 읽었다고 표시 할 수 있습니다.
- 읽음 처리가 되면 해당 채널 읽기 영수증의 사용자 시간 정보가 갱신됩니다.
- 주의: 사용자가 참가한 채널만 읽음 처리가 가능합니다.

### 메시지 읽음 확인 처리(다수 채널 일괄 처리): 14. readMessage
    public void readMessage(String[] channelIds, OnCallback<Boolean> listener)
- 사용자가 그룹 채널에서 온 메시지를 수신하였다고 표시 할 수 있습니다.
- 배달 확인 처리가 되면 해당 채널 배달 영수증의 사용자 시간 정보가 갱신됩니다.

### 메시지 수식 확인 처리: 15. deliveredMessage
    public void deliveredMessage(String channelId, OnCallback<Boolean> listener)

### 메타 데이터 수정: 16. updateMeta
    public void updateMeta(String channelId, Map<String, String> meta, OnCallback<GroupChannel> listener)
- 채널 매니저는 메타 데이터를 수정할 수 있습니다.

### 메타 데이터 삭제: 17. deleteMeta
    public void deleteMeta(String channelId, String[] keys, OnCallback<GroupChannel> listener)
- 채널 매니저는 메타 데이터를 삭제할 수 있습니다.

### 활성 조회: 18. getOnlineMemberList
    public void getOnlineMemberList(String channelId, OnCallback<String[]> listener)
- 현재 접속 중인 채널 참가자를 조회할 수 있습니다.

### 중재(채널 동결 / 해제): 19. freezeChannel
    public void freezeChannel(String channelId, boolean freeze, OnCallback<GroupChannel> listener)
- 채널 매니저는 채널을 동결 및 해제할 수 있습니다.

### 중재(금지): 20. ban
    public void ban(String channelId, String userId, int seconds, String reason, OnCallback<BanInfo> listener)
- 채널 매니저는 특정 사용자를 해당 그룹 채널에 참가하지 못 하도록 금지 할 수 있습니다.

### 중재(금지 해제): 21. unban
    public void unban(String channelId, String userId, OnCallback<Boolean> listener)
- 채널 매니저는 사용자 금지를 해제 할 수 있습니다.

### 중재(금지 목록 조회): 22. getBannedList
    public void getBannedList(String channelId, OnCallback<BanInfo[]> listener)
- 해당 채널의 사용자 금지 목록을 조회 할 수 있습니다. 조회 정렬 기준은 금지 시작일 내림차순입니다.


## 그룹 채널 메시지 SDK

- Singleton Access object: ChatClient.groupChannel()

        ChatClient.groupChannel().getMessageList(..., (message, errorType) -> { ... });

### 메시지 목록 조회: 1. getMessageList
    public void getMessageList(String channelId,
                               int limit,
                               String mode,
                               String type,
                               String content,
                               long messageId,
                               OnCallback<BaseMessage[]> listener)
- 그룹 채널의 메시지 목록을 페이징 조회 할 수 있습니다.
- 조회 정렬 기준은 생성일 내림차순입니다. 'base_message_id'를 지정하여 다음 목록을 조회 할 수 있습니다.

### 생성: 2-1. sendMessage (텍스트)
    public void sendMessage(String channelId, String text, Map<String, String> meta, OnCallback<BaseMessage> listener)
- 그룹 채널에 메시지를 전송할 수 있습니다.
- 전송 시점에 사용자가 대화방을 보고 있다고 가정하므로 해당 사용자의 읽기 영수증과 배달 영수증을 자동 갱신합니다.

### 생성: 2-2. sendMessage (파일)
    public void sendMessage(String channelId, File file, Map<String, String> meta, OnCallback<BaseMessage> listener)
- 그룹 채널에 파일를 전송할 수 있습니다.
- 전송 시점에 사용자가 대화방을 보고 있다고 가정하므로 해당 사용자의 읽기 영수증과 배달 영수증을 자동 갱신합니다.

### 삭제: 3. deleteMessage
    public void deleteMessage(String channelId, long messageId, OnCallback<Boolean> listener)
- 사용자 자신이 전송한 메시지 및 파일을 삭제할 수 있습니다.

### 메타 데이터 수정: 4. updateMessageMeta
    public void updateMessageMeta(String channelId, long messageId, Map<String, String> meta, OnCallback<BaseMessage> listener)
- 사용자 자신이 전송한 메시지의 메타 데이터를 수정할 수 있습니다.
- 
### 메타 데이터 삭제: 5. deleteMessageMeta
    public void deleteMessageMeta(String channelId, long messageId, String[] keys, OnCallback<BaseMessage> listener)
- 사용자 자신이 전송한 메시지의 메타 데이터를 삭제할 수 있습니다.


## 연결 이벤트 인터페이스

    public interface ConnectionEvent {
          // 에러
          void onError(int errorType);

          // 연결
          void onConnected(String status);

          // 재연결
          void onReconnected(String status);

          // 연결 해제
          void onDisconnected(String status);
    }

## 사용자 이벤트 인터페이스

    public interface UserEvent {
        // 사용자 정보가 업데이트 된 경우 수신
        void onUpdate(BaseUser user);

        // 사용자가 삭제된 경우 수신
        void onDelete(BaseUser user);

        // 사용자가 채널에 참여했을 경우 수신
        void onJoined(GroupChannel channel, BaseUser user);

        // 사용자가 채널의 매지저가 되었을 경우 수신
        void onManager(GroupChannel channel, BaseUser user);
    }

## 그룹 채널 이벤트 인터페이스

    public interface GroupChannelEvent {
        // 채널 정보가 수정 된 경우 수신
        void onUpdated(GroupChannel channel);

        // 채널이 삭제된 경우 수신
        void onDeleted(GroupChannel channel);

        // 채널에 새로운 멤버가 참가한 경우 수신
        void onJoined(GroupChannel channel, BaseUser user);

        // 채널에 멤버가 나간 경우 수신
        void onLeft(GroupChannel channel, BaseUser user);

        // 채널에 매니저가 등록된 경우 수신
        void onManagerCreated(GroupChannel channel, BaseUser user);

        // 채널에 매니저가 삭제된 경우 수신
        void onManagerDeleted(GroupChannel channel, BaseUser user);

        // 채널이 동결된 경우 수신
        void onFrozen(GroupChannel channel);

        // 채널이 동결 해제된 경우 수신
        void onUnfrozen(GroupChannel channel);

        // 채널에 사용자가 금지된 경우 수신
        void onUserBanned(GroupChannel channel, BaseUser user, BanInfo banInfo);

        // 채널에 사용자가 금지 해제된 경우 수신
        void onUserUnbanned(GroupChannel channel, BaseUser user);

        // 채널에 새로운 메시지가 전송된 경우 수신
        void onMessageCreated(GroupChannel channel, BaseMessage message);

        // 채널의 메시지가 수정된 경우 수신
        void onMessageUpdated(GroupChannel channel, BaseMessage message);

        // 채널의 메시지가 삭제된 경우 수신
        void onMessageDeleted(GroupChannel channel, BaseMessage message);

        // 채널의 읽기 영수증이 갱신된 경우 수신
        void onMessageRead(GroupChannel channel);

        // 채널의 배달 영수증이 갱신된 경우 수신
        void onMessageDelivered(GroupChannel channel);
    }

## 에러 타입 클래스

    public class ErrorType {
        // Check the sdk initialization init parameters.
        public static int INVALID_PARAMETERS = 60101;

        // invalid session token.
        public static int INVALID_TOKEN = 60102;

        // Generate token again.
        public static int EXPIRED_TOKEN = 60103;

        // Invalid channel ID.
        public static int INVALID_CHANNEL_ID = 60104;

        // The server is not responding.
        public static int SERVER_NOT_RESPONDING = 60901;

        // Unable to connect to the server.
        public static int UNABLE_CONNECT_ERROR = 60902;

        // Unable to subscribe to the event.
        public static int UNABLE_SUBSCRIBE_ERROR = 60903;

        // The device is not connected to the server.
        public static int NOT_CONNECTED = 60904;

        // Check the message on the console.
        public static int UNKNOWN_ERROR = 60999;

        public static String message(int errorType);
    }
