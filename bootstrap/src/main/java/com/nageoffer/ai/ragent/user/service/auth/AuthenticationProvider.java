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

package com.nageoffer.ai.ragent.user.service.auth;

/**
 * 认证提供者接口（策略模式）
 * <p>支持多种认证方式：本地账号、LDAP/AD、OAuth2/OIDC、SAML 等。
 */
public interface AuthenticationProvider {

    /**
     * 认证提供者类型标识
     */
    String getType();

    /**
     * 是否启用
     */
    boolean isEnabled();

    /**
     * 执行认证，返回用户ID（认证失败时抛出异常）
     *
     * @param request 认证请求
     * @return 用户ID
     */
    String authenticate(AuthenticationRequest request);

    /**
     * 支持的认证请求类型
     */
    default boolean supports(AuthenticationRequest request) {
        return getType().equals(request.getProviderType());
    }
}
