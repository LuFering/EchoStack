import { api } from "@/services/api";
import type { KbMemberRole } from "@/types";

export interface KbMember {
  id: string;
  kbId: string;
  userId: string;
  role: KbMemberRole;
  createTime: string;
  updateTime: string;
}

/** 获取知识库成员列表 */
export async function listKbMembers(kbId: string): Promise<KbMember[]> {
  return api.get<KbMember[], KbMember[]>(`/knowledge/kb/${kbId}/members`);
}

/** 添加成员 */
export async function addKbMember(kbId: string, userId: string, role: KbMemberRole): Promise<void> {
  await api.post(`/knowledge/kb/${kbId}/members`, null, {
    params: { userId, role },
  });
}

/** 更新成员角色 */
export async function updateKbMemberRole(
  kbId: string,
  userId: string,
  role: KbMemberRole
): Promise<void> {
  await api.put(`/knowledge/kb/${kbId}/members/${userId}`, null, {
    params: { role },
  });
}

/** 移除成员 */
export async function removeKbMember(kbId: string, userId: string): Promise<void> {
  await api.delete(`/knowledge/kb/${kbId}/members/${userId}`);
}

/** 获取当前用户在知识库的角色 */
export async function getMyKbRole(kbId: string): Promise<string> {
  return api.get<string, string>(`/knowledge/kb/${kbId}/members/my-role`);
}
