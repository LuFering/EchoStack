-- ============================================
-- Ragent v2.0 Enterprise Upgrade
-- 多租户 + RBAC 权限 + 知识库成员管理
-- ============================================

-- ============================================
-- Part 1: Multi-Tenant
-- ============================================

-- 租户表
CREATE TABLE t_tenant (
    id          VARCHAR(20)  NOT NULL PRIMARY KEY,
    name        VARCHAR(128) NOT NULL,
    domain      VARCHAR(128),
    logo_url    VARCHAR(512),
    status      VARCHAR(16)  NOT NULL DEFAULT 'ACTIVE',
    plan_type   VARCHAR(32)  NOT NULL DEFAULT 'FREE',
    max_users   INTEGER      DEFAULT 50,
    max_kb      INTEGER      DEFAULT 5,
    max_storage BIGINT       DEFAULT 10737418240,
    expire_time TIMESTAMP,
    created_by  VARCHAR(20)  NOT NULL,
    contact_name  VARCHAR(64),
    contact_email VARCHAR(128),
    contact_phone VARCHAR(32),
    remark      VARCHAR(512),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT uk_tenant_name UNIQUE (name, deleted)
);
COMMENT ON TABLE t_tenant IS '租户表';
COMMENT ON COLUMN t_tenant.name IS '租户名称';
COMMENT ON COLUMN t_tenant.domain IS '租户域名';
COMMENT ON COLUMN t_tenant.status IS '状态：ACTIVE/SUSPENDED/EXPIRED';
COMMENT ON COLUMN t_tenant.plan_type IS '套餐类型：FREE/BASIC/PRO/ENTERPRISE';
COMMENT ON COLUMN t_tenant.max_users IS '最大用户数';
COMMENT ON COLUMN t_tenant.max_kb IS '最大知识库数';
COMMENT ON COLUMN t_tenant.max_storage IS '最大存储容量(字节)';

-- 部门表
CREATE TABLE t_department (
    id          VARCHAR(20)  NOT NULL PRIMARY KEY,
    tenant_id   VARCHAR(20)  NOT NULL,
    parent_id   VARCHAR(20),
    name        VARCHAR(128) NOT NULL,
    path        VARCHAR(512),
    sort_order  INTEGER      DEFAULT 0,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT    NOT NULL DEFAULT 0
);
CREATE INDEX idx_dept_tenant ON t_department (tenant_id);
CREATE INDEX idx_dept_parent ON t_department (parent_id);
COMMENT ON TABLE t_department IS '部门表(树形结构)';
COMMENT ON COLUMN t_department.tenant_id IS '租户ID';
COMMENT ON COLUMN t_department.parent_id IS '父部门ID';
COMMENT ON COLUMN t_department.name IS '部门名称';
COMMENT ON COLUMN t_department.path IS '部门路径(如: /1/2/3)';

-- ============================================
-- Part 2: Extend t_user for Enterprise
-- ============================================

ALTER TABLE t_user ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(20);
ALTER TABLE t_user ADD COLUMN IF NOT EXISTS department_id VARCHAR(20);
ALTER TABLE t_user ADD COLUMN IF NOT EXISTS status VARCHAR(16) DEFAULT 'ACTIVE';
ALTER TABLE t_user ADD COLUMN IF NOT EXISTS is_tenant_admin SMALLINT DEFAULT 0;
ALTER TABLE t_user ADD COLUMN IF NOT EXISTS real_name VARCHAR(64);
ALTER TABLE t_user ADD COLUMN IF NOT EXISTS email VARCHAR(128);
ALTER TABLE t_user ADD COLUMN IF NOT EXISTS phone VARCHAR(32);

CREATE INDEX IF NOT EXISTS idx_user_tenant ON t_user (tenant_id);
CREATE INDEX IF NOT EXISTS idx_user_dept ON t_user (department_id);

COMMENT ON COLUMN t_user.tenant_id IS '所属租户ID';
COMMENT ON COLUMN t_user.department_id IS '所属部门ID';
COMMENT ON COLUMN t_user.status IS '状态：ACTIVE/DISABLED/LOCKED';
COMMENT ON COLUMN t_user.is_tenant_admin IS '是否租户管理员 0:否 1:是';
COMMENT ON COLUMN t_user.real_name IS '真实姓名';
COMMENT ON COLUMN t_user.email IS '邮箱';
COMMENT ON COLUMN t_user.phone IS '手机号';

-- ============================================
-- Part 3: RBAC
-- ============================================

-- 权限资源表
CREATE TABLE t_permission (
    id            VARCHAR(20)  NOT NULL PRIMARY KEY,
    code          VARCHAR(128) NOT NULL,
    name          VARCHAR(128) NOT NULL,
    resource_type VARCHAR(32)  NOT NULL,
    action        VARCHAR(32)  NOT NULL,
    description   VARCHAR(256),
    parent_id     VARCHAR(20),
    sort_order    INTEGER      DEFAULT 0,
    create_time   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT uk_perm_code UNIQUE (code, deleted)
);
COMMENT ON TABLE t_permission IS '权限资源表';
COMMENT ON COLUMN t_permission.code IS '权限标识(如: kb:document:upload)';
COMMENT ON COLUMN t_permission.name IS '权限名称';
COMMENT ON COLUMN t_permission.resource_type IS '资源类型：kb/document/user/tenant/system';
COMMENT ON COLUMN t_permission.action IS '操作：create/read/update/delete/manage';

-- 角色表
CREATE TABLE t_role (
    id          VARCHAR(20)  NOT NULL PRIMARY KEY,
    tenant_id   VARCHAR(20),
    code        VARCHAR(64)  NOT NULL,
    name        VARCHAR(64)  NOT NULL,
    is_system   SMALLINT     NOT NULL DEFAULT 0,
    description VARCHAR(256),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT uk_role_code_tenant UNIQUE (tenant_id, code, deleted)
);
CREATE INDEX idx_role_tenant ON t_role (tenant_id);
COMMENT ON TABLE t_role IS '角色表';
COMMENT ON COLUMN t_role.tenant_id IS '租户ID(NULL表示系统全局角色)';
COMMENT ON COLUMN t_role.code IS '角色编码';
COMMENT ON COLUMN t_role.name IS '角色名称';
COMMENT ON COLUMN t_role.is_system IS '是否系统预设角色 0:否 1:是';

-- 角色-权限关联表
CREATE TABLE t_role_permission (
    id            VARCHAR(20) NOT NULL PRIMARY KEY,
    role_id       VARCHAR(20) NOT NULL,
    permission_id VARCHAR(20) NOT NULL,
    create_time   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_role_perm UNIQUE (role_id, permission_id)
);
CREATE INDEX idx_rp_role ON t_role_permission (role_id);
CREATE INDEX idx_rp_perm ON t_role_permission (permission_id);
COMMENT ON TABLE t_role_permission IS '角色-权限关联表';

-- 用户-角色关联表
CREATE TABLE t_user_role (
    id          VARCHAR(20) NOT NULL PRIMARY KEY,
    user_id     VARCHAR(20) NOT NULL,
    role_id     VARCHAR(20) NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);
CREATE INDEX idx_ur_user ON t_user_role (user_id);
CREATE INDEX idx_ur_role ON t_user_role (role_id);
COMMENT ON TABLE t_user_role IS '用户-角色关联表';

-- ============================================
-- Part 4: Knowledge Base Members
-- ============================================

CREATE TABLE t_kb_member (
    id          VARCHAR(20) NOT NULL PRIMARY KEY,
    kb_id       VARCHAR(20) NOT NULL,
    user_id     VARCHAR(20) NOT NULL,
    role        VARCHAR(16) NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_kb_member UNIQUE (kb_id, user_id)
);
CREATE INDEX idx_kbm_kb ON t_kb_member (kb_id);
CREATE INDEX idx_kbm_user ON t_kb_member (user_id);
COMMENT ON TABLE t_kb_member IS '知识库成员表';
COMMENT ON COLUMN t_kb_member.kb_id IS '知识库ID';
COMMENT ON COLUMN t_kb_member.user_id IS '用户ID';
COMMENT ON COLUMN t_kb_member.role IS '角色：admin/editor/viewer';

-- ============================================
-- Part 5: Extend t_knowledge_base
-- ============================================

ALTER TABLE t_knowledge_base ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(20);
ALTER TABLE t_knowledge_base ADD COLUMN IF NOT EXISTS visibility VARCHAR(16) DEFAULT 'TENANT';
ALTER TABLE t_knowledge_base ADD COLUMN IF NOT EXISTS description VARCHAR(512);

CREATE INDEX IF NOT EXISTS idx_kb_tenant ON t_knowledge_base (tenant_id);

COMMENT ON COLUMN t_knowledge_base.tenant_id IS '所属租户ID';
COMMENT ON COLUMN t_knowledge_base.visibility IS '可见性：PRIVATE/TENANT/PUBLIC';
COMMENT ON COLUMN t_knowledge_base.description IS '知识库描述';

-- ============================================
-- Part 6: Seed Data - Preset System Roles & Permissions
-- ============================================

INSERT INTO t_permission (id, code, name, resource_type, action, parent_id, sort_order) VALUES
-- 知识库权限
('perm_kb_list',    'kb:list',    '查看知识库列表', 'kb', 'read',   NULL, 1),
('perm_kb_create',  'kb:create',  '创建知识库',     'kb', 'create', NULL, 2),
('perm_kb_update',  'kb:update',  '编辑知识库',     'kb', 'update', NULL, 3),
('perm_kb_delete',  'kb:delete',  '删除知识库',     'kb', 'delete', NULL, 4),
('perm_kb_manage',  'kb:manage',  '管理知识库成员', 'kb', 'manage', NULL, 5),
-- 文档权限
('perm_doc_list',   'document:list',   '查看文档列表', 'document', 'read',   NULL, 10),
('perm_doc_upload', 'document:upload', '上传文档',     'document', 'create', NULL, 11),
('perm_doc_update', 'document:update', '编辑文档',     'document', 'update', NULL, 12),
('perm_doc_delete', 'document:delete', '删除文档',     'document', 'delete', NULL, 13),
('perm_doc_review', 'document:review', '审核文档',     'document', 'manage', NULL, 14),
-- 用户管理权限
('perm_user_list',   'user:list',   '查看用户列表',   'user', 'read',   NULL, 20),
('perm_user_create', 'user:create', '创建用户',       'user', 'create', NULL, 21),
('perm_user_update', 'user:update', '编辑用户',       'user', 'update', NULL, 22),
('perm_user_delete', 'user:delete', '删除用户',       'user', 'delete', NULL, 23),
-- 租户管理权限(仅super_admin)
('perm_tenant_list',   'tenant:list',   '查看租户列表',   'tenant', 'read',   NULL, 30),
('perm_tenant_create', 'tenant:create', '创建租户',       'tenant', 'create', NULL, 31),
('perm_tenant_update', 'tenant:update', '编辑租户',       'tenant', 'update', NULL, 32),
('perm_tenant_delete', 'tenant:delete', '删除租户',       'tenant', 'delete', NULL, 33),
-- 系统设置权限
('perm_system_config', 'system:config', '系统配置管理', 'system', 'manage', NULL, 40);

-- 预设系统角色

-- 超级管理员(系统级，tenant_id=NULL)
INSERT INTO t_role (id, tenant_id, code, name, is_system, description) VALUES
('role_super_admin', NULL, 'super_admin', '超级管理员', 1, '系统最高权限，管理所有租户和系统配置');

-- 租户管理员
INSERT INTO t_role (id, tenant_id, code, name, is_system, description) VALUES
('role_tenant_admin', NULL, 'tenant_admin', '租户管理员', 1, '租户内所有资源的管理权限');

-- 知识库管理员
INSERT INTO t_role (id, tenant_id, code, name, is_system, description) VALUES
('role_kb_admin', NULL, 'kb_admin', '知识库管理员', 1, '管理指定知识库的文档和成员');

-- 编辑者
INSERT INTO t_role (id, tenant_id, code, name, is_system, description) VALUES
('role_editor', NULL, 'editor', '编辑者', 1, '上传和编辑文档');

-- 查看者
INSERT INTO t_role (id, tenant_id, code, name, is_system, description) VALUES
('role_viewer', NULL, 'viewer', '查看者', 1, '只读查看知识库和文档');

-- 超级管理员拥有所有权限
INSERT INTO t_role_permission (id, role_id, permission_id) VALUES
('rp_sa_1',  'role_super_admin', 'perm_kb_list'),
('rp_sa_2',  'role_super_admin', 'perm_kb_create'),
('rp_sa_3',  'role_super_admin', 'perm_kb_update'),
('rp_sa_4',  'role_super_admin', 'perm_kb_delete'),
('rp_sa_5',  'role_super_admin', 'perm_kb_manage'),
('rp_sa_6',  'role_super_admin', 'perm_doc_list'),
('rp_sa_7',  'role_super_admin', 'perm_doc_upload'),
('rp_sa_8',  'role_super_admin', 'perm_doc_update'),
('rp_sa_9',  'role_super_admin', 'perm_doc_delete'),
('rp_sa_10', 'role_super_admin', 'perm_doc_review'),
('rp_sa_11', 'role_super_admin', 'perm_user_list'),
('rp_sa_12', 'role_super_admin', 'perm_user_create'),
('rp_sa_13', 'role_super_admin', 'perm_user_update'),
('rp_sa_14', 'role_super_admin', 'perm_user_delete'),
('rp_sa_15', 'role_super_admin', 'perm_tenant_list'),
('rp_sa_16', 'role_super_admin', 'perm_tenant_create'),
('rp_sa_17', 'role_super_admin', 'perm_tenant_update'),
('rp_sa_18', 'role_super_admin', 'perm_tenant_delete'),
('rp_sa_19', 'role_super_admin', 'perm_system_config');

-- 租户管理员拥有租户内所有权限(不含tenant管理)
INSERT INTO t_role_permission (id, role_id, permission_id) VALUES
('rp_ta_1',  'role_tenant_admin', 'perm_kb_list'),
('rp_ta_2',  'role_tenant_admin', 'perm_kb_create'),
('rp_ta_3',  'role_tenant_admin', 'perm_kb_update'),
('rp_ta_4',  'role_tenant_admin', 'perm_kb_delete'),
('rp_ta_5',  'role_tenant_admin', 'perm_kb_manage'),
('rp_ta_6',  'role_tenant_admin', 'perm_doc_list'),
('rp_ta_7',  'role_tenant_admin', 'perm_doc_upload'),
('rp_ta_8',  'role_tenant_admin', 'perm_doc_update'),
('rp_ta_9',  'role_tenant_admin', 'perm_doc_delete'),
('rp_ta_10', 'role_tenant_admin', 'perm_doc_review'),
('rp_ta_11', 'role_tenant_admin', 'perm_user_list'),
('rp_ta_12', 'role_tenant_admin', 'perm_user_create'),
('rp_ta_13', 'role_tenant_admin', 'perm_user_update'),
('rp_ta_14', 'role_tenant_admin', 'perm_user_delete');

-- 编辑者权限
INSERT INTO t_role_permission (id, role_id, permission_id) VALUES
('rp_ed_1', 'role_editor', 'perm_kb_list'),
('rp_ed_2', 'role_editor', 'perm_doc_list'),
('rp_ed_3', 'role_editor', 'perm_doc_upload'),
('rp_ed_4', 'role_editor', 'perm_doc_update');

-- 查看者权限
INSERT INTO t_role_permission (id, role_id, permission_id) VALUES
('rp_vw_1', 'role_viewer', 'perm_kb_list'),
('rp_vw_2', 'role_viewer', 'perm_doc_list');



-- ============================================
-- Part 7: Audit Log
-- ============================================

CREATE TABLE t_audit_log (
    id            VARCHAR(20)  NOT NULL PRIMARY KEY,
    tenant_id     VARCHAR(20),
    user_id       VARCHAR(20),
    username      VARCHAR(64),
    action        VARCHAR(64)  NOT NULL,
    resource_type VARCHAR(32)  NOT NULL,
    resource_id   VARCHAR(64),
    resource_name VARCHAR(256),
    detail        VARCHAR(1024),
    ip_address    VARCHAR(64),
    user_agent    VARCHAR(512),
    create_time   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_audit_tenant ON t_audit_log (tenant_id);
CREATE INDEX idx_audit_user ON t_audit_log (user_id);
CREATE INDEX idx_audit_action ON t_audit_log (action, resource_type);
CREATE INDEX idx_audit_time ON t_audit_log (create_time);
COMMENT ON TABLE t_audit_log IS '审计日志表（只追加不删除，不可篡改）';
COMMENT ON COLUMN t_audit_log.action IS '操作类型：CREATE/UPDATE/DELETE/LOGIN/LOGOUT/EXPORT';
COMMENT ON COLUMN t_audit_log.resource_type IS '资源类型：kb/document/user/tenant/system';
COMMENT ON COLUMN t_audit_log.resource_id IS '资源ID';
COMMENT ON COLUMN t_audit_log.detail IS '操作详情JSON';

-- ============================================
-- Part 8: Document Versioning Table
-- ============================================

CREATE TABLE t_document_version (
    id          VARCHAR(20)  NOT NULL PRIMARY KEY,
    doc_id      VARCHAR(20)  NOT NULL,
    version     INTEGER      NOT NULL,
    file_url    VARCHAR(1024) NOT NULL,
    file_size   BIGINT,
    changes     VARCHAR(1024),
    created_by  VARCHAR(20)  NOT NULL,
    create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_doc_version ON t_document_version (doc_id, version);
CREATE UNIQUE INDEX uk_doc_version ON t_document_version (doc_id, version);
COMMENT ON TABLE t_document_version IS '文档版本表';
COMMENT ON COLUMN t_document_version.version IS '版本号（从1开始递增）';
COMMENT ON COLUMN t_document_version.changes IS '变更说明';
