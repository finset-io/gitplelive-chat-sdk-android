/**
 * UsersApi.java
 *
 */

package io.gitplelive.chat.sdk.api;


import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Map;

import io.gitplelive.chat.sdk.helper.Util;
import io.gitplelive.chat.sdk.interfaces.OnCallback;
import io.gitplelive.chat.sdk.model.BaseUser;
import io.gitplelive.chat.sdk.model.ErrorType;
import io.gitplelive.chat.sdk.model.ResponseError;


public class UsersApi {

    public BaseUser me;

    public boolean isMemberOf(BaseUser[] users) {
        return Arrays.stream(users).anyMatch(x -> x.getUserId().equals(me.getUserId()));
    }

    public boolean equals(BaseUser user) {
        return user.getUserId().equals(me.getUserId());
    }

    //-----------------------------------------------------------------------
    // 사용자 조회: 1. me
    //-----------------------------------------------------------------------
    public void me(OnCallback<BaseUser> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().usersSdk.find((response, error) -> {
            if (listener == null) return;

            if (error != null) {
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                me = new Gson().fromJson(response, BaseUser.class);
                listener.callback(me, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 사용자 수정: 2. updateUser
    //-----------------------------------------------------------------------
    public void updateUser(String name, String profile, OnCallback<BaseUser> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().usersSdk.update(name, profile, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                me = new Gson().fromJson(response, BaseUser.class);
                listener.callback(me, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 사용자 메타 데이터 수정: 3. updateMeta
    //-----------------------------------------------------------------------
    public void updateMeta(Map<String, String> meta, OnCallback<BaseUser> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().usersSdk.updateMeta(meta, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                me = new Gson().fromJson(response, BaseUser.class);
                listener.callback(me, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

    //-----------------------------------------------------------------------
    // 사용자 메타 데이터 삭제: 4. deleteMeta
    //-----------------------------------------------------------------------
    public void deleteMeta(String[] keys, OnCallback<BaseUser> listener) {
        if (ChatClient.getInstance().isNotConnected()) return;

        ChatClient.getInstance().usersSdk.deleteMeta(keys, (response, error) -> {
            if (listener == null) return;

            if (error != null) {
                listener.callback(null, ResponseError.fromJson(error).code);
                return;
            }
            try {
                me = new Gson().fromJson(response, BaseUser.class);
                listener.callback(me, 0);
            }
            catch (Exception e) {
                Util.error(e.toString());
                listener.callback(null, ErrorType.UNKNOWN_ERROR);
            }
        });
    }

} // UsersApi.java