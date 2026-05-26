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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 认证请求（统一模型）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    /**
     * 提供者类型：local / oauth2 / ldap / saml
     */
    private String providerType;

    /**
     * 用户名（local 认证）
     */
    private String username;

    /**
     * 密码（local / ldap 认证）
     */
    private String password;

    /**
     * OAuth2 / OIDC authorization code
     */
    private String authorizationCode;

    /**
     * OAuth2 state 参数
     */
    private String state;

    /**
     * SSO 提供方的回调请求参数（如企业微信 code）
     */
    private Map<String, String> extraParams;
}
