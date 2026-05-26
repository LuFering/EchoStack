import { useEffect, useState } from "react";
import { CheckCircle, RefreshCw, Send, XCircle, FileText } from "lucide-react";
import { toast } from "sonner";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import type { ReviewStatus } from "@/types";
import {
  listPendingReview,
  approveDocument,
  rejectDocument,
  publishDocument,
  type ReviewDocument,
} from "@/services/documentReviewService";
import { getKnowledgeBasesPage, type KnowledgeBase } from "@/services/knowledgeService";
import { getErrorMessage } from "@/utils/error";

const statusLabels: Record<ReviewStatus, string> = {
  draft: "草稿",
  pending_review: "待审核",
  approved: "已通过",
  rejected: "已驳回",
  published: "已发布",
};

const statusColors: Record<ReviewStatus, "secondary" | "default" | "outline" | "destructive"> = {
  draft: "secondary",
  pending_review: "default",
  approved: "outline",
  rejected: "destructive",
  published: "default",
};

export function DocumentReviewPage() {
  const [documents, setDocuments] = useState<ReviewDocument[]>([]);
  const [loading, setLoading] = useState(true);
  const [kbList, setKbList] = useState<KnowledgeBase[]>([]);
  const [selectedKbId, setSelectedKbId] = useState<string>("");
  const [rejectDialog, setRejectDialog] = useState<{
    open: boolean;
    doc: ReviewDocument | null;
  }>({ open: false, doc: null });
  const [rejectReason, setRejectReason] = useState("");

  const loadData = async () => {
    try {
      setLoading(true);
      const [docs, kbs] = await Promise.all([
        listPendingReview(selectedKbId || undefined),
        getKnowledgeBasesPage(1, 200),
      ]);
      setDocuments(docs);
      setKbList(kbs.records || []);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载审核列表失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [selectedKbId]);

  const handleApprove = async (doc: ReviewDocument) => {
    try {
      await approveDocument(doc.id);
      toast.success(`"${doc.docName}" 已审核通过`);
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "审批失败"));
    }
  };

  const handleReject = async () => {
    if (!rejectDialog.doc || !rejectReason.trim()) {
      toast.error("请填写驳回原因");
      return;
    }
    try {
      await rejectDocument(rejectDialog.doc.id, rejectReason.trim());
      toast.success(`"${rejectDialog.doc.docName}" 已驳回`);
      setRejectDialog({ open: false, doc: null });
      setRejectReason("");
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "驳回失败"));
    }
  };

  const handlePublish = async (doc: ReviewDocument) => {
    try {
      await publishDocument(doc.id);
      toast.success(`"${doc.docName}" 已发布`);
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "发布失败"));
    }
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return "-";
    return new Date(dateStr).toLocaleString("zh-CN");
  };

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">文档审核</h1>
          <p className="admin-page-subtitle">审核待发布的文档内容</p>
        </div>
        <div className="admin-page-actions">
          <Select value={selectedKbId} onValueChange={setSelectedKbId}>
            <SelectTrigger className="w-[200px]">
              <SelectValue placeholder="全部知识库" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">全部知识库</SelectItem>
              {kbList.map((kb) => (
                <SelectItem key={kb.id} value={kb.id}>
                  {kb.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          <Button variant="outline" onClick={loadData}>
            <RefreshCw className="w-4 h-4 mr-2" />
            刷新
          </Button>
        </div>
      </div>

      <Card>
        <CardContent className="pt-6">
          {loading ? (
            <div className="text-center py-8 text-muted-foreground">加载中...</div>
          ) : documents.length === 0 ? (
            <div className="text-center py-12 text-muted-foreground">
              <FileText className="mx-auto h-10 w-10 mb-3 text-slate-300" />
              <p className="text-sm">暂无待审核文档</p>
              <p className="text-xs mt-1">可以通过知识库文档页提交文档审核</p>
            </div>
          ) : (
            <div className="space-y-3">
              {documents.map((doc) => (
                <div
                  key={doc.id}
                  className="flex items-center justify-between rounded-lg border px-4 py-3 hover:bg-slate-50/50 transition-colors"
                >
                  <div className="flex items-center gap-3 min-w-0">
                    <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-amber-50">
                      <FileText className="h-5 w-5 text-amber-600" />
                    </div>
                    <div className="min-w-0">
                      <div className="flex items-center gap-2">
                        <span className="font-medium text-slate-900 truncate max-w-[300px]">
                          {doc.docName}
                        </span>
                        <Badge variant={statusColors[doc.reviewStatus]}>
                          {statusLabels[doc.reviewStatus]}
                        </Badge>
                      </div>
                      <div className="flex items-center gap-2 mt-1 text-xs text-muted-foreground">
                        <span>创建者: {doc.createdBy || "-"}</span>
                        <span>·</span>
                        <span>提交时间: {formatDate(doc.createTime)}</span>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-2 shrink-0">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleApprove(doc)}
                      className="text-green-600 hover:text-green-700 hover:bg-green-50"
                    >
                      <CheckCircle className="w-4 h-4 mr-1" />
                      通过
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setRejectDialog({ open: true, doc })}
                      className="text-red-600 hover:text-red-700 hover:bg-red-50"
                    >
                      <XCircle className="w-4 h-4 mr-1" />
                      驳回
                    </Button>
                    {doc.reviewStatus === "approved" && (
                      <Button
                        variant="default"
                        size="sm"
                        onClick={() => handlePublish(doc)}
                      >
                        <Send className="w-4 h-4 mr-1" />
                        发布
                      </Button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      {/* Reject reason dialog */}
      <Dialog
        open={rejectDialog.open}
        onOpenChange={(open) => {
          if (!open) {
            setRejectDialog({ open: false, doc: null });
            setRejectReason("");
          }
        }}
      >
        <DialogContent className="sm:max-w-[420px]">
          <DialogHeader>
            <DialogTitle>驳回文档</DialogTitle>
            <DialogDescription>
              请填写驳回原因，帮助提交者了解需要修改的内容
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-2">
            <Label>驳回原因 <span className="text-destructive">*</span></Label>
            <Textarea
              placeholder="例如：文档内容存在错误，请核实后重新提交"
              value={rejectReason}
              onChange={(e) => setRejectReason(e.target.value)}
              rows={4}
            />
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => { setRejectDialog({ open: false, doc: null }); setRejectReason(""); }}>
              取消
            </Button>
            <Button
              onClick={handleReject}
              className="bg-destructive text-destructive-foreground"
              disabled={!rejectReason.trim()}
            >
              确认驳回
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
