---
description: 部署应用到指定环境（dev/test/pre/pro）
parameters:
  - name: env
    type: string
    enum: [dev, test, pre, pro]
    required: true
    description: 部署目标环境
  - name: skipTests
    type: boolean
    default: false
    description: 是否跳过测试
---

# 部署工作流

## 执行步骤

### 1. 运行测试
{% if not skipTests %}
```bash
gradlew test
```
{% endif %}

### 2. 打包
```bash
gradlew :taotao-cloud-payment-assembly:bootJar
```

### 3. 启动
```bash
java --enable-preview \
  -jar taotao-cloud-payment-assembly/build/libs/taotao-cloud-payment-assembly-*.jar \
  --spring.profiles.active={{ env }}
```

### 4. 健康检查
```bash
curl -f http://localhost:8080/actuator/health
```

## 输出报告
```
环境: {{ env }}
JAR: taotao-cloud-payment-assembly-*.jar
健康检查: PASS/FAIL
```
