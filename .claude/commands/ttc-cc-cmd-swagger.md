---
description: 生成并查看 OpenAPI / Swagger 文档
parameters:
  - name: action
    type: string
    enum: [view, export]
    default: view
    description: 查看或导出 API 文档
---

# API 文档

## 查看文档

启动应用后访问：
- Knife4j UI: `http://localhost:{port}/doc.html`
- OpenAPI JSON: `http://localhost:{port}/v3/api-docs`

## 检查清单

- [ ] 所有 Controller 是否有 `@Tag` 注解
- [ ] 所有接口方法是否有 `@Operation` 注解
- [ ] DTO 字段是否有 `@Schema` 注解
- [ ] 分页接口是否返回正确结构

## 构建时生成
```bash
gradlew :taotao-cloud-payment-assembly:bootJar
```
