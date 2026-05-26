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

package com.nageoffer.ai.ragent.framework.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * <p>标注在 Controller 方法上，自动校验当前用户是否拥有指定权限。
 * 超级管理员自动拥有所有权限。
 *
 * <pre>
 * &#064;RequirePermission("kb:document:upload")
 * public Result uploadDocument(...) { ... }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 权限标识（如 kb:document:upload）
     */
    String value();

    /**
     * 所需角色（满足其一即可），与 value 为 AND 关系
     */
    String[] anyRole() default {};
}
