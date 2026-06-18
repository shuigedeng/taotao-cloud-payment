---
description: 运行测试并生成 JaCoCo 覆盖率报告
parameters:
  - name: module
    type: string
    description: 测试模块（domain/application/infrastructure/interfaces 或完整模块名 taotao-cloud-payment-xxx）
    required: false
  - name: coverage
    type: boolean
    default: true
    description: 是否生成覆盖率报告
---

# 测试执行

## 执行步骤

### 1. 运行测试
{% if module %}
{% if module startsWith "taotao-cloud-payment-" %}
```bash
gradlew :{{ module }}:test
```
{% else %}
```bash
gradlew :taotao-cloud-payment-{{ module }}:test
```
{% endif %}
{% else %}
```bash
gradlew test
```
{% endif %}

### 2. 生成覆盖率报告
{% if coverage %}
```bash
gradlew jacocoTestReport
```
报告位置: `build/reports/jacoco/test/html/index.html`
{% endif %}

### 3. 输出结果
```
测试总数: {{ total }}
通过: {{ passed }}
失败: {{ failed }}
耗时: {{ duration }}ms

覆盖率:
  指令: {{ instructionCoverage }}%
  分支: {{ branchCoverage }}%
  行: {{ lineCoverage }}%
```
