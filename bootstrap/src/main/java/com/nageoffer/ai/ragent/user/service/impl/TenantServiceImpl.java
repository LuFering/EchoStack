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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.context.UserContext;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.user.controller.request.TenantCreateRequest;
import com.nageoffer.ai.ragent.user.controller.request.TenantUpdateRequest;
import com.nageoffer.ai.ragent.user.controller.vo.TenantVO;
import com.nageoffer.ai.ragent.user.dao.entity.TenantDO;
import com.nageoffer.ai.ragent.user.dao.mapper.TenantMapper;
import com.nageoffer.ai.ragent.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户服务实现（仅超级管理员可调用）
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantMapper tenantMapper;

    @Override
    public IPage<TenantVO> pageQuery(int page, int size, String keyword) {
        if (!UserContext.isSuperAdmin()) {
            throw new ClientException("仅超级管理员可管理租户");
        }
        LambdaQueryWrapper<TenantDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(TenantDO::getName, keyword)
                    .or()
                    .like(TenantDO::getContactName, keyword)
                    .or()
                    .like(TenantDO::getContactEmail, keyword);
        }
        wrapper.orderByDesc(TenantDO::getCreateTime);
        IPage<TenantDO> doPage = tenantMapper.selectPage(
                new Page<>(page, size), wrapper);
        return doPage.convert(this::toVO);
    }

    @Override
    @Transactional
    public TenantVO create(TenantCreateRequest request) {
        if (!UserContext.isSuperAdmin()) {
            throw new ClientException("仅超级管理员可创建租户");
        }
        if (StrUtil.isBlank(request.getName())) {
            throw new ClientException("租户名称不能为空");
        }
        // 检查租户名称是否重复
        Long count = tenantMapper.selectCount(
                new LambdaQueryWrapper<TenantDO>()
                        .eq(TenantDO::getName, request.getName())
        );
        if (count > 0) {
            throw new ClientException("租户名称已存在");
        }

        TenantDO entity = new TenantDO();
        entity.setName(request.getName());
        entity.setDomain(request.getDomain());
        entity.setContactName(request.getContactName());
        entity.setContactEmail(request.getContactEmail());
        entity.setContactPhone(request.getContactPhone());
        entity.setPlanType(StrUtil.isBlank(request.getPlanType()) ? "FREE" : request.getPlanType());
        entity.setMaxUsers(request.getMaxUsers() != null ? request.getMaxUsers() : 50);
        entity.setMaxKb(request.getMaxKb() != null ? request.getMaxKb() : 5);
        entity.setMaxStorage(10737418240L);
        entity.setStatus("ACTIVE");
        entity.setCreatedBy(UserContext.getUserId());
        entity.setRemark(request.getRemark());
        tenantMapper.insert(entity);

        return toVO(entity);
    }

    @Override
    @Transactional
    public TenantVO update(String id, TenantUpdateRequest request) {
        if (!UserContext.isSuperAdmin()) {
            throw new ClientException("仅超级管理员可更新租户");
        }
        TenantDO entity = tenantMapper.selectById(id);
        if (entity == null) {
            throw new ClientException("租户不存在");
        }
        if (StrUtil.isNotBlank(request.getName())) {
            entity.setName(request.getName());
        }
        if (request.getDomain() != null) {
            entity.setDomain(request.getDomain());
        }
        if (request.getContactName() != null) {
            entity.setContactName(request.getContactName());
        }
        if (request.getContactEmail() != null) {
            entity.setContactEmail(request.getContactEmail());
        }
        if (request.getContactPhone() != null) {
            entity.setContactPhone(request.getContactPhone());
        }
        if (StrUtil.isNotBlank(request.getPlanType())) {
            entity.setPlanType(request.getPlanType());
        }
        if (request.getMaxUsers() != null) {
            entity.setMaxUsers(request.getMaxUsers());
        }
        if (request.getMaxKb() != null) {
            entity.setMaxKb(request.getMaxKb());
        }
        if (StrUtil.isNotBlank(request.getStatus())) {
            entity.setStatus(request.getStatus());
        }
        if (request.getRemark() != null) {
            entity.setRemark(request.getRemark());
        }
        tenantMapper.updateById(entity);
        return toVO(entity);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!UserContext.isSuperAdmin()) {
            throw new ClientException("仅超级管理员可删除租户");
        }
        tenantMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleStatus(String id, String status) {
        if (!UserContext.isSuperAdmin()) {
            throw new ClientException("仅超级管理员可操作租户状态");
        }
        TenantDO entity = tenantMapper.selectById(id);
        if (entity == null) {
            throw new ClientException("租户不存在");
        }
        entity.setStatus(status);
        tenantMapper.updateById(entity);
    }

    private TenantVO toVO(TenantDO entity) {
        return new TenantVO(
                entity.getId(),
                entity.getName(),
                entity.getDomain(),
                entity.getStatus(),
                entity.getPlanType(),
                entity.getMaxUsers(),
                entity.getMaxKb(),
                entity.getMaxStorage(),
                entity.getExpireTime(),
                entity.getContactName(),
                entity.getContactEmail(),
                entity.getContactPhone(),
                entity.getRemark(),
                entity.getCreateTime(),
                entity.getUpdateTime()
        );
    }
}
