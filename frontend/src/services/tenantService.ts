import { api } from "@/services/api";
import type { Tenant } from "@/types";

export interface TenantPageResult {
  records: Tenant[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export async function getTenants(page = 1, size = 10, keyword?: string): Promise<TenantPageResult> {
  return api.get<TenantPageResult, TenantPageResult>("/tenants", {
    params: { page, size, keyword },
  });
}

export async function createTenant(data: {
  name: string;
  domain?: string;
  contactName?: string;
  contactEmail?: string;
  contactPhone?: string;
  planType?: string;
  maxUsers?: number;
  maxKb?: number;
  remark?: string;
}): Promise<Tenant> {
  return api.post<Tenant, Tenant>("/tenants", data);
}

export async function updateTenant(id: string, data: Partial<Tenant>): Promise<Tenant> {
  return api.put<Tenant, Tenant>(`/tenants/${id}`, data);
}

export async function deleteTenant(id: string): Promise<void> {
  await api.delete(`/tenants/${id}`);
}

export async function toggleTenantStatus(id: string, status: string): Promise<void> {
  await api.put(`/tenants/${id}/status`, null, { params: { status } });
}
