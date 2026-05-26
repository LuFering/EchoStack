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

package com.nageoffer.ai.ragent.knowledge.mq;

import com.nageoffer.ai.ragent.user.dao.entity.AuditLogDO;
import com.nageoffer.ai.ragent.user.dao.mapper.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 审计日志 RocketMQ 消费者
 * <p>异步消费审计事件，写入数据库。
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = "ragent-audit-log-topic", consumerGroup = "ragent-audit-cg")
public class AuditLogConsumer implements RocketMQListener<AuditLogDO> {

    private final AuditLogMapper auditLogMapper;

    @Override
    public void onMessage(AuditLogDO auditLog) {
        try {
            auditLogMapper.insert(auditLog);
            log.debug("审计日志已持久化: action={}, user={}", auditLog.getAction(), auditLog.getUsername());
        } catch (Exception e) {
            log.error("审计日志持久化失败: action={}, user={}", auditLog.getAction(), auditLog.getUsername(), e);
        }
    }
}
