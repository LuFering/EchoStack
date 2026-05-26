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

package com.nageoffer.ai.ragent.user.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantVO {
    private String id;
    private String name;
    private String domain;
    private String status;
    private String planType;
    private Integer maxUsers;
    private Integer maxKb;
    private Long maxStorage;
    private Date expireTime;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
