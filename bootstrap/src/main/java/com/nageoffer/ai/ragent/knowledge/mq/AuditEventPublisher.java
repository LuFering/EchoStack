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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 审计事件发布器
 * <p>将审计日志通过 RocketMQ 异步发送到持久化消费者。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.producer.group:ragent-producer_pg}")
    private String producerGroup;

    /**
     * 发布审计事件
     */
    public void publish(AuditLogDO auditLog) {
        try {
            rocketMQTemplate.syncSend("ragent-audit-log-topic",
                    MessageBuilder.withPayload(auditLog).build(),
                    3000);
            log.debug("审计事件已发送: action={}, resourceType={}", auditLog.getAction(), auditLog.getResourceType());
        } catch (Exception e) {
            log.error("审计事件发送失败，将降级到本地日志: action={}, resourceType={}",
                    auditLog.getAction(), auditLog.getResourceType(), e);
        }
    }
}
