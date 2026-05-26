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

package com.nageoffer.ai.ragent.knowledge.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KbMemberDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 知识库成员 Mapper
 */
public interface KbMemberMapper extends BaseMapper<KbMemberDO> {

    /**
     * 查询用户在指定知识库的角色
     */
    @Select("SELECT role FROM t_kb_member WHERE kb_id = #{kbId} AND user_id = #{userId}")
    String selectRoleByKbIdAndUserId(@Param("kbId") String kbId, @Param("userId") String userId);

    /**
     * 查询知识库的所有成员
     */
    @Select("SELECT * FROM t_kb_member WHERE kb_id = #{kbId}")
    List<KbMemberDO> selectByKbId(@Param("kbId") String kbId);
}
