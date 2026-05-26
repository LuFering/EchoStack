import { api } from "@/services/api";
import type { AuthProvider, CurrentUser, User } from "@/types";

export interface LoginResponse extends User {}
export interface CurrentUserResponse extends CurrentUser {}

export async function login(username: string, password: string, providerType?: string) {
  return api.post<LoginResponse>("/auth/login", {
    username,
    password,
    providerType: providerType || "local",
  });
}

export async function logout() {
  return api.post<void>("/auth/logout");
}

export async function getCurrentUser() {
  return api.get<CurrentUserResponse>("/user/me");
}

/** 获取已启用的认证提供者列表（SSO 按钮） */
export async function getEnabledProviders() {
  return api.get<AuthProvider[]>("/auth/providers");
}
