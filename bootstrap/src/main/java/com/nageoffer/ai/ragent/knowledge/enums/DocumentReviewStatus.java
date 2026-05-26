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

package com.nageoffer.ai.ragent.knowledge.enums;

import lombok.Getter;
import java.util.Set;

/**
 * 文档审核状态枚举
 *
 * <p>状态流转:
 * <pre>
 *   DRAFT --> PENDING_REVIEW --> APPROVED --> PUBLISHED
 *              |                        |
 *              v                        v
 *           REJECTED                PUBLISHED
 * </pre>
 */
@Getter
public enum DocumentReviewStatus {

    DRAFT("draft", "草稿", false),
    PENDING_REVIEW("pending_review", "待审核", false),
    APPROVED("approved", "已通过", true),
    REJECTED("rejected", "已驳回", true),
    PUBLISHED("published", "已发布", true);

    private final String code;
    private final String name;
    private final boolean terminal; // 是否为终态

    DocumentReviewStatus(String code, String name, boolean terminal) {
        this.code = code;
        this.name = name;
        this.terminal = terminal;
    }

    /**
     * 允许的状态转换
     */
    private static final java.util.Map<String, Set<String>> ALLOWED_TRANSITIONS = java.util.Map.of(
            "draft",          Set.of("pending_review"),
            "pending_review", Set.of("approved", "rejected"),
            "approved",       Set.of("published"),
            "rejected",       Set.of("draft", "pending_review"),
            "published",      Set.of()
    );

    /**
     * 检查状态转换是否合法
     */
    public static boolean canTransition(String from, String to) {
        Set<String> allowed = ALLOWED_TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);
    }
}
