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
 * 审计日志注解
 * <p>标注在需要记录操作审计的 Controller 方法上。
 * 方法执行成功后自动写入审计日志。
 *
 * <pre>
 * &#064;Auditable(action = "CREATE", resourceType = "document")
 * public Result uploadDocument(...) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {

    /**
     * 操作类型：CREATE / UPDATE / DELETE / LOGIN / LOGOUT / EXPORT
     */
    String action();

    /**
     * 资源类型：kb / document / user / tenant / system
     */
    String resourceType();

    /**
     * 资源名称（支持 SpEL 表达式，如 "#request.name"）
     */
    String resourceName() default "";
}
