/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nageoffer.ai.ragent.framework.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.nageoffer.ai.ragent.framework.exception.ClientException;

import java.util.Collections;
import java.util.List;

/**
 * 用户上下文容器（基于 TTL 传递当前线程的登录用户）
 */
public final class UserContext {

    private static final TransmittableThreadLocal<LoginUser> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置当前线程的用户上下文
     */
    public static void set(LoginUser user) {
        CONTEXT.set(user);
    }

    /**
     * 获取当前线程的用户上下文
     */
    public static LoginUser get() {
        return CONTEXT.get();
    }

    /**
     * 获取当前线程用户，若不存在则抛异常
     */
    public static LoginUser requireUser() {
        LoginUser user = CONTEXT.get();
        if (user == null) {
            throw new ClientException("未获取到当前登录用户");
        }
        return user;
    }

    /**
     * 获取当前用户 ID（未登录返回 null）
     */
    public static String getUserId() {
        LoginUser user = CONTEXT.get();
        return user == null ? null : user.getUserId();
    }

    /**
     * 获取当前用户名（未登录返回 null）
     */
    public static String getUsername() {
        LoginUser user = CONTEXT.get();
        return user == null ? null : user.getUsername();
    }

    /**
     * 获取当前角色（未登录返回 null）
     */
    public static String getRole() {
        LoginUser user = CONTEXT.get();
        return user == null ? null : user.getRole();
    }

    /**
     * 获取当前头像（未登录返回 null）
     */
    public static String getAvatar() {
        LoginUser user = CONTEXT.get();
        return user == null ? null : user.getAvatar();
    }

    /**
     * 清理当前线程的用户上下文
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * 判断是否已存在用户上下文
     */
    public static boolean hasUser() {
        return CONTEXT.get() != null;
    }

    /**
     * 获取当前租户 ID（未登录返回 null）
     */
    public static String getTenantId() {
        LoginUser user = CONTEXT.get();
        return user == null ? null : user.getTenantId();
    }

    /**
     * 获取当前用户角色列表（未登录返回空列表）
     */
    public static List<String> getRoleList() {
        LoginUser user = CONTEXT.get();
        return user == null || user.getRoleList() == null
                ? Collections.emptyList() : user.getRoleList();
    }

    /**
     * 获取当前用户权限列表（未登录返回空列表）
     */
    public static List<String> getPermissionList() {
        LoginUser user = CONTEXT.get();
        return user == null || user.getPermissionList() == null
                ? Collections.emptyList() : user.getPermissionList();
    }

    /**
     * 判断当前用户是否拥有指定权限
     */
    public static boolean hasPermission(String permission) {
        LoginUser user = CONTEXT.get();
        if (user == null) {
            return false;
        }
        if (user.isSuperAdmin()) {
            return true;
        }
        List<String> permissions = user.getPermissionList();
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 判断当前用户是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        LoginUser user = CONTEXT.get();
        return user != null && user.isSuperAdmin();
    }

    /**
     * 判断当前用户是否为租户管理员
     */
    public static boolean isTenantAdmin() {
        LoginUser user = CONTEXT.get();
        return user != null && user.isTenantAdmin();
    }
}
