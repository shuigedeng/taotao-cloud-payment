---
description: DDD 架构审查 — 检查领域模型、分层依赖、代码质量
parameters:
  - name: scope
    type: string
    enum: [domain, application, infrastructure, interfaces, all]
    default: all
    description: 审查范围
---

# DDD 代码审查

审查范围：`{{ scope }}`

## 审查清单

### 领域模型（Domain）
- [ ] 聚合根是否维护内部不变量
- [ ] 值对象是否不可变（final 字段、构造时自验证）
- [ ] 跨聚合是否通过 ID 引用
- [ ] 领域事件是否在聚合内 registerEvent
- [ ] 仓储接口是否在 domain 层定义
- [ ] 聚合根中是否注入了 Repository 或 Domain Service（❌ 禁止）

### 应用层（Application）
- [ ] 事务边界是否仅开在此层
- [ ] 是否包含业务规则判断（❌ 禁止）
- [ ] 是否直接调用 Mapper/DAO（❌ 禁止）

### 基础设施层（Infrastructure）
- [ ] 仓储实现是否正确映射 PO ↔ Domain
- [ ] 事件发布是否正确传递领域事件

### 接口层（Interfaces）
- [ ] Controller 是否不含业务逻辑
- [ ] 是否按 buyer/seller/manager/mall 分包
- [ ] 参数校验是否完整

### 代码质量
- [ ] 方法长度是否超过 50 行
- [ ] 是否存在 N+1 查询
- [ ] 金额是否使用 BigDecimal（❌ 禁止 float/double）
- [ ] 空值处理是否正确

## 输出格式
```markdown
### 🔴 严重（必须修复）
- [文件:行号] 问题 + 修复建议

### 🟡 警告（建议修复）
- [文件:行号] 问题 + 优化方案

### 🟢 通过项
- 列举做得好的地方
```
