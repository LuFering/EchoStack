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

package com.nageoffer.ai.ragent.knowledge.controller;

import com.nageoffer.ai.ragent.framework.annotation.RequirePermission;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KbMemberDO;
import com.nageoffer.ai.ragent.knowledge.service.KbMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库成员管理控制器
 */
@RestController
@RequestMapping("/knowledge/kb/{kbId}/members")
@RequiredArgsConstructor
public class KbMemberController {

    private final KbMemberService kbMemberService;

    /**
     * 获取知识库成员列表
     */
    @GetMapping
    @RequirePermission("kb:list")
    public Result<List<KbMemberDO>> listMembers(@PathVariable String kbId) {
        return Results.success(kbMemberService.listMembers(kbId));
    }

    /**
     * 添加知识库成员
     */
    @PostMapping
    @RequirePermission("kb:manage")
    public Result<Void> addMember(@PathVariable String kbId,
                                   @RequestParam String userId,
                                   @RequestParam String role) {
        kbMemberService.addMember(kbId, userId, role);
        return Results.success();
    }

    /**
     * 更新成员角色
     */
    @PutMapping("/{userId}")
    @RequirePermission("kb:manage")
    public Result<Void> updateMemberRole(@PathVariable String kbId,
                                          @PathVariable String userId,
                                          @RequestParam String role) {
        kbMemberService.updateMemberRole(kbId, userId, role);
        return Results.success();
    }

    /**
     * 移除知识库成员
     */
    @DeleteMapping("/{userId}")
    @RequirePermission("kb:manage")
    public Result<Void> removeMember(@PathVariable String kbId,
                                      @PathVariable String userId) {
        kbMemberService.removeMember(kbId, userId);
        return Results.success();
    }

    /**
     * 获取当前用户在知识库的角色
     */
    @GetMapping("/my-role")
    public Result<String> getMyRole(@PathVariable String kbId) {
        return Results.success(kbMemberService.getCurrentUserRole(kbId));
    }
}
