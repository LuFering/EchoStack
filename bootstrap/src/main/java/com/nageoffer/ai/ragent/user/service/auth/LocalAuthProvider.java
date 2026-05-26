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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.user.dao.entity.UserDO;
import com.nageoffer.ai.ragent.user.dao.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 本地用户名密码认证提供者
 */
@Component
@RequiredArgsConstructor
public class LocalAuthProvider implements AuthenticationProvider {

    private final UserMapper userMapper;

    @Value("${enterprise.auth.providers[0].enabled:true}")
    private boolean enabled;

    @Override
    public String getType() {
        return "local";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ClientException("用户名或密码不能为空");
        }

        UserDO user = userMapper.selectOne(
                Wrappers.lambdaQuery(UserDO.class)
                        .eq(UserDO::getUsername, username)
                        .eq(UserDO::getDeleted, 0)
        );

        if (user == null || !password.equals(user.getPassword())) {
            throw new ClientException("用户名或密码错误");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new ClientException("账号已被禁用，请联系管理员");
        }

        if (user.getId() == null) {
            throw new ClientException("用户信息异常");
        }

        return user.getId().toString();
    }
}
