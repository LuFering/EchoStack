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

import com.nageoffer.ai.ragent.framework.annotation.Auditable;
import com.nageoffer.ai.ragent.framework.context.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 审计日志 AOP 切面
 * <p>拦截标注了 @Auditable 的方法，成功后自动记录审计日志。
 * 日志通过 RocketMQ 异步发送，不阻塞主流程。
 */
@Slf4j
@Aspect
@Component
public class AuditAspect {

    @AfterReturning("@annotation(auditable)")
    public void afterReturning(JoinPoint joinPoint, Auditable auditable) {
        try {
            String userId = UserContext.getUserId();
            String username = UserContext.getUsername();
            String tenantId = UserContext.getTenantId();

            String ipAddress = "unknown";
            String userAgent = "unknown";
            try {
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    HttpServletRequest request = attrs.getRequest();
                    ipAddress = getClientIp(request);
                    userAgent = request.getHeader("User-Agent");
                    if (userAgent == null) {
                        userAgent = "unknown";
                    }
                }
            } catch (Exception e) {
                log.debug("获取请求信息失败", e);
            }

            log.info("[AUDIT] user={}({}), tenant={}, action={}, resourceType={}, resourceName={}, ip={}",
                    username, userId, tenantId,
                    auditable.action(), auditable.resourceType(),
                    auditable.resourceName(), ipAddress);

            // TODO: 后续可通过 RocketMQ 异步发送审计日志到数据库
            // auditEventPublisher.publish(new AuditEvent(...));

        } catch (Exception e) {
            log.error("审计日志记录失败", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
