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

/**
 * 知识库成员角色枚举
 */
@Getter
public enum KbMemberRole {

    ADMIN("admin", "管理员", "可以管理知识库成员和文档"),
    EDITOR("editor", "编辑者", "可以上传和编辑文档"),
    VIEWER("viewer", "查看者", "只读查看知识库内容");

    private final String code;
    private final String name;
    private final String description;

    KbMemberRole(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
}
