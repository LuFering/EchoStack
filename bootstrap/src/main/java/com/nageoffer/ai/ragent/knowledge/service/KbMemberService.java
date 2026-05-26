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

package com.nageoffer.ai.ragent.knowledge.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nageoffer.ai.ragent.framework.context.UserContext;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KbMemberDO;
import com.nageoffer.ai.ragent.knowledge.dao.mapper.KbMemberMapper;
import com.nageoffer.ai.ragent.knowledge.enums.KbMemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 知识库成员管理服务
 */
@Service
@RequiredArgsConstructor
public class KbMemberService {

    private final KbMemberMapper kbMemberMapper;

    /**
     * 添加知识库成员
     */
    @Transactional
    public void addMember(String kbId, String userId, String role) {
        checkManagePermission(kbId);
        KbMemberDO existing = kbMemberMapper.selectOne(
                Wrappers.lambdaQuery(KbMemberDO.class)
                        .eq(KbMemberDO::getKbId, kbId)
                        .eq(KbMemberDO::getUserId, userId)
        );
        if (existing != null) {
            throw new ClientException("该用户已是知识库成员");
        }
        KbMemberDO member = new KbMemberDO();
        member.setKbId(kbId);
        member.setUserId(userId);
        member.setRole(role);
        kbMemberMapper.insert(member);
    }

    /**
     * 移除知识库成员
     */
    @Transactional
    public void removeMember(String kbId, String userId) {
        checkManagePermission(kbId);
        kbMemberMapper.delete(
                Wrappers.lambdaQuery(KbMemberDO.class)
                        .eq(KbMemberDO::getKbId, kbId)
                        .eq(KbMemberDO::getUserId, userId)
        );
    }

    /**
     * 更新成员角色
     */
    @Transactional
    public void updateMemberRole(String kbId, String userId, String role) {
        checkManagePermission(kbId);
        KbMemberDO member = kbMemberMapper.selectOne(
                Wrappers.lambdaQuery(KbMemberDO.class)
                        .eq(KbMemberDO::getKbId, kbId)
                        .eq(KbMemberDO::getUserId, userId)
        );
        if (member == null) {
            throw new ClientException("该用户不是知识库成员");
        }
        member.setRole(role);
        kbMemberMapper.updateById(member);
    }

    /**
     * 获取知识库成员列表
     */
    public List<KbMemberDO> listMembers(String kbId) {
        return kbMemberMapper.selectByKbId(kbId);
    }

    /**
     * 获取当前用户在知识库的角色
     */
    public String getCurrentUserRole(String kbId) {
        String userId = UserContext.getUserId();
        if (StrUtil.isBlank(userId)) {
            return null;
        }
        return kbMemberMapper.selectRoleByKbIdAndUserId(kbId, userId);
    }

    /**
     * 检查当前用户是否有管理知识库成员的权限
     */
    public void checkManagePermission(String kbId) {
        if (UserContext.isSuperAdmin() || UserContext.isTenantAdmin()) {
            return;
        }
        String role = getCurrentUserRole(kbId);
        if (!KbMemberRole.ADMIN.getCode().equals(role)) {
            throw new ClientException("无权管理知识库成员");
        }
    }

    /**
     * 检查当前用户是否可访问知识库
     */
    public void checkAccessPermission(String kbId, String tenantId, String visibility) {
        if (UserContext.isSuperAdmin()) {
            return;
        }
        String currentTenantId = UserContext.getTenantId();
        // 私有知识库：仅成员可访问
        if ("PRIVATE".equalsIgnoreCase(visibility)) {
            String role = getCurrentUserRole(kbId);
            if (StrUtil.isBlank(role)) {
                throw new ClientException("无权访问该知识库");
            }
            return;
        }
        // 租户内可见：需要同租户
        if ("TENANT".equalsIgnoreCase(visibility)) {
            if (!Objects.equals(currentTenantId, tenantId)) {
                throw new ClientException("无权访问该知识库");
            }
            return;
        }
        // PUBLIC：允许访问
    }
}
