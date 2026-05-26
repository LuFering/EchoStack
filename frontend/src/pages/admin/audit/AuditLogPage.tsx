import { useEffect, useState } from "react";
import { RefreshCw, Shield, Search } from "lucide-react";
import { toast } from "sonner";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { getAuditLogs, type AuditPageResult } from "@/services/auditLogService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 20;

const actionOptions = [
  { value: "", label: "全部操作" },
  { value: "login", label: "登录" },
  { value: "logout", label: "登出" },
  { value: "create", label: "创建" },
  { value: "update", label: "编辑" },
  { value: "delete", label: "删除" },
  { value: "upload", label: "上传" },
  { value: "download", label: "下载" },
  { value: "review", label: "审核" },
  { value: "publish", label: "发布" },
];

const resourceTypeOptions = [
  { value: "", label: "全部类型" },
  { value: "tenant", label: "租户" },
  { value: "user", label: "用户" },
  { value: "knowledge_base", label: "知识库" },
  { value: "document", label: "文档" },
  { value: "role", label: "角色" },
  { value: "system", label: "系统" },
];

export function AuditLogPage() {
  const [pageData, setPageData] = useState<AuditPageResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [filters, setFilters] = useState({
    userId: "",
    action: "",
    resourceType: "",
  });

  const loadLogs = async () => {
    try {
      setLoading(true);
      const data = await getAuditLogs(pageNo, PAGE_SIZE, {
        userId: filters.userId || undefined,
        action: filters.action || undefined,
        resourceType: filters.resourceType || undefined,
      });
      setPageData(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载审计日志失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLogs();
  }, [pageNo, filters.action, filters.resourceType]);

  const handleSearch = () => {
    setPageNo(1);
    loadLogs();
  };

  const handleRefresh = () => {
    setPageNo(1);
    setFilters({ userId: "", action: "", resourceType: "" });
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return "-";
    return new Date(dateStr).toLocaleString("zh-CN");
  };

  const logs = pageData?.records || [];

  const actionBadge = (action: string) => {
    const colorMap: Record<string, "default" | "secondary" | "outline" | "destructive"> = {
      login: "default",
      logout: "secondary",
      create: "default",
      update: "secondary",
      delete: "destructive",
      upload: "default",
      download: "secondary",
      review: "outline",
      publish: "default",
    };
    return <Badge variant={colorMap[action] || "outline"}>{action}</Badge>;
  };

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">审计日志</h1>
          <p className="admin-page-subtitle">系统操作记录与合规审计追溯</p>
        </div>
      </div>

      <div className="mb-4 flex flex-wrap items-center gap-3">
        <Input
          value={filters.userId}
          onChange={(e) => setFilters((prev) => ({ ...prev, userId: e.target.value }))}
          placeholder="搜索用户ID"
          className="w-[200px]"
        />
        <Select
          value={filters.action}
          onValueChange={(v) => setFilters((prev) => ({ ...prev, action: v }))}
        >
          <SelectTrigger className="w-[140px]">
            <SelectValue placeholder="全部操作" />
          </SelectTrigger>
          <SelectContent>
            {actionOptions.map((opt) => (
              <SelectItem key={opt.value} value={opt.value}>
                {opt.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Select
          value={filters.resourceType}
          onValueChange={(v) => setFilters((prev) => ({ ...prev, resourceType: v }))}
        >
          <SelectTrigger className="w-[140px]">
            <SelectValue placeholder="全部类型" />
          </SelectTrigger>
          <SelectContent>
            {resourceTypeOptions.map((opt) => (
              <SelectItem key={opt.value} value={opt.value}>
                {opt.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Button variant="outline" onClick={handleSearch}>
          <Search className="w-4 h-4 mr-1" />
          查询
        </Button>
        <Button variant="outline" onClick={handleRefresh}>
          <RefreshCw className="w-4 h-4 mr-1" />
          重置
        </Button>
      </div>

      <Card>
        <CardContent className="pt-6">
          {loading ? (
            <div className="text-center py-8 text-muted-foreground">加载中...</div>
          ) : logs.length === 0 ? (
            <div className="text-center py-12 text-muted-foreground">
              <Shield className="mx-auto h-10 w-10 mb-3 text-slate-300" />
              <p className="text-sm">暂无审计日志</p>
            </div>
          ) : (
            <Table className="min-w-[900px]">
              <TableHeader>
                <TableRow>
                  <TableHead className="w-[180px]">操作时间</TableHead>
                  <TableHead className="w-[100px]">用户</TableHead>
                  <TableHead className="w-[100px]">操作</TableHead>
                  <TableHead className="w-[120px]">资源类型</TableHead>
                  <TableHead className="w-[180px]">资源名称</TableHead>
                  <TableHead className="w-[140px]">IP地址</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {logs.map((log) => (
                  <TableRow key={log.id}>
                    <TableCell className="text-muted-foreground text-sm">
                      {formatDate(log.createTime)}
                    </TableCell>
                    <TableCell className="text-sm font-mono">{log.userId}</TableCell>
                    <TableCell>{actionBadge(log.action)}</TableCell>
                    <TableCell className="text-sm text-muted-foreground">
                      {log.resourceType}
                    </TableCell>
                    <TableCell className="text-sm max-w-[180px] truncate">
                      {log.resourceName || "-"}
                    </TableCell>
                    <TableCell className="text-xs font-mono text-muted-foreground">
                      {log.clientIp || "-"}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      {pageData ? (
        <div className="mt-4 flex flex-wrap items-center justify-between gap-2 text-sm text-slate-500">
          <span>共 {pageData.total} 条</span>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.max(1, prev - 1))}
              disabled={pageData.current <= 1}
            >
              上一页
            </Button>
            <span>
              {pageData.current} / {pageData.pages}
            </span>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.min(pageData.pages || 1, prev + 1))}
              disabled={pageData.current >= pageData.pages}
            >
              下一页
            </Button>
          </div>
        </div>
      ) : null}
    </div>
  );
}
