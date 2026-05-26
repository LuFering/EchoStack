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

import com.nageoffer.ai.ragent.framework.exception.ClientException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证提供者管理器
 * <p>维护所有 AuthenticationProvider 实例，根据请求类型路由到对应认证提供者。
 */
@Component
public class AuthenticationProviderManager {

    private final Map<String, AuthenticationProvider> providerMap;

    public AuthenticationProviderManager(List<AuthenticationProvider> providers) {
        this.providerMap = providers.stream()
                .filter(AuthenticationProvider::isEnabled)
                .collect(Collectors.toMap(
                        AuthenticationProvider::getType,
                        p -> p,
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 获取指定类型的认证提供者
     */
    public AuthenticationProvider getProvider(String type) {
        AuthenticationProvider provider = providerMap.get(type);
        if (provider == null) {
            throw new ClientException("不支持的认证方式: " + type);
        }
        return provider;
    }

    /**
     * 获取所有已启用的认证提供者类型
     */
    public List<String> getEnabledProviders() {
        return List.copyOf(providerMap.keySet());
    }

    /**
     * 是否有指定的认证提供者
     */
    public boolean hasProvider(String type) {
        return providerMap.containsKey(type);
    }
}
