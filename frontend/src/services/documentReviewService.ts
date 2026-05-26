import { api } from "@/services/api";
import type { ReviewStatus } from "@/types";

export interface ReviewDocument {
  id: string;
  kbId: string;
  docName: string;
  sourceType?: string;
  reviewStatus: ReviewStatus;
  reviewerId?: string;
  reviewComment?: string;
  reviewTime?: string;
  createdBy?: string;
  createTime?: string;
  updateTime?: string;
}

/** 获取待审核文档列表 */
export async function listPendingReview(kbId?: string): Promise<ReviewDocument[]> {
  return api.get<ReviewDocument[], ReviewDocument[]>("/knowledge-base/docs/review/pending", {
    params: { kbId },
  });
}

/** 提交审核 */
export async function submitForReview(docId: string): Promise<void> {
  await api.post(`/knowledge-base/docs/${docId}/review/submit`);
}

/** 审核通过 */
export async function approveDocument(docId: string, comment?: string): Promise<void> {
  await api.post(`/knowledge-base/docs/${docId}/review/approve`, { comment: comment || "" });
}

/** 审核驳回 */
export async function rejectDocument(docId: string, reason: string): Promise<void> {
  await api.post(`/knowledge-base/docs/${docId}/review/reject`, { reason });
}

/** 发布文档 */
export async function publishDocument(docId: string): Promise<void> {
  await api.post(`/knowledge-base/docs/${docId}/review/publish`);
}
