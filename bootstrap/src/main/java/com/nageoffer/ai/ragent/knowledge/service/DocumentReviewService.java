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

package com.nageoffer.ai.ragent.knowledge.service;

import cn.hutool.core.util.StrUtil;
import com.nageoffer.ai.ragent.framework.context.UserContext;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KnowledgeDocumentDO;
import com.nageoffer.ai.ragent.knowledge.dao.mapper.KnowledgeDocumentMapper;
import com.nageoffer.ai.ragent.knowledge.enums.DocumentReviewStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 文档审核服务
 *
 * <p>支持文档的五段式审核流程：草稿 -> 待审核 -> 已通过/已驳回 -> 已发布
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentReviewService {

    private final KnowledgeDocumentMapper documentMapper;

    /**
     * 提交审核（草稿 -> 待审核）
     */
    @Transactional
    public void submitForReview(String docId) {
        KnowledgeDocumentDO doc = getDocument(docId);
        validateTransition(doc, "draft", "pending_review");
        doc.setReviewStatus("pending_review");
        doc.setUpdatedBy(UserContext.getUserId());
        documentMapper.updateById(doc);
        log.info("文档 {} 已提交审核", docId);
    }

    /**
     * 审核通过（待审核 -> 已通过）
     */
    @Transactional
    public void approve(String docId, String comment) {
        KnowledgeDocumentDO doc = getDocument(docId);
        validateTransition(doc, "pending_review", "approved");
        doc.setReviewStatus("approved");
        doc.setReviewerId(UserContext.getUserId());
        doc.setReviewComment(comment);
        doc.setReviewTime(new Date());
        doc.setUpdatedBy(UserContext.getUserId());
        documentMapper.updateById(doc);
        log.info("文档 {} 审核通过", docId);
    }

    /**
     * 审核驳回（待审核 -> 已驳回）
     */
    @Transactional
    public void reject(String docId, String reason) {
        if (StrUtil.isBlank(reason)) {
            throw new ClientException("驳回原因不能为空");
        }
        KnowledgeDocumentDO doc = getDocument(docId);
        validateTransition(doc, "pending_review", "rejected");
        doc.setReviewStatus("rejected");
        doc.setReviewerId(UserContext.getUserId());
        doc.setReviewComment(reason);
        doc.setReviewTime(new Date());
        doc.setUpdatedBy(UserContext.getUserId());
        documentMapper.updateById(doc);
        log.info("文档 {} 审核被驳回: {}", docId, reason);
    }

    /**
     * 发布文档（已通过 -> 已发布）
     */
    @Transactional
    public void publish(String docId) {
        KnowledgeDocumentDO doc = getDocument(docId);
        validateTransition(doc, "approved", "published");
        doc.setReviewStatus("published");
        doc.setUpdatedBy(UserContext.getUserId());
        documentMapper.updateById(doc);
        log.info("文档 {} 已发布", docId);
    }

    /**
     * 查询待审核文档列表
     */
    public List<KnowledgeDocumentDO> listPendingReview(String kbId) {
        return documentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeDocumentDO>()
                        .eq(kbId != null, KnowledgeDocumentDO::getKbId, kbId)
                        .eq(KnowledgeDocumentDO::getReviewStatus, "pending_review")
                        .orderByAsc(KnowledgeDocumentDO::getCreateTime)
        );
    }

    private KnowledgeDocumentDO getDocument(String docId) {
        KnowledgeDocumentDO doc = documentMapper.selectById(docId);
        if (doc == null) {
            throw new ClientException("文档不存在");
        }
        return doc;
    }

    private void validateTransition(KnowledgeDocumentDO doc, String expectedFrom, String targetTo) {
        String current = StrUtil.isBlank(doc.getReviewStatus()) ? "draft" : doc.getReviewStatus();
        if (!current.equals(expectedFrom)) {
            throw new ClientException(
                    String.format("文档当前状态为 %s，无法执行 %s 操作",
                            current, DocumentReviewStatus.valueOf(targetTo.toUpperCase()).getName())
            );
        }
    }
}
