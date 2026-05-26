import { useEffect, useState } from "react";
import { Plus, Trash2, Users, Shield, User, Eye, UserPlus } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Avatar } from "@/components/common/Avatar";
import {
  listKbMembers,
  addKbMember,
  updateKbMemberRole,
  removeKbMember,
  type KbMember,
} from "@/services/kbMemberService";
import { getUsersPage, type UserItem } from "@/services/userService";
import type { KbMemberRole } from "@/types";
import { getErrorMessage } from "@/utils/error";

const roleOptions: { value: KbMemberRole; label: string; icon: typeof Shield }[] = [
  { value: "admin", label: "管理员", icon: Shield },
  { value: "editor", label: "编辑者", icon: User },
  { value: "viewer", label: "查看者", icon: Eye },
];

interface Props {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  kbId: string;
  kbName: string;
}

export function KbMemberDialog({ open, onOpenChange, kbId, kbName }: Props) {
  const [members, setMembers] = useState<KbMember[]>([]);
  const [loading, setLoading] = useState(false);
  const [userList, setUserList] = useState<UserItem[]>([]);
  const [userSearch, setUserSearch] = useState("");
  const [showAddForm, setShowAddForm] = useState(false);
  const [newMemberRole, setNewMemberRole] = useState<KbMemberRole>("viewer");
  const [selectedUserId, setSelectedUserId] = useState("");

  const loadMembers = async () => {
    try {
      setLoading(true);
      const data = await listKbMembers(kbId);
      setMembers(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载成员列表失败"));
    } finally {
      setLoading(false);
    }
  };

  const loadUsers = async (keyword?: string) => {
    try {
      const data = await getUsersPage(1, 20, keyword);
      setUserList(data.records || []);
    } catch {
      setUserList([]);
    }
  };

  useEffect(() => {
    if (open) {
      loadMembers();
      loadUsers();
    }
  }, [open, kbId]);

  const handleAdd = async () => {
    if (!selectedUserId) {
      toast.error("请选择用户");
      return;
    }
    try {
      await addKbMember(kbId, selectedUserId, newMemberRole);
      toast.success("成员添加成功");
      setShowAddForm(false);
      setSelectedUserId("");
      setNewMemberRole("viewer");
      await loadMembers();
    } catch (error) {
      toast.error(getErrorMessage(error, "添加失败"));
    }
  };

  const handleRoleChange = async (userId: string, role: KbMemberRole) => {
    try {
      await updateKbMemberRole(kbId, userId, role);
      toast.success("角色已更新");
      await loadMembers();
    } catch (error) {
      toast.error(getErrorMessage(error, "更新失败"));
    }
  };

  const handleRemove = async (userId: string, _userName?: string) => {
    try {
      await removeKbMember(kbId, userId);
      toast.success("成员已移除");
      await loadMembers();
    } catch (error) {
      toast.error(getErrorMessage(error, "移除失败"));
    }
  };

  const filteredUsers = userList.filter(
    (u) => !members.some((m) => m.userId === u.id)
  );

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[560px]">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Users className="h-5 w-5" />
            知识库成员管理
          </DialogTitle>
          <DialogDescription>
            {kbName} - 管理知识库的访问成员及角色
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          {/* Add member form */}
          {!showAddForm ? (
            <Button
              variant="outline"
              className="w-full"
              onClick={() => {
                setShowAddForm(true);
                loadUsers();
              }}
            >
              <UserPlus className="mr-2 h-4 w-4" />
              添加成员
            </Button>
          ) : (
            <div className="rounded-lg border p-4 space-y-3">
              <div className="flex items-center gap-3">
                <div className="flex-1 space-y-1">
                  <Label className="text-xs">选择用户</Label>
                  <Input
                    placeholder="搜索用户..."
                    value={userSearch}
                    onChange={(e) => {
                      setUserSearch(e.target.value);
                      loadUsers(e.target.value);
                    }}
                  />
                  {filteredUsers.length > 0 && (
                    <Select value={selectedUserId} onValueChange={setSelectedUserId}>
                      <SelectTrigger className="w-full">
                        <SelectValue placeholder="点击选择用户" />
                      </SelectTrigger>
                      <SelectContent>
                        {filteredUsers.map((u) => (
                          <SelectItem key={u.id} value={u.id}>
                            {u.username}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  )}
                  {filteredUsers.length === 0 && (
                    <p className="text-xs text-muted-foreground">没有可添加的用户</p>
                  )}
                </div>
                <div className="w-[130px] space-y-1">
                  <Label className="text-xs">角色</Label>
                  <Select value={newMemberRole} onValueChange={(v) => setNewMemberRole(v as KbMemberRole)}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {roleOptions.map((opt) => (
                        <SelectItem key={opt.value} value={opt.value}>
                          {opt.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="flex justify-end gap-2">
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => {
                    setShowAddForm(false);
                    setSelectedUserId("");
                  }}
                >
                  取消
                </Button>
                <Button size="sm" onClick={handleAdd} disabled={!selectedUserId}>
                  <Plus className="mr-1 h-4 w-4" />
                  添加
                </Button>
              </div>
            </div>
          )}

          {/* Member list */}
          {loading ? (
            <div className="text-center py-8 text-muted-foreground">加载中...</div>
          ) : members.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground text-sm">
              暂无成员，点击上方按钮添加
            </div>
          ) : (
            <div className="space-y-2 max-h-[320px] overflow-y-auto">
              {members.map((member) => (
                <div
                  key={member.id}
                  className="flex items-center justify-between rounded-lg border p-3"
                >
                  <div className="flex items-center gap-3">
                    <Avatar
                      name={member.userId}
                      className="h-8 w-8 bg-slate-100 text-xs"
                    />
                    <div>
                      <div className="text-sm font-medium">{member.userId}</div>
                      <div className="text-xs text-muted-foreground">
                        {formatDate(member.createTime)} 加入
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Select
                      value={member.role}
                      onValueChange={(v) => handleRoleChange(member.userId, v as KbMemberRole)}
                    >
                      <SelectTrigger className="h-8 w-[100px]">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        {roleOptions.map((opt) => (
                          <SelectItem key={opt.value} value={opt.value}>
                            {opt.label}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    <Button
                      variant="ghost"
                      size="sm"
                      className="text-destructive hover:text-destructive"
                      onClick={() => handleRemove(member.userId, member.userId)}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}

function formatDate(dateStr?: string | null) {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleDateString("zh-CN");
}
