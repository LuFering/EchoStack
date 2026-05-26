import { useEffect, useState } from "react";
import { Pencil, Plus, RefreshCw, Trash2, Building2, XCircle, CheckCircle } from "lucide-react";
import { toast } from "sonner";

import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "@/components/ui/alert-dialog";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Textarea } from "@/components/ui/textarea";
import { getErrorMessage } from "@/utils/error";
import {
  getTenants,
  createTenant,
  updateTenant,
  deleteTenant,
  toggleTenantStatus,
  type TenantPageResult,
} from "@/services/tenantService";
import type { Tenant } from "@/types";

const PAGE_SIZE = 10;

const planTypeOptions = [
  { value: "free", label: "免费版" },
  { value: "pro", label: "专业版" },
  { value: "enterprise", label: "企业版" },
];

const buildEmptyForm = () => ({
  name: "",
  domain: "",
  contactName: "",
  contactEmail: "",
  contactPhone: "",
  planType: "free",
  maxUsers: 50,
  maxKb: 5,
  remark: "",
});

export function TenantListPage() {
  const [pageData, setPageData] = useState<TenantPageResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchInput, setSearchInput] = useState("");
  const [keyword, setKeyword] = useState("");
  const [pageNo, setPageNo] = useState(1);
  const [deleteTarget, setDeleteTarget] = useState<Tenant | null>(null);
  const [dialogState, setDialogState] = useState<{
    open: boolean;
    mode: "create" | "edit";
    tenant: Tenant | null;
  }>({ open: false, mode: "create", tenant: null });
  const [form, setForm] = useState(buildEmptyForm());

  const tenants = pageData?.records || [];

  const loadTenants = async (current = pageNo, name = keyword) => {
    try {
      setLoading(true);
      const data = await getTenants(current, PAGE_SIZE, name || undefined);
      setPageData(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载租户列表失败"));
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTenants();
  }, [pageNo, keyword]);

  const handleSearch = () => {
    setPageNo(1);
    setKeyword(searchInput.trim());
  };

  const handleRefresh = () => {
    setPageNo(1);
    setKeyword("");
    setSearchInput("");
    loadTenants(1, "");
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await deleteTenant(deleteTarget.id);
      toast.success("删除成功");
      setDeleteTarget(null);
      setPageNo(1);
      await loadTenants(1, keyword);
    } catch (error) {
      toast.error(getErrorMessage(error, "删除失败"));
      console.error(error);
    }
  };

  const handleToggleStatus = async (tenant: Tenant) => {
    const newStatus = tenant.status === "active" ? "disabled" : "active";
    try {
      await toggleTenantStatus(tenant.id, newStatus);
      toast.success(newStatus === "active" ? "已启用" : "已禁用");
      await loadTenants(pageNo, keyword);
    } catch (error) {
      toast.error(getErrorMessage(error, "操作失败"));
    }
  };

  const openCreateDialog = () => {
    setForm(buildEmptyForm());
    setDialogState({ open: true, mode: "create", tenant: null });
  };

  const openEditDialog = (tenant: Tenant) => {
    setForm({
      name: tenant.name || "",
      domain: tenant.domain || "",
      contactName: tenant.contactName || "",
      contactEmail: tenant.contactEmail || "",
      contactPhone: tenant.contactPhone || "",
      planType: tenant.planType || "free",
      maxUsers: tenant.maxUsers || 50,
      maxKb: tenant.maxKb || 5,
      remark: tenant.remark || "",
    });
    setDialogState({ open: true, mode: "edit", tenant });
  };

  const handleSave = async () => {
    const trimmedName = form.name.trim();
    if (!trimmedName) {
      toast.error("请输入租户名称");
      return;
    }

    try {
      if (dialogState.mode === "create") {
        await createTenant({
          name: trimmedName,
          domain: form.domain.trim() || undefined,
          contactName: form.contactName.trim() || undefined,
          contactEmail: form.contactEmail.trim() || undefined,
          contactPhone: form.contactPhone.trim() || undefined,
          planType: form.planType || undefined,
          maxUsers: form.maxUsers,
          maxKb: form.maxKb,
          remark: form.remark.trim() || undefined,
        });
        toast.success("创建成功");
        setPageNo(1);
        await loadTenants(1, keyword);
      } else if (dialogState.tenant) {
        await updateTenant(dialogState.tenant.id, {
          name: trimmedName,
          domain: form.domain.trim() || undefined,
          contactName: form.contactName.trim() || undefined,
          contactEmail: form.contactEmail.trim() || undefined,
          contactPhone: form.contactPhone.trim() || undefined,
          planType: form.planType || undefined,
          maxUsers: form.maxUsers,
          maxKb: form.maxKb,
          remark: form.remark.trim() || undefined,
        });
        toast.success("更新成功");
        await loadTenants(pageNo, keyword);
      }
      setDialogState({ open: false, mode: "create", tenant: null });
    } catch (error) {
      toast.error(getErrorMessage(error, "保存失败"));
      console.error(error);
    }
  };

  const formatDate = (dateStr?: string | null) => {
    if (!dateStr) return "-";
    return new Date(dateStr).toLocaleString("zh-CN");
  };

  const statusBadge = (status: string) => {
    if (status === "active") {
      return <Badge variant="default">启用</Badge>;
    }
    return <Badge variant="secondary">禁用</Badge>;
  };

  const planBadge = (planType: string) => {
    const map: Record<string, string> = { free: "免费版", pro: "专业版", enterprise: "企业版" };
    return <Badge variant="outline">{map[planType] || planType}</Badge>;
  };

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">租户管理</h1>
          <p className="admin-page-subtitle">管理所有企业租户，仅超级管理员可操作</p>
        </div>
        <div className="admin-page-actions">
          <Input
            value={searchInput}
            onChange={(event) => setSearchInput(event.target.value)}
            placeholder="搜索租户名称"
            className="w-[220px]"
          />
          <Button variant="outline" onClick={handleSearch}>
            搜索
          </Button>
          <Button variant="outline" onClick={handleRefresh}>
            <RefreshCw className="w-4 h-4 mr-2" />
            刷新
          </Button>
          <Button className="admin-primary-gradient" onClick={openCreateDialog}>
            <Plus className="w-4 h-4 mr-2" />
            新增租户
          </Button>
        </div>
      </div>

      <Card>
        <CardContent className="pt-6">
          {loading ? (
            <div className="text-center py-8 text-muted-foreground">加载中...</div>
          ) : tenants.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">暂无租户</div>
          ) : (
            <Table className="min-w-[960px]">
              <TableHeader>
                <TableRow>
                  <TableHead className="w-[200px]">租户名称</TableHead>
                  <TableHead className="w-[160px]">域名</TableHead>
                  <TableHead className="w-[100px]">套餐</TableHead>
                  <TableHead className="w-[100px]">状态</TableHead>
                  <TableHead className="w-[120px]">用户上限</TableHead>
                  <TableHead className="w-[120px]">知识库上限</TableHead>
                  <TableHead className="w-[180px]">创建时间</TableHead>
                  <TableHead className="w-[220px] text-left">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {tenants.map((tenant) => (
                  <TableRow key={tenant.id}>
                    <TableCell>
                      <div className="flex items-center gap-3">
                        <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-blue-50">
                          <Building2 className="h-5 w-5 text-blue-600" />
                        </div>
                        <div>
                          <div className="font-medium text-slate-900">{tenant.name}</div>
                          {tenant.contactName && (
                            <div className="text-xs text-slate-400">{tenant.contactName}</div>
                          )}
                        </div>
                      </div>
                    </TableCell>
                    <TableCell className="text-muted-foreground">{tenant.domain || "-"}</TableCell>
                    <TableCell>{planBadge(tenant.planType)}</TableCell>
                    <TableCell>{statusBadge(tenant.status)}</TableCell>
                    <TableCell className="text-muted-foreground">{tenant.maxUsers}</TableCell>
                    <TableCell className="text-muted-foreground">{tenant.maxKb}</TableCell>
                    <TableCell className="text-muted-foreground">{formatDate(tenant.createTime)}</TableCell>
                    <TableCell className="text-center">
                      <div className="flex justify-center gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => openEditDialog(tenant)}
                        >
                          <Pencil className="w-4 h-4 mr-0.5" />
                          编辑
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleToggleStatus(tenant)}
                        >
                          {tenant.status === "active" ? (
                            <><XCircle className="w-4 h-4 mr-0.5" />禁用</>
                          ) : (
                            <><CheckCircle className="w-4 h-4 mr-0.5" />启用</>
                          )}
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="text-destructive hover:text-destructive"
                          onClick={() => setDeleteTarget(tenant)}
                        >
                          <Trash2 className="w-4 h-4 mr-0.5" />
                          删除
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      {/* Delete confirmation */}
      <AlertDialog open={!!deleteTarget} onOpenChange={() => setDeleteTarget(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              此操作将永久删除该租户及其所有数据，包括用户、知识库和文档，无法恢复。确定要继续吗？
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>取消</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground">
              删除
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Create/Edit dialog */}
      <Dialog open={dialogState.open} onOpenChange={(open) => setDialogState((prev) => ({ ...prev, open }))}>
        <DialogContent className="sm:max-w-[520px]">
          <DialogHeader>
            <DialogTitle>{dialogState.mode === "create" ? "新增租户" : "编辑租户"}</DialogTitle>
            <DialogDescription>
              {dialogState.mode === "create" ? "创建新的企业租户，自动初始化管理员账号和默认知识库" : "修改租户基本信息"}
            </DialogDescription>
          </DialogHeader>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2 col-span-2">
              <Label>租户名称 <span className="text-destructive">*</span></Label>
              <Input
                value={form.name}
                onChange={(event) => setForm((prev) => ({ ...prev, name: event.target.value }))}
                placeholder="请输入企业名称"
              />
            </div>
            <div className="space-y-2 col-span-2">
              <Label>域名</Label>
              <Input
                value={form.domain}
                onChange={(event) => setForm((prev) => ({ ...prev, domain: event.target.value }))}
                placeholder="例如 company.example.com"
              />
            </div>
            <div className="space-y-2">
              <Label>联系人</Label>
              <Input
                value={form.contactName}
                onChange={(event) => setForm((prev) => ({ ...prev, contactName: event.target.value }))}
                placeholder="姓名"
              />
            </div>
            <div className="space-y-2">
              <Label>联系人邮箱</Label>
              <Input
                value={form.contactEmail}
                onChange={(event) => setForm((prev) => ({ ...prev, contactEmail: event.target.value }))}
                placeholder="email@company.com"
              />
            </div>
            <div className="space-y-2">
              <Label>联系人电话</Label>
              <Input
                value={form.contactPhone}
                onChange={(event) => setForm((prev) => ({ ...prev, contactPhone: event.target.value }))}
                placeholder="138****1234"
              />
            </div>
            <div className="space-y-2">
              <Label>套餐类型</Label>
              <Select value={form.planType} onValueChange={(value) => setForm((prev) => ({ ...prev, planType: value }))}>
                <SelectTrigger>
                  <SelectValue placeholder="请选择套餐" />
                </SelectTrigger>
                <SelectContent>
                  {planTypeOptions.map((option) => (
                    <SelectItem key={option.value} value={option.value}>{option.label}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label>最大用户数</Label>
              <Input
                type="number"
                value={form.maxUsers}
                onChange={(event) => setForm((prev) => ({ ...prev, maxUsers: Number(event.target.value) }))}
              />
            </div>
            <div className="space-y-2">
              <Label>最大知识库数</Label>
              <Input
                type="number"
                value={form.maxKb}
                onChange={(event) => setForm((prev) => ({ ...prev, maxKb: Number(event.target.value) }))}
              />
            </div>
            <div className="space-y-2 col-span-2">
              <Label>备注</Label>
              <Textarea
                value={form.remark}
                onChange={(event) => setForm((prev) => ({ ...prev, remark: event.target.value }))}
                placeholder="可选的备注信息"
                rows={2}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDialogState({ open: false, mode: "create", tenant: null })}>
              取消
            </Button>
            <Button onClick={handleSave}>
              {dialogState.mode === "create" ? (
                <><Plus className="mr-2 h-4 w-4" />创建</>
              ) : (
                <><Pencil className="mr-2 h-4 w-4" />保存</>
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Pagination */}
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
            <span>{pageData.current} / {pageData.pages}</span>
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
