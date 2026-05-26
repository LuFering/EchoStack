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

package com.nageoffer.ai.ragent.framework.aop;

import com.nageoffer.ai.ragent.framework.annotation.RequirePermission;
import com.nageoffer.ai.ragent.framework.context.UserContext;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 权限校验 AOP 切面
 * <p>拦截标注了 @RequirePermission 的方法，校验当前用户是否拥有所需权限和角色。
 * 超级管理员自动通过所有权限校验。
 */
@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 超级管理员跳过权限校验
        if (UserContext.isSuperAdmin()) {
            return;
        }

        String requiredPerm = requirePermission.value();
        String[] requiredRoles = requirePermission.anyRole();

        // 校验权限
        if (!requiredPerm.isEmpty() && !UserContext.hasPermission(requiredPerm)) {
            throw new ClientException("权限不足，需要权限: " + requiredPerm);
        }

        // 校验角色（满足其一即可）
        if (requiredRoles.length > 0) {
            List<String> userRoles = UserContext.getRoleList();
            boolean hasRole = false;
            for (String role : requiredRoles) {
                if (userRoles.contains(role)) {
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                throw new ClientException("权限不足，需要角色: " + String.join(", ", requiredRoles));
            }
        }
    }
}
