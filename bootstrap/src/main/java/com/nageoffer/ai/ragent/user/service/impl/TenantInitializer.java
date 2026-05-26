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

package com.nageoffer.ai.ragent.user.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KnowledgeBaseDO;
import com.nageoffer.ai.ragent.knowledge.dao.mapper.KnowledgeBaseMapper;
import com.nageoffer.ai.ragent.user.dao.entity.TenantDO;
import com.nageoffer.ai.ragent.user.dao.entity.UserDO;
import com.nageoffer.ai.ragent.user.dao.entity.UserRoleDO;
import com.nageoffer.ai.ragent.user.dao.mapper.UserMapper;
import com.nageoffer.ai.ragent.user.dao.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户初始化器
 * <p>新租户创建后，自动初始化：默认管理员账号、默认知识库、角色绑定。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantInitializer {

    private static final Snowflake SNOWFLAKE = new Snowflake(1, 1);

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final KnowledgeBaseMapper knowledgeBaseMapper;

    /**
     * 为新租户初始化默认资源
     *
     * @param tenant       租户实体
     * @param adminUsername 管理员用户名（租户名 + _admin）
     * @param adminPassword 管理员初始密码
     * @return 创建的管理员用户ID
     */
    @Transactional
    public String initializeTenant(TenantDO tenant, String adminUsername, String adminPassword) {
        log.info("开始初始化租户: {}", tenant.getName());

        // 1. 创建租户管理员账号
        UserDO adminUser = new UserDO();
        adminUser.setUsername(adminUsername);
        adminUser.setPassword(adminPassword);
        adminUser.setRole("admin");
        adminUser.setTenantId(tenant.getId());
        adminUser.setIsTenantAdmin(1);
        adminUser.setStatus("ACTIVE");
        adminUser.setRealName(tenant.getContactName());
        adminUser.setEmail(tenant.getContactEmail());
        adminUser.setPhone(tenant.getContactPhone());
        userMapper.insert(adminUser);
        log.info("租户管理员账号已创建: {}", adminUsername);

        // 2. 绑定租户管理员角色（role_tenant_admin）
        UserRoleDO userRole = new UserRoleDO();
        userRole.setUserId(adminUser.getId());
        userRole.setRoleId("role_tenant_admin");
        userRoleMapper.insert(userRole);

        // 3. 创建默认知识库
        KnowledgeBaseDO defaultKb = new KnowledgeBaseDO();
        defaultKb.setName(tenant.getName() + " 知识库");
        defaultKb.setTenantId(tenant.getId());
        defaultKb.setVisibility("TENANT");
        defaultKb.setDescription("系统自动创建的默认知识库");
        defaultKb.setCreatedBy(adminUser.getId());
        defaultKb.setCollectionName("kb_" + tenant.getId() + "_default");
        knowledgeBaseMapper.insert(defaultKb);
        log.info("默认知识库已创建");

        log.info("租户 {} 初始化完成", tenant.getName());
        return adminUser.getId();
    }
}
