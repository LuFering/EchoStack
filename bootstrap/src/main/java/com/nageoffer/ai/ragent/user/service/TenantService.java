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

package com.nageoffer.ai.ragent.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nageoffer.ai.ragent.user.controller.request.TenantCreateRequest;
import com.nageoffer.ai.ragent.user.controller.request.TenantUpdateRequest;
import com.nageoffer.ai.ragent.user.controller.vo.TenantVO;

/**
 * 租户服务接口（仅超级管理员可调用）
 */
public interface TenantService {

    /**
     * 分页查询租户列表
     */
    IPage<TenantVO> pageQuery(int page, int size, String keyword);

    /**
     * 创建租户（自动初始化默认知识库、意图树等）
     */
    TenantVO create(TenantCreateRequest request);

    /**
     * 更新租户
     */
    TenantVO update(String id, TenantUpdateRequest request);

    /**
     * 删除租户
     */
    void delete(String id);

    /**
     * 启用/停用租户
     */
    void toggleStatus(String id, String status);
}
