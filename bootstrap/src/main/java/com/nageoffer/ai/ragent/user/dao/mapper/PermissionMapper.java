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

package com.nageoffer.ai.ragent.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.ai.ragent.user.dao.entity.PermissionDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限 Mapper
 */
public interface PermissionMapper extends BaseMapper<PermissionDO> {

    /**
     * 根据角色ID列表查询权限CODE
     */
    @Select("""
        SELECT DISTINCT p.code FROM t_permission p
        INNER JOIN t_role_permission rp ON p.id = rp.permission_id
        WHERE rp.role_id IN (
            SELECT ur.role_id FROM t_user_role ur WHERE ur.user_id = #{userId}
        )
        AND p.deleted = 0
    """)
    List<String> selectPermissionCodesByUserId(@Param("userId") String userId);
}
