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

package com.nageoffer.ai.ragent.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nageoffer.ai.ragent.framework.annotation.RequirePermission;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.user.controller.request.TenantCreateRequest;
import com.nageoffer.ai.ragent.user.controller.request.TenantUpdateRequest;
import com.nageoffer.ai.ragent.user.controller.vo.TenantVO;
import com.nageoffer.ai.ragent.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租户管理控制器（仅超级管理员）
 */
@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    /**
     * 分页查询租户列表
     */
    @GetMapping
    @RequirePermission("tenant:list")
    public Result<IPage<TenantVO>> pageQuery(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false) String keyword) {
        return Results.success(tenantService.pageQuery(page, size, keyword));
    }

    /**
     * 创建租户
     */
    @PostMapping
    @RequirePermission("tenant:create")
    public Result<TenantVO> create(@RequestBody TenantCreateRequest request) {
        return Results.success(tenantService.create(request));
    }

    /**
     * 更新租户
     */
    @PutMapping("/{id}")
    @RequirePermission("tenant:update")
    public Result<TenantVO> update(@PathVariable String id,
                                    @RequestBody TenantUpdateRequest request) {
        return Results.success(tenantService.update(id, request));
    }

    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    @RequirePermission("tenant:delete")
    public Result<Void> delete(@PathVariable String id) {
        tenantService.delete(id);
        return Results.success();
    }

    /**
     * 启用/停用租户
     */
    @PutMapping("/{id}/status")
    @RequirePermission("tenant:update")
    public Result<Void> toggleStatus(@PathVariable String id,
                                      @RequestParam String status) {
        tenantService.toggleStatus(id, status);
        return Results.success();
    }
}
