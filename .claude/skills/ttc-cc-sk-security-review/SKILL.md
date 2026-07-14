---
name: security-review
description: 安全审查 — 支付安全、数据安全、权限校验、依赖漏洞
triggers:
  - "安全审查"
  - "安全审计"
  - "漏洞检查"
---

# 安全审查

## 审查范围

### 支付安全
- 金额使用 BigDecimal（非 float/double）
- 退款幂等性（防重复退款）
- 支付通知签名校验
- 敏感信息脱敏

### 数据安全
- SQL 注入防护（MyBatis 参数化查询）
- XSS 防护
- 配置文件中的 AK/SK 不硬编码

### 依赖安全
```bash
gradlew dependencyCheckAnalyze  # OWASP 依赖检查
```

### 权限控制
- buyer/seller/manager 接口权限隔离
- 租户数据隔离
- 越权操作防护

## 输出
按严重程度分级：🔴 高危 / 🟡 中危 / 🟢 低危
