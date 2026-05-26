import { api } from "@/services/api";

export interface AuditLog {
  id: string;
  userId: string;
  tenantId?: string;
  action: string;
  resourceType: string;
  resourceId?: string;
  resourceName?: string;
  detail?: string;
  clientIp?: string;
  createTime: string;
}

export interface AuditPageResult {
  records: AuditLog[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

/**
 * 分页查询审计日志
 */
export async function getAuditLogs(
  page = 1,
  size = 20,
  params?: {
    userId?: string;
    action?: string;
    resourceType?: string;
    startTime?: string;
    endTime?: string;
  }
): Promise<AuditPageResult> {
  return api.get<AuditPageResult, AuditPageResult>("/audit/logs", { params: { page, size, ...params } });
}
