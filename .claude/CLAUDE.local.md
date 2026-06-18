# 个人开发配置

## 开发工具
- **IDE**: IntelliJ IDEA Ultimate
- **JDK**: graalvm-jdk-25（预览特性）
- **建模**: Miro（事件风暴）、PlantUML（领域模型图）

## 本地偏好
- 先写领域层单元测试（TDD）
- 开启 SQL 日志调试仓储实现
- 使用 `dev` 环境本地启动

## 本地启动
```bash
gradlew :taotao-cloud-payment-assembly:bootRun --args='--spring.profiles.active=dev'
```
