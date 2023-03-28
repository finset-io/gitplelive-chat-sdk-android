/**
 * GroupChannelApi.java
 *
 */

package io.gitplelive.chat.sdk.api;


import com.google.gson.Gson;

import java.io.File;
import java.util.Map;
import java.util.Random;

import io.gitplelive.chat.sdk.helper.UploadRequest;
import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.interfaces.OnCallback;
import io.gitplelive.chat.sdk.model.BanInfo;
import io.gitplelive.chat.sdk.model.BaseMessage;
import io.gitplelive.chat.sdk.model.BaseUser;
import io.gitplelive.chat.sdk.model.ChannelPage;
import io.gitplelive.chat.sdk.model.ErrorType;
import io.gitplelive.chat.sdk.model.GroupChannel;
import io.gitplelive.chat.sdk.model.MessagePage;
import io.gitplelive.chat.sdk.model.ResponseError;


public class GroupChannelApi {

    //-----------------------------------------------------------------------
    // 그룹 채널 전체 목록: 1-1. getChannelList
    //-----------------------------------------------------------------------
    public void getChannelList(OnCallback<ChannelPage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findAll((response, error) -> {
            if (listener == null) return;

            if (error != null) {
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                ChannelPage channels = new Gson().fromJson(response, ChannelPage.class);
                listener.callback(channels, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 전체 목록: 1-2. getChannelList (filtered)
    //-----------------------------------------------------------------------
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
                                OnCallback<ChannelPage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findAll(   next,
                                                            limit,
                                                            showMembers,
                                                            showManagers,
                                                            showReadReceipt,
                                                            showDeliveryReceipt,
                                                            showUnread,
                                                            showLastMessage,
                                                            name,
                                                            include_members,
                                                            (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                ChannelPage channels = new Gson().fromJson(response, ChannelPage.class);
                listener.callback(channels, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 참가한 목록: 2. getJoinedChannelList
    //-----------------------------------------------------------------------
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
                                        OnCallback<ChannelPage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findAllJoined( next,
                                                                limit,
                                                                showMembers,
                                                                showManagers,
                                                                showReadReceipt,
                                                                showDeliveryReceipt,
                                                                showUnread,
                                                                showLastMessage,
                                                                name,
                                                                include_members,
                                                                (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                ChannelPage channels = new Gson().fromJson(response, ChannelPage.class);
                listener.callback(channels, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 정보: 3. getChannel
    //-----------------------------------------------------------------------
    public void getChannel(String channelId, OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findOne(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                GroupChannel groupChannel = new Gson().fromJson(response, GroupChannel.class);
                listener.callback(groupChannel, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 생성: 4-1. createChannel
    //-----------------------------------------------------------------------
    public void createChannel(  String channelId,
                                String name,
                                String profile,
                                String[] members,
                                boolean reuse,
                                Map<String, String> meta,
                                OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.create( channelId, name, profile, members, reuse, meta, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                GroupChannel groupChannel = new Gson().fromJson(response, GroupChannel.class);
                listener.callback(groupChannel, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 생성: 4-2. createChannel (아이디 자동 생성)
    //-----------------------------------------------------------------------
    public void createChannel(  String name,
                                String profile,
                                String[] members,
                                boolean reuse,
                                Map<String, String> meta,
                                OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        String channelId = "ch_";
        String alphanumerics = "abcdefghijklmnopqrstuvwxyz1234567890_";
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(alphanumerics.length());
            char randomChar = alphanumerics.charAt(index);
            channelId += randomChar;
        }

        createChannel(channelId, name, profile, members, reuse, meta, listener);
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 수정: 5. updateChannel
    //-----------------------------------------------------------------------
    public void updateChannel(String channelId,
                              String name,
                              String profile,
                              OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.update(channelId, name, profile, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                GroupChannel groupChannel = new Gson().fromJson(response, GroupChannel.class);
                listener.callback(groupChannel, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 삭제: 6. deleteChannel
    //-----------------------------------------------------------------------
    public void deleteChannel(String channelId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.delete(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 참가자 목록: 7. getMemberList
    //-----------------------------------------------------------------------
    public void getMemberList(String channelId, OnCallback<BaseUser[]> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findMembers(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BaseUser[] members = new Gson().fromJson(response, BaseUser[].class);
                listener.callback(members, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 채널 입장: 8. joinChannel
    //-----------------------------------------------------------------------
    public void joinChannel(String channelId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.join(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 채널 퇴장: 9. leaveChannel
    //-----------------------------------------------------------------------
    public void leaveChannel(String channelId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.leave(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 매니저 목록: 10. getManagerList
    //-----------------------------------------------------------------------
    public void getManagerList(String channelId, OnCallback<BaseUser[]> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findManagers(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BaseUser[] members = new Gson().fromJson(response, BaseUser[].class);
                listener.callback(members, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 매니저 등재: 11. registerManager
    //-----------------------------------------------------------------------
    public void registerManager(String channelId, String userId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.registerManager(channelId, userId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 매니저 삭제: 12. deleteManager
    //-----------------------------------------------------------------------
    public void deleteManager(String channelId, String userId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.unregisterManager(channelId, userId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 읽음 확인 처리(특정 채널): 13. readMessage
    //-----------------------------------------------------------------------
    public void readMessage(String channelId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.read(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 읽음 확인 처리(다수 채널 일괄 처리): 14. readMessage
    //-----------------------------------------------------------------------
    public void readMessage(String[] channelIds, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().usersSdk.read(channelIds, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 수식 확인 처리: 15. deliveredMessage
    //-----------------------------------------------------------------------
    public void deliveredMessage(String channelId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.delivered(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메타 데이터 수정: 16. updateMeta
    //-----------------------------------------------------------------------
    public void updateMeta(String channelId, Map<String, String> meta, OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.updateMeta(channelId, meta, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                GroupChannel groupChannel = new Gson().fromJson(response, GroupChannel.class);
                listener.callback(groupChannel, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메타 데이터 삭제: 17. deleteMeta
    //-----------------------------------------------------------------------
    public void deleteMeta(String channelId, String[] keys, OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.deleteMeta(channelId, keys, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                GroupChannel groupChannel = new Gson().fromJson(response, GroupChannel.class);
                listener.callback(groupChannel, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 활성 사용자 조회: 18. getOnlineMemberList
    //-----------------------------------------------------------------------
    public void getOnlineMemberList(String channelId, OnCallback<String[]> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findOnlineMembers(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                String[] members = new Gson().fromJson(response, String[].class);
                listener.callback(members, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 중재(채널 동결 / 해제): 19. freezeChannel
    //-----------------------------------------------------------------------
    public void freezeChannel(String channelId, boolean freeze, OnCallback<GroupChannel> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.freeze(channelId, freeze, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                GroupChannel groupChannel = new Gson().fromJson(response, GroupChannel.class);
                listener.callback(groupChannel, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 중재(사용자 금지): 20. ban
    //-----------------------------------------------------------------------
    public void ban(String channelId, String userId, int seconds, String reason, OnCallback<BanInfo> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.ban(channelId, userId, seconds, reason, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BanInfo banInfo = new Gson().fromJson(response, BanInfo.class);
                listener.callback(banInfo, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 중재(사용자 금지 해제): 21. unban
    //-----------------------------------------------------------------------
    public void unban(String channelId, String userId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.unban(channelId, userId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 중재(사용자 금지 목록 조회): 22. getBannedList
    //-----------------------------------------------------------------------
    public void getBannedList(String channelId, OnCallback<BanInfo[]> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelSdk.findBanList(channelId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BanInfo[] bans = new Gson().fromJson(response, BanInfo[].class);
                listener.callback(bans, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 목록: 1-1. getMessageList
    //-----------------------------------------------------------------------
    public void getMessageList(String channelId, OnCallback<BaseMessage[]> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelMessageSdk.get(channelId, 0, null, null, 0, null, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                MessagePage messagePage = new Gson().fromJson(response, MessagePage.class);
                listener.callback(messagePage.messages, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 목록: 1-2. getMessageList (filtered)
    //-----------------------------------------------------------------------
    public void getMessageList(String channelId,
                               int limit,
                               String mode,
                               String type,
                               String content,
                               long messageId,
                               OnCallback<BaseMessage[]> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        limit = Math.max(1, limit);
        limit = Math.min(15, limit);

        ChatClient.getInstance().groupChannelMessageSdk.get(channelId, messageId, mode, type, limit, content, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                MessagePage messagePage = new Gson().fromJson(response, MessagePage.class);
                listener.callback(messagePage.messages, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 생성: 2-1. sendMessage (텍스트)
    //-----------------------------------------------------------------------
    public void sendMessage(String channelId, String text, Map<String, String> meta, OnCallback<BaseMessage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelMessageSdk.create(channelId, "text", text, meta, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BaseMessage message = new Gson().fromJson(response, BaseMessage.class);
                listener.callback(message, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 생성: 2-2. sendMessage (파일)
    //-----------------------------------------------------------------------
    public void sendMessage(String channelId, File file, Map<String, String> meta, OnCallback<BaseMessage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        String url = ChatClient.getInstance().groupChannelMessageSdk.getUrl() + channelId + "/messages";
        Map<String, String> headers = ChatClient.getInstance().groupChannelMessageSdk.getHeaders();


        new UploadRequest(null, url, file, headers, (response, error) -> {
            if (response.isEmpty()) {
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
                return;
            }
            try {
                BaseMessage message = new Gson().fromJson(response, BaseMessage.class);
                updateMessageMeta(channelId, message.getId(), meta, (message2, errorType) -> {
                    if (message2 != null) {
                        listener.callback(message2, 0);
                    }
                    else listener.callback(null, errorType);
                });
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 삭제: 3. deleteMessage
    //-----------------------------------------------------------------------
    public void deleteMessage(String channelId, long messageId, OnCallback<Boolean> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelMessageSdk.delete(channelId, messageId, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(false, ResponseError.fromJson(error).code);
            }
            else listener.callback(true, 0);
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 메타 데이터 수정: 4. updateMessageMeta
    //-----------------------------------------------------------------------
    public void updateMessageMeta(String channelId, long messageId, Map<String, String> meta, OnCallback<BaseMessage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelMessageSdk.updateMeta(channelId, messageId, meta, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BaseMessage message = new Gson().fromJson(response, BaseMessage.class);
                listener.callback(message, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 그룹 채널 메시지 메타 데이터 삭제: 5. deleteMessageMeta
    //-----------------------------------------------------------------------
    public void deleteMessageMeta(String channelId, long messageId, String[] keys, OnCallback<BaseMessage> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().groupChannelMessageSdk.deleteMeta(channelId, messageId, keys, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                Util.error(error);
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                BaseMessage message = new Gson().fromJson(response, BaseMessage.class);
                listener.callback(message, 0);
            } catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

} // GroupChannelApi.java