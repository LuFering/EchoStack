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

package com.nageoffer.ai.ragent.knowledge.controller;

import com.nageoffer.ai.ragent.framework.annotation.RequirePermission;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KnowledgeDocumentDO;
import com.nageoffer.ai.ragent.knowledge.service.DocumentReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文档审核控制器
 * 提供文档的提交审核、通过、驳回、发布等功能
 */
@RestController
@RequiredArgsConstructor
public class DocumentReviewController {

    private final DocumentReviewService documentReviewService;

    /**
     * 查询待审核文档列表
     */
    @GetMapping("/knowledge-base/docs/review/pending")
    @RequirePermission("document:review")
    public Result<List<KnowledgeDocumentDO>> listPendingReview(
            @RequestParam(value = "kbId", required = false) String kbId) {
        return Results.success(documentReviewService.listPendingReview(kbId));
    }

    /**
     * 提交审核（草稿 -> 待审核）
     */
    @PostMapping("/knowledge-base/docs/{docId}/review/submit")
    public Result<Void> submitForReview(@PathVariable String docId) {
        documentReviewService.submitForReview(docId);
        return Results.success();
    }

    /**
     * 审核通过
     */
    @PostMapping("/knowledge-base/docs/{docId}/review/approve")
    @RequirePermission("document:review")
    public Result<Void> approve(@PathVariable String docId,
                                @RequestBody(required = false) Map<String, String> body) {
        String comment = body != null ? body.getOrDefault("comment", "") : "";
        documentReviewService.approve(docId, comment);
        return Results.success();
    }

    /**
     * 审核驳回
     */
    @PostMapping("/knowledge-base/docs/{docId}/review/reject")
    @RequirePermission("document:review")
    public Result<Void> reject(@PathVariable String docId,
                               @RequestBody Map<String, String> body) {
        String reason = body != null ? body.getOrDefault("reason", "") : "";
        documentReviewService.reject(docId, reason);
        return Results.success();
    }

    /**
     * 发布文档（已通过 -> 已发布）
     */
    @PostMapping("/knowledge-base/docs/{docId}/review/publish")
    @RequirePermission("document:review")
    public Result<Void> publish(@PathVariable String docId) {
        documentReviewService.publish(docId);
        return Results.success();
    }
}
