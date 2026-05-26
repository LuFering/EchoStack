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

package com.nageoffer.ai.ragent.knowledge.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KnowledgeDocumentDO;
import com.nageoffer.ai.ragent.knowledge.dao.mapper.KnowledgeDocumentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 过期文档归档定时任务
 * <p>每天凌晨 3 点执行，将过期文档标记为已归档。
 * 归档文档从活跃 Collection 迁移，不再参与检索。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentArchiveScheduler {

    private final KnowledgeDocumentMapper documentMapper;

    /**
     * 每天凌晨 3:00 执行归档
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void archiveExpiredDocuments() {
        log.info("开始归档过期文档...");

        // 查找已过期但未归档的文档
        LambdaQueryWrapper<KnowledgeDocumentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(KnowledgeDocumentDO::getExpireTime)
                .lt(KnowledgeDocumentDO::getExpireTime, new Date())
                .ne(KnowledgeDocumentDO::getArchiveStatus, "ARCHIVED");

        List<KnowledgeDocumentDO> expiredDocs = documentMapper.selectList(wrapper);

        if (expiredDocs.isEmpty()) {
            log.info("无过期文档需要归档");
            return;
        }

        int count = 0;
        for (KnowledgeDocumentDO doc : expiredDocs) {
            doc.setArchiveStatus("ARCHIVED");
            doc.setEnabled(0); // 禁用文档，不再参与检索
            documentMapper.updateById(doc);
            count++;
        }

        log.info("归档完成，共处理 {} 个过期文档", count);
    }
}
