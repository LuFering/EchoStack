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

package com.nageoffer.ai.ragent.user.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 审计日志实体（只追加，不删除，不可篡改）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_audit_log")
public class AuditLogDO {

    @TableId
    private String id;

    private String tenantId;

    private String userId;

    private String username;

    /**
     * 操作类型：CREATE / UPDATE / DELETE / LOGIN / LOGOUT / EXPORT
     */
    private String action;

    /**
     * 资源类型：kb / document / user / tenant / system
     */
    private String resourceType;

    private String resourceId;

    private String resourceName;

    /**
     * 操作详情（JSON格式）
     */
    private String detail;

    private String ipAddress;

    private String userAgent;

    private Date createTime;
}
