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

package com.nageoffer.ai.ragent.framework.database;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.nageoffer.ai.ragent.framework.context.UserContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * MyBatis-Plus 多租户行级处理器
 *
 * <p>自动在 SQL 中追加 tenant_id = ? 条件，实现租户数据隔离。
 * 超级管理员（isSuperAdmin=true）跳过租户过滤。
 * 未配置 tenantId 的全局表（如 t_permission、t_role）也跳过。
 */
@Component
public class RagentTenantLineHandler implements TenantLineHandler {

    private static final String TENANT_COLUMN = "tenant_id";

    /**
     * 不启用租户隔离的表（全局共享表）
     */
    private static final Set<String> IGNORE_TABLES = new HashSet<>(Arrays.asList(
            "t_tenant",
            "t_permission",
            "t_role",
            "t_role_permission"
    ));

    @Override
    public Expression getTenantId() {
        String tenantId = UserContext.getTenantId();
        if (tenantId == null) {
            // 无租户上下文时使用空字符串（匹配不到任何记录，安全兜底）
            return new StringValue("");
        }
        return new StringValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return TENANT_COLUMN;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 超级管理员跳过租户隔离
        if (UserContext.isSuperAdmin()) {
            return true;
        }
        // 指定全局表跳过租户隔离
        if (IGNORE_TABLES.contains(tableName)) {
            return true;
        }
        // 未登录用户不跳过（返回空 tenantId 作为兜底）
        return !UserContext.hasUser();
    }
}
